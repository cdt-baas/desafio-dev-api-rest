package domain_test

import (
	"fmt"
	"testing"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

func TestCpf(t *testing.T) {
	t.Run("invalid cpf", func(t *testing.T) {
		testCases := []struct {
			CEP string
			err error
		}{
			{
				CEP: "862.288.875-41",
				err: nil,
			},
			{
				CEP: "a simple name",
				err: domain.NotValidCpfError,
			},
			{
				CEP: "63.712.675/0001-83",
				err: domain.NotValidCpfError,
			},
			{
				CEP: "8a2.288.875-41",
				err: domain.NotValidCpfError,
			},
			{
				CEP: "86228887548",
				err: domain.NotValidCpfError,
			},
			{
				CEP: "862.288.875.41",
				err: domain.NotValidCpfError,
			},
		}
		for _, tC := range testCases {
			t.Run(fmt.Sprintf("test validate cpf %s", tC.CEP), func(t *testing.T) {
				err := domain.ValidateCpf(tC.CEP)
				if err != tC.err {
					t.Fatalf("expected error is cpf is not valid, got %v", err)
				}
			})
		}
	})
}
