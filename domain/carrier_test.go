package domain_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

func TestCarrier(t *testing.T) {
	t.Run("create user with simple cpf", func(t *testing.T) {
		data, err := domain.Create("862.288.875-41", "Victor Raton")
		if err != nil {
			t.Fatalf("expected error is null, got %v", err)
		}
		if data.CPF != "862.288.875-41" || data.Name != "Victor Raton" {
			t.Fail()
		}
	})
	t.Run("invalid cep", func(t *testing.T) {
		data, err := domain.Create("862.288.875-413", "Victor Raton")
		if err != domain.NotValidCpfError {
			t.Fatalf("expected error is cpf is not valid, got %v", err)
		}
		if data != nil {
			t.Fatalf("expected data is null, got %v", data)
		}
	})
}
