package event

import (
	amqp "github.com/rabbitmq/amqp091-go"
	"os"
)

func ConnectRabbitMQ() (*amqp.Connection, error) {
	return amqp.Dial(os.Getenv("RABBITMQ_URL"))
}
