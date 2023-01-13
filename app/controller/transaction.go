package controller

import (
	"gihub.com/victorfernandesraton/dev-api-rest/adapter"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/query"
	"github.com/labstack/echo/v4"
	"net/http"
)

type TransactionController struct {
	TransactionCommand *command.TransactionCommand
	ExtractQuery       *query.ExtractQuery
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

type ExtractQueryParams struct {
	AccountInPathParams
	StartAt string `query:"start_at"`
	EndAt   string `query:"end_at"`
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
	return ctx.JSON(http.StatusOK, adapter.TransactionToJSON(data))
}

func (c *TransactionController) Extract(ctx echo.Context) error {
	body := new(ExtractQueryParams)
	if err := ctx.Bind(body); err != nil {
		return err
	}

	startDate, err := adapter.StringToDate(body.StartAt)
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())

	}
	endDate, err := adapter.StringToDate(body.EndAt)
	if err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	result, err := c.ExtractQuery.Execute(body.AccountNumber, body.Agency, startDate, endDate)
	if err != nil {
		statusCode := http.StatusInternalServerError

		switch err {
		case command.NotFoundCarrierWithCpfError:
			statusCode = http.StatusNotFound
			break

		}

		return echo.NewHTTPError(statusCode, err.Error())
	}

	return ctx.JSON(http.StatusOK, result)
}
