package command

import (
	"errors"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

var NotFoundCarrierWithCpfError = errors.New("not found carrier with this cpf")
var DuplicatedAccountAndAgencyError = errors.New("duplicated account number")

type accountRepository interface {
	Save(*domain.Account) error
	FindByAccountNumberAndAgency(uint64, uint64) (*domain.Account, error)
	GenerateIdForAgency(uint64) (uint64, error)
}

type carrierRepository interface {
	FindByCPF(string) (*domain.Carrier, error)
}

type CreateAccountCommand struct {
	AccountRepository accountRepository
	CarrierRepository carrierRepository
}

func (c *CreateAccountCommand) Execute(cpf string, agency uint64) (*domain.Account, error) {
	carrier, err := c.CarrierRepository.FindByCPF(cpf)
	if err != nil {
		return nil, err
	}
	if carrier == nil {
		return nil, NotFoundCarrierWithCpfError
	}

	account, err := domain.CreateAccount(*carrier, agency)
	if err != nil {
		return nil, err
	}

	accountId, err := c.AccountRepository.GenerateIdForAgency(agency)
	if err != nil {
		return nil, err
	}

	account.AccountNumber = accountId

	finRepeatAccount, err := c.AccountRepository.FindByAccountNumberAndAgency(account.AccountNumber, account.Agency)
	if err != nil {
		return nil, err
	}

	if finRepeatAccount != nil {
		return nil, DuplicatedAccountAndAgencyError
	}

	if err := c.AccountRepository.Save(account); err != nil {
		return nil, err
	}

	return account, nil
}
