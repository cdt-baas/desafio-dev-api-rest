package domain_test

import (
	"fmt"
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/stretchr/testify/assert"
)

func TestCarrier(t *testing.T) {
	t.Run("create user with simple cpf", func(t *testing.T) {
		data, err := domain.CreateCarrier("862.288.875-41", "Victor Raton")
		assert.Nil(t, err)
		assert.Equal(t, data.CPF, "862.288.875-41")
		assert.Equal(t, data.Name, "Victor Raton")
	})
	t.Run("invalid cpf", func(t *testing.T) {
		testCases := []string{"a simple name", "63.712.675/0001-83", "8a2.288.875-41", "86228887548", "862.288.875.41"}
		for _, tC := range testCases {
			t.Run(fmt.Sprintf("test validate cpf %s", tC), func(t *testing.T) {
				data, err := domain.CreateCarrier(tC, "Victor Raton")
				assert.Equal(t, err, domain.NotValidCpfError)
				assert.Nil(t, data)
			})
		}
	})
}
