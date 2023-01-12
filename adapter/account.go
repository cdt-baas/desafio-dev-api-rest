package adapter

import "gihub.com/victorfernandesraton/dev-api-rest/domain"

type AccountJSON struct {
	ID            string  `json:"id"`
	CPF           string  `json:"cpf"`
	CarrierId     string  `json:"carrier_id"`
	Balance       float64 `json:"balance"`
	Status        uint    `json:"status"`
	Agency        uint64  `json:"agency"`
	AccountNumber uint64  `json:"account_number"`
}

func AccountToJSON(data *domain.Account) *AccountJSON {
	return &AccountJSON{
		ID:            data.ID,
		CPF:           data.CPF,
		CarrierId:     data.CarrierId,
		Balance:       BalanceToJSON(data.Balance),
		Status:        uint(data.Status),
		Agency:        data.Agency,
		AccountNumber: data.AccountNumber,
	}
}
