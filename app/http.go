package app

import (
	"gihub.com/victorfernandesraton/dev-api-rest/app/controller"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
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
	}

	group.POST("", ctr.CreateAccount)
	group.PUT("/:account/:agency/deposit", ctr.Deposit)
	group.PUT("/:account/:agency/withdrawal", ctr.Withdrawal)

}
