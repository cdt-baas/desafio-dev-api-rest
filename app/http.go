package app

import (
	"gihub.com/victorfernandesraton/dev-api-rest/app/controller"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/infra/event/emitter"
	"gihub.com/victorfernandesraton/dev-api-rest/query"
	"github.com/labstack/echo/v4"
)

type DefaultControllerFactory struct {
	Echo *echo.Echo
}

type CarrierControllerFactoryParams struct {
	DefaultControllerFactory
	CreateCarrierCommand *command.CreateCarrierCommand
}

type AccountControllerFactoryParams struct {
	DefaultControllerFactory
	CreateAccountCommand  *command.CreateAccountCommand
	DepositAccountCommand *command.DepositCommand
	WithdrawalCommand     *command.WithdrawalCommand
	UpdateStatusCommand   *command.UpdateStatusCommand
	TransactionEmitter    *emitter.TransactionEmitter
}

type TransactionControllerFactoryParams struct {
	DefaultControllerFactory
	TransactionCommand *command.TransactionCommand
	ExtractQuery       *query.ExtractQuery
	TransactionEmitter *emitter.TransactionEmitter
}

func CarrierControllerFactory(params *CarrierControllerFactoryParams) {
	group := params.Echo.Group("/carrier")
	ctr := &controller.CarrierController{
		CreateCarrierCommand: params.CreateCarrierCommand,
	}

	group.POST("", func(c echo.Context) error {
		return ctr.CreateCarrier(c)
	})
}

func AccountControllerFactory(params *AccountControllerFactoryParams) {
	group := params.Echo.Group("/account")
	ctr := &controller.AccountController{
		CreateAccountCommand: params.CreateAccountCommand,
		DepositCommand:       params.DepositAccountCommand,
		WithdrawalCommand:    params.WithdrawalCommand,
		UpdateStatusCommand:  params.UpdateStatusCommand,
		TransactionEmitter:   params.TransactionEmitter,
	}

	group.POST("", ctr.CreateAccount)
	group.PUT("/:account/:agency", ctr.UpdateStatus)
	group.PUT("/:account/:agency/deposit", ctr.Deposit)
	group.PUT("/:account/:agency/withdrawal", ctr.Withdrawal)
}

func TransactionControllerFactory(params *TransactionControllerFactoryParams) {
	group := params.Echo.Group("/transaction")
	ctr := &controller.TransactionController{
		TransactionCommand: params.TransactionCommand,
		ExtractQuery:       params.ExtractQuery,
		TransactionEmitter: params.TransactionEmitter,
	}

	group.POST("", ctr.CreateTransaction)
	group.GET("/:account/:agency/extract", ctr.Extract)
}
