package command_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/stretchr/testify/assert"
)

type accountRepositoryInActivateCommandMock struct {
	Data []*domain.Account
}

func (m *accountRepositoryInActivateCommandMock) FindByAccountNumberAndAgency(number, agency uint64) (*domain.Account, error) {
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

func (m *accountRepositoryInActivateCommandMock) UpdateStatus(string, domain.AccountStatus) error {
	return nil
}

func TestActivateAccount(t *testing.T) {

	carrier, _ := domain.CreateCarrier("862.288.875-41", "Victor Raton")
	account, _ := domain.CreateAccount(*carrier, uint64(878))
	account.AccountNumber = uint64(1)
	accountRepository := &accountRepositoryInActivateCommandMock{
		Data: []*domain.Account{account},
	}

	stub := &command.UpdateStatusCommand{
		AccountRepository: accountRepository,
	}

	t.Run("simple deactivate account", func(t *testing.T) {
		res, err := stub.Execute(uint64(1), uint64(878), domain.DeactivatedAccountStatus)
		assert.Empty(t, err)
		assert.Equal(t, res.AccountNumber, uint64(1))
		assert.Equal(t, res.Agency, uint64(878))
		assert.Equal(t, res.Balance, uint64(0))
		assert.Equal(t, res.Status, domain.DeactivatedAccountStatus)
	})

	t.Run("simple activate account", func(t *testing.T) {
		res, err := stub.Execute(uint64(1), uint64(878), domain.CreatedAccountStatus)
		assert.Empty(t, err)
		assert.Equal(t, res.AccountNumber, uint64(1))
		assert.Equal(t, res.Agency, uint64(878))
		assert.Equal(t, res.Balance, uint64(0))
		assert.Equal(t, res.Status, domain.CreatedAccountStatus)

	})

	t.Run("error because not found account", func(t *testing.T) {
		res, err := stub.Execute(uint64(2), uint64(878), domain.CreatedAccountStatus)
		assert.Empty(t, res)
		assert.Equal(t, err, command.NotFoundAccountWithNumberError)
	})

}
