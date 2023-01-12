package main

import (
	"context"
	"fmt"
	"gihub.com/victorfernandesraton/dev-api-rest/app/controller"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/infra/storage"
	"net/http"
	"os"

	"github.com/jackc/pgx/v5"
	"github.com/labstack/echo/v4"
)

func main() {
	e := echo.New()

	conn, err := pgx.Connect(context.Background(), os.Getenv("DATABASE_URL"))
	if err != nil {
		fmt.Fprintf(os.Stderr, "Unable to connect to database: %v\n", err)
		os.Exit(1)
	}
	defer conn.Close(context.Background())

	if err != nil {
		fmt.Fprintf(os.Stderr, "Unable to connect to database: %v\n", err)
		os.Exit(1)
	}
	defer conn.Close(context.Background())

	carryRepository := storage.CarrierRepository{
		DB: conn,
	}

	createCarryCommand := command.CreateCarrierCommand{
		CaryRepository: &carryRepository,
	}

	carrierController := controller.CarrierController{
		CreateCarrierCommand: &createCarryCommand,
	}
	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, ":-)")
	})

	e.POST("/carrier", func(c echo.Context) error {
		return carrierController.CreateCarrier(c)
	})
	e.Logger.Fatal(e.Start(":3000"))
}
