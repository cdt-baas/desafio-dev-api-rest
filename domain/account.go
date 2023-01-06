package domain

import "github.com/google/uuid"

// Account represents an bank account, using int64 for balance because using integer and convert unot for basis avoid gfloat losing
type Account struct {
	ID        string `json:"id"`
	CPF       string `json:"cpf"`
	CarrierId string `json:"carrier_id"`
	Balance   int64  `json:"balance"`
}

func CreateAccount(carrier Carrier) (*Account, error) {
	if err := ValidateCpf(carrier.CPF); err != nil {
		return nil, err
	}

	return &Account{
		ID:        uuid.NewString(),
		CPF:       carrier.CPF,
		CarrierId: carrier.ID,
	}, nil
}
