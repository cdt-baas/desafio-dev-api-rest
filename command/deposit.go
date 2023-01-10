package command

import (
	"errors"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

var NotFoundAccountWithNumberError = errors.New("not found account with this number")

type accountRepositoryInDepositCommand interface {
	FindByAccountNumberAndAgency(uint64, uint64) (*domain.Account, error)
	Update(*domain.Account) error
}

type DepositCommand struct {
	AccountRepository accountRepositoryInDepositCommand
}

func (c *DepositCommand) Execute(accountNumber, agency, ammount uint64) (*domain.Account, error) {
	account, err := c.AccountRepository.FindByAccountNumberAndAgency(accountNumber, agency)
	if err != nil {
		return nil, err
	}

	account.Balance += ammount

	if err = c.AccountRepository.Update(account); err != nil {
		return nil, err
	}

	return account, nil
}
