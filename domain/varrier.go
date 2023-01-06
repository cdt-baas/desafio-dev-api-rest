package domain

import (
	"errors"
	"log"
	"regexp"

	"github.com/google/uuid"
)

const cpf_regex = `[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$`

var NotValidCpfError = errors.New("cpf is not valid")

// Carrier is an basic structure to store CPF and User FullName
type Carrier struct {
	ID   string `json:"id"`
	CPF  string `json:"cpf"`
	Name string `json:"name"`
}

func Create(cpf, name string) (*Carrier, error) {
	if err := validateCpf(cpf); err != nil {
		return nil, err
	}
	return &Carrier{
		Name: name,
		CPF:  cpf,
		ID:   uuid.NewString(),
	}, nil
}

func validateCpf(cpf string) error {
	res, err := regexp.Match(cpf_regex, []byte(cpf))
	if err != nil {
		return err
	}

	log.Println(res)

	if !res {
		return NotValidCpfError
	}

	return nil
}
