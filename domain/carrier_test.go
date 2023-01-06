package domain_test

import (
	"fmt"
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

func TestCarrier(t *testing.T) {
	t.Run("create user with simple cpf", func(t *testing.T) {
		data, err := domain.CreateCarrier("862.288.875-41", "Victor Raton")
		if err != nil {
			t.Fatalf("expected error is null, got %v", err)
		}
		if data.CPF != "862.288.875-41" || data.Name != "Victor Raton" {
			t.Fail()
		}
	})
	t.Run("invalid cpf", func(t *testing.T) {
		testCases := []string{"a simple name", "63.712.675/0001-83", "8a2.288.875-41", "86228887548", "862.288.875.41"}
		for _, tC := range testCases {
			t.Run(fmt.Sprintf("test validate cpf %s", tC), func(t *testing.T) {
				data, err := domain.CreateCarrier(tC, "Victor Raton")
				if err != domain.NotValidCpfError {
					t.Fatalf("expected error is cpf is not valid, got %v", err)
				}
				if data != nil {
					t.Fatalf("expected data is null, got %v", data)
				}
			})
		}
	})
}
