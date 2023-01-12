package controller

import (
	"net/http"

	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/labstack/echo/v4"
)

type CarrierController struct {
	CreateCarrierCommand *command.CreateCarrierCommand
}

type CreateCarrierControllerBody struct {
	Name string `json:"name"`
	Cpf  string `json:"cpf"`
}

func (c *CarrierController) CreateCarrier(ctx echo.Context) error {
	body := new(CreateCarrierControllerBody)
	if err := ctx.Bind(body); err != nil {
		return err
	}

	result, err := c.CreateCarrierCommand.Execute(body.Cpf, body.Name)
	if err != nil {
		var statusCode int
		switch err {
		case command.HasExistCarrierWithCpfError:
			statusCode = http.StatusConflict
		case domain.NotValidCpfError:
			statusCode = http.StatusBadRequest
		default:
			statusCode = http.StatusInternalServerError
		}
		return echo.NewHTTPError(statusCode, err.Error())
	}

	return ctx.JSON(http.StatusOK, result)

}
