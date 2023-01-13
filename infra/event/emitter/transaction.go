package emitter

import (
	"context"
	"encoding/json"
	"fmt"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"gihub.com/victorfernandesraton/dev-api-rest/infra/event"
	amqp "github.com/rabbitmq/amqp091-go"
	"log"
	"os"
)

type TransactionEmitterProviderInterface interface {
	GetQueue(*amqp.Channel) (amqp.Queue, error)
}

type TransactionEmitter struct {
	Provider TransactionEmitterProviderInterface
}

func (e *TransactionEmitter) Execute(params *domain.Transaction) error {
	conn, err := event.ConnectRabbitMQ()
	ch, err := conn.Channel()
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to open a channel")
		return err
	}
	defer ch.Close()

	transactionQueue, err := e.Provider.GetQueue(ch)

	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to declare a queue")
		return err
	}

	body, err := json.Marshal(params)

	if err != nil {
		return err
	}

	if err := ch.PublishWithContext(context.Background(),
		"",
		transactionQueue.Name,
		false,
		false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(body),
		}); err != nil {
		return err
	}

	defer log.Println("snding message")

	return nil
}
