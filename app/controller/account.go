package controller

import (
	"database/sql"
	"errors"
	"gihub.com/victorfernandesraton/dev-api-rest/adapter"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"gihub.com/victorfernandesraton/dev-api-rest/infra/event/emitter"
	"github.com/labstack/echo/v4"
	"log"
	"net/http"
	"time"
)

var NotValidNegativeDepositError = errors.New("not valid negative value for deposit")

type AccountController struct {
	CreateAccountCommand *command.CreateAccountCommand
	DepositCommand       *command.DepositCommand
	WithdrawalCommand    *command.WithdrawalCommand
	UpdateStatusCommand  *command.UpdateStatusCommand
	TransactionEmitter   *emitter.TransactionEmitter
}

type CreateAccountBodyParams struct {
	CPF    string `json:"cpf"`
	Agency uint64 `json:"agency"`
}

type AccountInPathParams struct {
	AccountNumber uint64 `param:"account"`
	Agency        uint64 `param:"agency""`
}

type DepositParams struct {
	AccountInPathParams
	Value float64 `json:"value"`
}

type UpdateStatusParams struct {
	AccountInPathParams
	Status uint `json:"status"`
}

type AccountResponse struct {
	ID            string  `json:"id"`
	CPF           string  `json:"cpf"`
	CarrierId     string  `json:"carrier_id"`
	Balance       float64 `json:"balance"`
	Status        uint    `json:"status"`
	Agency        uint64  `json:"agency"`
	AccountNumber uint64  `json:"account_number"`
}

func (c *AccountController) CreateAccount(ctx echo.Context) error {
	body := new(CreateAccountBodyParams)
	if err := ctx.Bind(body); err != nil {
		return err
	}

	res, err := c.CreateAccountCommand.Execute(body.CPF, body.Agency)
	if err != nil {
		statusCode := http.StatusInternalServerError
		switch err {
		case domain.NotValidCpfError:
			statusCode = http.StatusBadRequest
			break
		case command.NotFoundCarrierWithCpfError:
			statusCode = http.StatusNotFound
			break
		case command.DuplicatedAccountAndAgencyError:
			statusCode = http.StatusConflict
			break
		default:
			statusCode = http.StatusInternalServerError
		}
		return echo.NewHTTPError(statusCode, err.Error())
	}
	return ctx.JSON(http.StatusCreated, res)
}

func (c *AccountController) Deposit(ctx echo.Context) error {
	body := new(DepositParams)
	if err := ctx.Bind(body); err != nil {
		return err
	}

	if body.Value < 0 {
		return echo.NewHTTPError(http.StatusBadRequest, NotValidNegativeDepositError)
	}

	value := uint64(body.Value * 100)

	data, err := c.DepositCommand.Execute(body.AccountNumber, body.Agency, value)
	if err != nil {
		statusCode := http.StatusInternalServerError
		if err == command.NotFoundAccountWithNumberError {
			statusCode = http.StatusNotFound
		}
		return echo.NewHTTPError(statusCode, err.Error())
	}

	res := adapter.AccountToJSON(data)

	if err := c.TransactionEmitter.Execute(&domain.Transaction{
		From: sql.NullString{
			Valid: false,
		},
		To:        adapter.TransactionAccountToEvent(data),
		Value:     value,
		CreatedAt: time.Now(),
	}); err != nil {
		log.Println(err)
	}

	return ctx.JSON(http.StatusOK, res)

}

func (c *AccountController) Withdrawal(ctx echo.Context) error {
	body := new(DepositParams)
	if err := ctx.Bind(body); err != nil {
		return err
	}

	if body.Value < 0 {
		return echo.NewHTTPError(http.StatusBadRequest, NotValidNegativeDepositError)
	}

	value := uint64(body.Value * 100)

	data, err := c.WithdrawalCommand.Execute(body.AccountNumber, body.Agency, value)
	if err != nil {
		statusCode := http.StatusInternalServerError
		switch err {
		case command.NotFoundAccountWithNumberError:
			statusCode = http.StatusNotFound
			break
		case command.InsuficientBalanceError:
			statusCode = http.StatusBadRequest
		default:
			break
		}
		return echo.NewHTTPError(statusCode, err.Error())
	}

	res := adapter.AccountToJSON(data)

	if err := c.TransactionEmitter.Execute(&domain.Transaction{
		To: sql.NullString{
			Valid: false,
		},
		From:      adapter.TransactionAccountToEvent(data),
		Value:     value,
		CreatedAt: time.Now(),
	}); err != nil {
		log.Println(err)
	}
	return ctx.JSON(http.StatusOK, res)

}

func (c *AccountController) UpdateStatus(ctx echo.Context) error {
	body := new(UpdateStatusParams)
	if err := ctx.Bind(body); err != nil {
		return err
	}

	data, err := c.UpdateStatusCommand.Execute(body.AccountNumber, body.Agency, domain.AccountStatus(body.Status))
	if err != nil {
		statusCode := http.StatusInternalServerError
		switch err {
		case command.NotFoundAccountWithNumberError:
			statusCode = http.StatusNotFound
			break
		default:
			break
		}
		return echo.NewHTTPError(statusCode, err.Error())
	}

	res := adapter.AccountToJSON(data)
	return ctx.JSON(http.StatusOK, res)

}
