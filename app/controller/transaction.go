package controller

import (
	"gihub.com/victorfernandesraton/dev-api-rest/adapter"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"github.com/labstack/echo/v4"
	"net/http"
)

type TransactionController struct {
	TransactionCommand *command.TransactionCommand
}

type TransactionAccountParams struct {
	AccountNumber uint64 `json:"account_number"`
	Agency        uint64 `json:"agency"`
}

type TransactionParams struct {
	From  *TransactionAccountParams `json:"from"`
	To    *TransactionAccountParams `json:"to"`
	Value float64                   `json:"value"`
}

func (c *TransactionController) CreateTransaction(ctx echo.Context) error {
	body := new(TransactionParams)
	if err := ctx.Bind(body); err != nil {
		return err
	}

	data, err := c.TransactionCommand.Execute(command.TransactionCommandParams{
		From: command.TransactionCommandAccountParams{
			Agemcy: body.From.Agency,
			Number: body.From.AccountNumber,
		},
		To: command.TransactionCommandAccountParams{
			Agemcy: body.To.Agency,
			Number: body.To.AccountNumber,
		},
		Ammount: uint64(body.Value * 100),
	})

	if err != nil {
		statusCode := http.StatusInternalServerError

		switch err {
		case command.NotFoundCarrierWithCpfError:
			statusCode = http.StatusNotFound
			break
		case command.InsuficientBalanceError:
			statusCode = http.StatusConflict
			break
		}

		return echo.NewHTTPError(statusCode, err.Error())
	}
	return ctx.JSON(http.StatusOK, adapter.TarnsactionToJSON(data))
}
