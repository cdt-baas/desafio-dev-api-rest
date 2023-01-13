package command

import (
	"errors"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

var InsuficientBalanceError = errors.New("insuficient balance to be withdrawal")
var LimitWithdrawalInDayError = errors.New("limit for withdrawal today")

type accountRepositoryInWithdrawalCommand interface {
	FindByAccountNumberAndAgency(uint64, uint64) (*domain.Account, error)
	UpdateBalance(string, uint64) error
}

type transactionRepositoryInWithdrawalCommand interface {
	ExtractToday(string) (uint64, error)
}

type WithdrawalCommand struct {
	AccountRepository     accountRepositoryInWithdrawalCommand
	TransactionRepository transactionRepositoryInWithdrawalCommand
}

func (c *WithdrawalCommand) Execute(accountNumber, agency, ammount uint64) (*domain.Account, error) {
	account, err := c.AccountRepository.FindByAccountNumberAndAgency(accountNumber, agency)
	if err != nil {
		return nil, err
	}

	if account == nil || account.Status == domain.DeactivatedAccountStatus {
		return nil, NotFoundCarrierWithCpfError
	}

	extToday, err := c.TransactionRepository.ExtractToday(account.ID)

	if err != nil {
		return nil, err
	}

	if extToday+ammount > uint64(200000) {
		return nil, LimitWithdrawalInDayError
	}

	if account.Balance < ammount {
		return nil, InsuficientBalanceError
	}

	account.Balance = account.Balance - ammount

	if err = c.AccountRepository.UpdateBalance(account.ID, account.Balance); err != nil {
		return nil, err
	}

	return account, nil
}
