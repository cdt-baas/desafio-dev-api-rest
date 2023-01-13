package provider

import amqp "github.com/rabbitmq/amqp091-go"

type TransactionProvider struct {
}

func (p *TransactionProvider) GetQueue(ch *amqp.Channel) (amqp.Queue, error) {
	return ch.QueueDeclare(
		"transaction_queue", // name
		false,               // durable
		false,               // delete when unused
		false,               // exclusive
		false,               // no-wait
		nil,                 // arguments
	)
}
