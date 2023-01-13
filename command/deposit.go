package command

import (
	"errors"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

var NotFoundAccountWithNumberError = errors.New("not found account with this number")

type accountRepositoryInDepositCommand interface {
	FindByAccountNumberAndAgency(uint64, uint64) (*domain.Account, error)
	UpdateBalance(string, uint64) error
}

type DepositCommand struct {
	AccountRepository accountRepositoryInDepositCommand
}

func (c *DepositCommand) Execute(accountNumber, agency, amount uint64) (*domain.Account, error) {

	account, err := c.AccountRepository.FindByAccountNumberAndAgency(accountNumber, agency)
	if err != nil {
		return nil, err
	}

	if account == nil || account.Status == domain.DeactivatedAccountStatus {
		return nil, NotFoundCarrierWithCpfError
	}

	account.Balance += amount

	if err = c.AccountRepository.UpdateBalance(account.ID, account.Balance); err != nil {
		return nil, err
	}

	return account, nil
}
