package adapter

import (
	"database/sql"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
)

type TransactionJSON struct {
	From  *AccountJSON `json:"from"`
	To    *AccountJSON `json:"to"`
	Value float64      `json:"value"`
}

type TransactionAccountSQL struct {
	ID     sql.NullString
	CPF    sql.NullString
	Number sql.NullInt64
	Agency sql.NullInt64
}

type TransactionAccountJSON struct {
	ID     string `json:"id,omitempty",`
	Number uint64 `json:"account_number,omitempty"`
	Agency uint64 `json:"agency,omitempty"`
	CPF    string `json:"cpf,omitempty"`
}

func TransactionToJSON(data *command.TransactionCommandResult) *TransactionJSON {
	return &TransactionJSON{
		From:  AccountToJSON(data.From),
		To:    AccountToJSON(data.To),
		Value: BalanceToJSON(data.Ammount),
	}
}
func TransactionFromSQL(data TransactionAccountSQL) *TransactionAccountJSON {
	if !data.ID.Valid {
		return nil
	}
	return &TransactionAccountJSON{
		ID:     data.ID.String,
		CPF:    data.CPF.String,
		Number: uint64(data.Number.Int64),
		Agency: uint64(data.Agency.Int64),
	}
}

func TransactionAccountToEvent(data *domain.Account) sql.NullString {
	value := sql.NullString{}
	if data != nil {
		value.Valid = true
		value.String = data.ID
	}
	return value
}
