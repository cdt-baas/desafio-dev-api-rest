package adapter

import "gihub.com/victorfernandesraton/dev-api-rest/command"

type TramsactionJSON struct {
	From  *AccountJSON `json:"from"`
	To    *AccountJSON `json:"to"`
	Value float64      `json:"value"`
}

func TarnsactionToJSON(data *command.TransactionCommandResult) *TramsactionJSON {
	return &TramsactionJSON{
		From:  AccountToJSON(data.From),
		To:    AccountToJSON(data.To),
		Value: BalanceToJSON(data.Ammount),
	}
}
