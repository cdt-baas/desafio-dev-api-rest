package command_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/stretchr/testify/assert"
)

type accountRepositoryMock struct {
	data []*domain.Account
}

func (a *accountRepositoryMock) Save(data *domain.Account) error {
	a.data = append(a.data, data)
	return nil
}

func (a *accountRepositoryMock) FindByAccountNumberAndAgency(account, agency uint64) (*domain.Account, error) {
	var result *domain.Account

	for _, v := range a.data {
		if v.AccountNumber == account && v.Agency == agency {
			result = v
		}
	}

	return result, nil
}

func (a *accountRepositoryMock) GenerateIdForAgency(agency uint64) (uint64, error) {
	var result uint64

	for _, v := range a.data {
		if v.Agency == agency {
			result = v.AccountNumber + 1
		}
	}
	return result, nil
}

func TestCreatAccount(t *testing.T) {
	carrierRepository := &carrierMockRepository{}
	accountRepository := &accountRepositoryMock{
		data: []*domain.Account{},
	}
	stub := command.CreateAccountCommand{
		CarrierRepository: carrierRepository,
		AccountRepository: accountRepository,
	}

	t.Run("should create simple account", func(t *testing.T) {
		data, err := stub.Execute("862.288.875-41", uint64(254))
		assert.Nil(t, err)
		assert.NotEmpty(t, data)

		assert.Equal(t, data.AccountNumber, uint64(1))
		assert.Equal(t, data.Agency, uint64(254))
		assert.Empty(t, data.Balance)
	})
	t.Run("should create simple account in same agency increase account number", func(t *testing.T) {
		data, err := stub.Execute("862.288.875-41", uint64(254))
		assert.Nil(t, err)
		assert.NotEmpty(t, data)

		assert.Equal(t, data.AccountNumber, uint64(2))
		assert.Equal(t, data.Agency, uint64(254))
		assert.Empty(t, data.Balance)
	})
	t.Run("should create simple account in same other agency and number is one", func(t *testing.T) {
		data, err := stub.Execute("862.288.875-41", uint64(222))
		assert.Nil(t, err)
		assert.NotEmpty(t, data)

		assert.Equal(t, data.AccountNumber, uint64(1))
		assert.Equal(t, data.Agency, uint64(222))
		assert.Empty(t, data.Balance)
	})

	t.Run("should error because carrier is not exist", func(t *testing.T) {
		data, err := stub.Execute("862.288.875-31", uint64(222))
		assert.Nil(t, data)
		assert.Equal(t, err, command.NotFoundCarrierWithCpfError)
	})
	t.Run("should error because cpf is invalid", func(t *testing.T) {
		data, err := stub.Execute("862.288.87-31", uint64(222))
		assert.Nil(t, data)
		assert.Equal(t, err, domain.NotValidCpfError)
	})
}
