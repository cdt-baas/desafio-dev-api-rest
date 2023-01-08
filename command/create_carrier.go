package command

import (
	"errors"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

var HasExistCarrierWithCpfError = errors.New("carrier with this cpf alredy exist")

type CarrierRepository interface {
	Save(*domain.Carrier) error
	FindByCPF(string) (*domain.Carrier, error)
}

type CreateCarrierCommand struct {
	CaryRepository CarrierRepository
}

func (c *CreateCarrierCommand) Execute(cpf, name string) (*domain.Carrier, error) {
	existCarrier, err := c.CaryRepository.FindByCPF(cpf)
	if err != nil {
		return nil, err
	}

	if existCarrier != nil {
		return nil, HasExistCarrierWithCpfError
	}

	data, err := domain.CreateCarrier(cpf, name)
	if err != nil {
		return nil, err
	}

	if err := c.CaryRepository.Save(data); err != nil {
		return nil, err
	}

	return data, nil
}
