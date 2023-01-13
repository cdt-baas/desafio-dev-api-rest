package command

import (
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

type accountRepositoryInActivateCommand interface {
	FindByAccountNumberAndAgency(uint64, uint64) (*domain.Account, error)
	UpdateStatus(string, domain.AccountStatus) error
}

type UpdateStatusCommand struct {
	AccountRepository accountRepositoryInActivateCommand
}

func (c *UpdateStatusCommand) Execute(accountNumber, agency uint64, status domain.AccountStatus) (*domain.Account, error) {
	account, err := c.AccountRepository.FindByAccountNumberAndAgency(accountNumber, agency)
	if err != nil {
		return nil, err
	}

	if account == nil {
		return nil, NotFoundCarrierWithCpfError
	}
	switch status {
	case 0:
		account.Status = domain.DeactivatedAccountStatus
		break
	default:
		account.Status = domain.CreatedAccountStatus
	}

	if err = c.AccountRepository.UpdateStatus(account.ID, account.Status); err != nil {
		return nil, err
	}

	return account, nil
}
