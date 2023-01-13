package consume

import (
	"context"
	"encoding/json"
	"fmt"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"gihub.com/victorfernandesraton/dev-api-rest/infra/event"
	"github.com/jackc/pgx/v5"
	amqp "github.com/rabbitmq/amqp091-go"
	"log"
	"os"
)

type TransactionConsumeProviderInterface interface {
	GetQueue(*amqp.Channel) (amqp.Queue, error)
}

type TransactionConsume struct {
	Provider TransactionConsumeProviderInterface
	DB       *pgx.Conn
}

func (c *TransactionConsume) Listen() {
	conn, err := event.ConnectRabbitMQ()
	ch, err := conn.Channel()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to open a channel")
	}
	defer ch.Close()

	transactionQueue, err := c.Provider.GetQueue(ch)

	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to declare a queue")
		fmt.Fprintf(os.Stderr, err.Error())
	}

	msgs, err := ch.Consume(
		transactionQueue.Name, // queue
		"",                    // consumer
		true,                  // auto-ack
		false,                 // exclusive
		false,                 // no-local
		false,                 // no-wait
		nil,                   // args
	)

	query := `
		insert into public.transaction  ("from", "to", "value", created_at)
		values ($1, $2, $3, $4);`

	var forever chan struct{}

	go func() {
		for d := range msgs {
			body := new(domain.Transaction)
			if err := json.Unmarshal(d.Body, body); err != nil {
				log.Println(err)
			}
			log.Printf("Received a message: %s", d.Body)

			_, err := c.DB.Exec(context.Background(), query,
				body.From, body.To, body.Value, body.CreatedAt,
			)
			if err != nil {
				log.Println(err)
			}
		}
	}()

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")
	<-forever
}
