package domain

import (
	"github.com/google/uuid"
)

// Carrier is an basic structure to store CPF and User FullName
type Carrier struct {
	ID   string `json:"id"`
	CPF  string `json:"cpf"`
	Name string `json:"name"`
}

func CreateCarrier(cpf, name string) (*Carrier, error) {
	if err := ValidateCpf(cpf); err != nil {
		return nil, err
	}
	return &Carrier{
		Name: name,
		CPF:  cpf,
		ID:   uuid.NewString(),
	}, nil
}
