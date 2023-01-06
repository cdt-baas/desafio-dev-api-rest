package domain_test

import (
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

func TestAccount(t *testing.T) {
	carrier, err := domain.CreateCarrier("862.299.976-01", "Victor Raton")
	if err != nil {
		t.Fatalf("expected error is null, got %v", err)
	}
	t.Run("create simple account", func(t *testing.T) {
		data, err := domain.CreateAccount(*carrier)
		if err != nil {
			t.Fatalf("expected error is null, got %v", err)
		}
		if data.Balance != 0 || data.CPF != carrier.CPF || carrier.ID != data.CarrierId {
			t.Fatal("invalid account")
		}
	})
	t.Run("invalid account because cpf is invalid", func(t *testing.T) {
		testCases := []string{"a simple name", "63.712.675/0001-83", "8a2.288.875-41", "86228887548", "862.288.875.41"}
		for _, tC := range testCases {
			carrier.CPF = tC
			data, err := domain.CreateAccount(*carrier)
			if data != nil {
				t.Fatalf("expected account is null, got %v", data)
			}
			if err != domain.NotValidCpfError {
				t.Fatalf("expected error is %v, got %v", domain.NotValidCpfError, err)
			}
		}
	})
}
