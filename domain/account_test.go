package domain_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/stretchr/testify/assert"
)

func TestAccount(t *testing.T) {
	carrier, err := domain.CreateCarrier("862.299.976-01", "Victor Raton")
	assert.Empty(t, err)
	t.Run("create simple account", func(t *testing.T) {
		data, err := domain.CreateAccount(*carrier)
		assert.Empty(t, err)
		assert.Empty(t, data.Balance)
		assert.Equal(t, data.CPF, carrier.CPF)
		assert.Equal(t, carrier.ID, data.CarrierId)
		assert.NotEmpty(t, data.ID)
	})
	t.Run("invalid account because cpf is invalid", func(t *testing.T) {
		testCases := []string{"a simple name", "63.712.675/0001-83", "8a2.288.875-41", "86228887548", "862.288.875.41"}
		for _, tC := range testCases {
			carrier.CPF = tC
			data, err := domain.CreateAccount(*carrier)
			assert.Empty(t, data)
			assert.Equal(t, err, domain.NotValidCpfError)
		}
	})
}
