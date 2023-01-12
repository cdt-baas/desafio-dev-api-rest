package storage

import (
	"context"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/jackc/pgx/v5"
)

type AccountRepository struct {
	DB *pgx.Conn
}

func (r *AccountRepository) Save(account *domain.Account) error {
	_, err := r.DB.Exec(context.Background(), "insert into public.account(id, cpf, carrier_id, agency, account_number) values ($1, $2, $3, $4, $5)",
		account.ID, account.CPF, account.CarrierId, account.Agency, account.AccountNumber,
	)
	return err
}

func (r *AccountRepository) FindByAccountNumberAndAgency(account uint64, agency uint64) (*domain.Account, error) {
	var result *domain.Account
	rows, err := r.DB.Query(context.Background(), "select id, cpf, carrier_id, balance, status, agency, account_number from public.account a where a.agency = $1 and a.account_number = $2",
		agency, account)
	if err != nil {
		return nil, err
	}
	for rows.Next() {
		err := rows.Scan(&result)
		if err != nil {
			return nil, err
		}
	}

	return result, nil
}

func (r *AccountRepository) GenerateIdForAgency(agency uint64) (uint64, error) {
	var result uint64
	rows, err := r.DB.Query(context.Background(), "select COALESCE(MAX(a.account_number),0) from public.account a where a.agency = $1", agency)
	if err != nil {
		return 0, err
	}
	for rows.Next() {
		err := rows.Scan(&result)
		if err != nil {
			return 0, err
		}
	}

	return result, nil
}
