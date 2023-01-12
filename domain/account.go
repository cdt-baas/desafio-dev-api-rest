package domain

import "github.com/google/uuid"

// Account represents an bank account, using int64 for balance because using integer and convert unot for basis avoid gfloat losing
type Account struct {
	ID            string        `json:"id"`
	CPF           string        `json:"cpf"`
	CarrierId     string        `json:"carrier_id"`
	Balance       uint64        `json:"balance"`
	Status        AccountStatus `json:"status"`
	Agency        uint64        `json:"agency"`
	AccountNumber uint64        `json:"account_number"`
}

type AccountStatus uint

const (
	DeactivatedAccountStatus               = 0
	CreatedAccountStatus     AccountStatus = 1
)

func (AccountStatus) FromUint(data uint) AccountStatus {
	switch data {
	case 0:
		return DeactivatedAccountStatus
	default:
		return CreatedAccountStatus
	}
}

func CreateAccount(carrier Carrier, agency uint64) (*Account, error) {
	if err := ValidateCpf(carrier.CPF); err != nil {
		return nil, err
	}

	return &Account{
		ID:        uuid.NewString(),
		CPF:       carrier.CPF,
		CarrierId: carrier.ID,
		Status:    CreatedAccountStatus,
		Agency:    agency,
	}, nil
}
