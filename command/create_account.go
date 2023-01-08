package command

import "gihub.com/victorfernandesraton/dev-api-rest/domain"

type accountRepository interface {
	Save(domain.Account) error
}

type CreateAccountCommand struct {
}
