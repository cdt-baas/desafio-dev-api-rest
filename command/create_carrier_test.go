package command_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/stretchr/testify/assert"
)

type carrierMockRepository struct {
}

func (r *carrierMockRepository) FindByCPF(cpf string) (*domain.Carrier, error) {
	if cpf != "862.288.875-31" {
		return domain.CreateCarrier(cpf, "Test Name")
	}
	return nil, nil
}

func (r *carrierMockRepository) Save(data *domain.Carrier) error {
	return nil
}
func TestCreateCarrierCommand(t *testing.T) {
	repository := &carrierMockRepository{}
	stub := command.CreateCarrierCommand{
		CaryRepository: repository,
	}
	t.Run("create simple carrier", func(t *testing.T) {
		data, err := stub.Execute("862.288.875-31", "Victor Raton")
		assert.Empty(t, err)
		assert.Equal(t, data.CPF, "862.288.875-31")
		assert.NotEmpty(t, data.ID)
	})

	t.Run("error creating carrier with invalid cpf", func(t *testing.T) {
		data, err := stub.Execute("862.288.87531", "Victor Raton")
		assert.Empty(t, data)
		assert.Equal(t, err, domain.NotValidCpfError)
	})

	t.Run("error creating carrier with duplicate cpf", func(t *testing.T) {
		data, err := stub.Execute("862.288.875-41", "Victor Raton")
		assert.Empty(t, data)
		assert.Equal(t, err, command.HasExistCarrierWithCpfError)
	})
}
