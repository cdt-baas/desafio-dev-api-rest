package controller

import (
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"github.com/labstack/echo/v4"
	"net/http"
)

type AccountController struct {
	CreateAccountCommand *command.CreateAccountCommand
}

type CreateAccountBodyParams struct {
	CPF    string `json:"cpf"`
	Agency uint64 `json:"agency"`
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
