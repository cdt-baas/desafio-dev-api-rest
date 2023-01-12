package command

import "gihub.com/victorfernandesraton/dev-api-rest/domain"

type accountRepositoryTransactionCommand interface {
	FindByAccountNumberAndAgency(uint64, uint64) (*domain.Account, error)
	UpdateBalanceTransaction(to, from *domain.Account) error
}

type TransactionCommand struct {
	AccountRepository accountRepositoryTransactionCommand
}

type TransactionCommandAccountParams struct {
	Agemcy uint64
	Number uint64
}

type TransactionCommandParams struct {
	From    TransactionCommandAccountParams
	To      TransactionCommandAccountParams
	Ammount uint64
}

type TransactionCommandResult struct {
	From    *domain.Account
	To      *domain.Account
	Ammount uint64
}

func (c *TransactionCommand) Execute(params TransactionCommandParams) (*TransactionCommandResult, error) {
	accountFrom, err := c.AccountRepository.FindByAccountNumberAndAgency(params.From.Number, params.From.Agemcy)
	if err != nil {
		return nil, err
	}

	accountTo, err := c.AccountRepository.FindByAccountNumberAndAgency(params.To.Number, params.To.Agemcy)
	if err != nil {
		return nil, err
	}

	if accountTo == nil || accountTo.Status == domain.DeactivatedAccountStatus || accountFrom == nil || accountFrom.Status == domain.DeactivatedAccountStatus {
		return nil, NotFoundCarrierWithCpfError
	}

	if accountFrom.Balance < params.Ammount {
		return nil, InsuficientBalanceError
	}

	accountFrom.Balance = accountFrom.Balance - params.Ammount
	accountTo.Balance = accountTo.Balance + params.Ammount

	if err := c.AccountRepository.UpdateBalanceTransaction(accountTo, accountFrom); err != nil {
		return nil, err
	}

	return &TransactionCommandResult{
		From:    accountFrom,
		To:      accountTo,
		Ammount: params.Ammount,
	}, nil

}
