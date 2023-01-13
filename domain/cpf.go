package domain

import (
	"errors"
	"regexp"
)

const cpf_regex = `[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}$`

var NotValidCpfError = errors.New("cpf is not valid")

func ValidateCpf(cpf string) error {
	res, err := regexp.Match(cpf_regex, []byte(cpf))
	if err != nil {
		return err
	}

	if !res {
		return NotValidCpfError
	}

	return nil
}
