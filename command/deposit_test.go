package command_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/stretchr/testify/assert"
)

type accountRepositoryInDepositCommandMock struct {
	Data []*domain.Account
}

func (m *accountRepositoryInDepositCommandMock) FindByAccountNumberAndAgency(number, agency uint64) (*domain.Account, error) {
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

func (m *accountRepositoryInDepositCommandMock) UpdateBalance(id string, balance uint64) error {
	return nil
}

func TestDepositAccount(t *testing.T) {

	carrier, _ := domain.CreateCarrier("862.288.875-41", "Victor Raton")
	account, _ := domain.CreateAccount(*carrier, uint64(878))
	account.AccountNumber = uint64(1)
	accountRepository := &accountRepositoryInDepositCommandMock{
		Data: []*domain.Account{account},
	}

	stub := &command.DepositCommand{
		AccountRepository: accountRepository,
	}

	t.Run("simple deposit in account 200 cents", func(t *testing.T) {
		res, err := stub.Execute(uint64(1), uint64(878), uint64(200))
		assert.Empty(t, err)
		assert.Equal(t, res.AccountNumber, uint64(1))
		assert.Equal(t, res.Agency, uint64(878))
		assert.Equal(t, res.Balance, uint64(200))
	})

	t.Run("error because not found account", func(t *testing.T) {
		res, err := stub.Execute(uint64(2), uint64(878), uint64(200))
		assert.Empty(t, res)
		assert.Equal(t, err, command.NotFoundAccountWithNumberError)
	})

}
