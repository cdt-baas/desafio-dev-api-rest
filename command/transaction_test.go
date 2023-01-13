package command_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/stretchr/testify/assert"
)

type accountRepositoryTransactionCommandMock struct {
	Data []*domain.Account
}

func (m *accountRepositoryTransactionCommandMock) FindByAccountNumberAndAgency(number, agency uint64) (*domain.Account, error) {
	var response *domain.Account

	for _, v := range m.Data {
		if v.AccountNumber == number && v.Agency == agency {
			response = v
		}
	}

	if response == nil {

		return nil, command.NotFoundAccountWithNumberError
	}
	return response, nil

}

func (m *accountRepositoryTransactionCommandMock) UpdateBalanceTransaction(to, from *domain.Account) error {
	var updatedList []*domain.Account
	for _, oldVakue := range m.Data {

		currentItem := oldVakue
		for _, updatedItem := range []*domain.Account{to, from} {
			if oldVakue.ID == updatedItem.ID {
				currentItem = updatedItem
			}
		}

		updatedList = append(updatedList, currentItem)
	}

	m.Data = updatedList

	return nil
}

func TestTransaction(t *testing.T) {

	carriers, _ := CarruerFactory([]*CarrierFactoryArgs{
		{
			Name: "Victor Raton",
			CPF:  "862.288.875-31",
		},
		{
			Name: "Nikola Tesla",
			CPF:  "862.288.875-41",
		},
	})
	accounts, _ := AccountFacotry(carriers, uint64(2))
	accountsInOtherAgency, _ := AccountFacotry(carriers, uint64(3))
	accounts = append(accounts, accountsInOtherAgency...)

	accounts[0].Balance = uint64(200000)
	accounts[3].Balance = uint64(10000)

	repository := &accountRepositoryTransactionCommandMock{
		Data: accounts,
	}
	stub := &command.TransactionCommand{
		AccountRepository: repository,
	}
	t.Run("should be able to transaction between accounts", func(t *testing.T) {
		res, err := stub.Execute(command.TransactionCommandParams{
			From: command.TransactionCommandAccountParams{
				Agemcy: uint64(2),
				Number: uint64(1),
			},
			To: command.TransactionCommandAccountParams{
				Agemcy: uint64(3),
				Number: uint64(2),
			},
			Ammount: uint64(100000),
		})

		assert.Empty(t, err)
		assert.Equal(t, res.From.AccountNumber, accounts[0].AccountNumber)
		assert.Equal(t, res.From.Agency, accounts[0].Agency)
		assert.Equal(t, res.To.AccountNumber, accounts[3].AccountNumber)
		assert.Equal(t, res.To.Agency, accounts[3].Agency)
		assert.Equal(t, res.From.Balance, uint64(100000))
		assert.Equal(t, res.To.Balance, uint64(110000))
	})

	t.Run("error because ammount in one account is inssuficient", func(t *testing.T) {
		res, err := stub.Execute(command.TransactionCommandParams{
			From: command.TransactionCommandAccountParams{
				Agemcy: uint64(2),
				Number: uint64(2),
			},
			To: command.TransactionCommandAccountParams{
				Agemcy: uint64(3),
				Number: uint64(2),
			},
			Ammount: uint64(100000),
		})

		assert.Empty(t, res)
		assert.Equal(t, err, command.InsuficientBalanceError)
	})

	t.Run("error because from account is not found", func(t *testing.T) {
		res, err := stub.Execute(command.TransactionCommandParams{
			From: command.TransactionCommandAccountParams{
				Agemcy: uint64(4),
				Number: uint64(2),
			},
			To: command.TransactionCommandAccountParams{
				Agemcy: uint64(3),
				Number: uint64(2),
			},
			Ammount: uint64(100000),
		})

		assert.Empty(t, res)
		assert.Equal(t, err, command.NotFoundAccountWithNumberError)
	})
	t.Run("error because to account is not found", func(t *testing.T) {
		res, err := stub.Execute(command.TransactionCommandParams{
			From: command.TransactionCommandAccountParams{
				Agemcy: uint64(2),
				Number: uint64(1),
			},
			To: command.TransactionCommandAccountParams{
				Agemcy: uint64(4),
				Number: uint64(2),
			},
			Ammount: uint64(100000),
		})

		assert.Empty(t, res)
		assert.Equal(t, err, command.NotFoundAccountWithNumberError)
	})

}
