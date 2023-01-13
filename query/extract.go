package query

import (
	"context"
	"database/sql"
	"gihub.com/victorfernandesraton/dev-api-rest/adapter"
	"gihub.com/victorfernandesraton/dev-api-rest/command"
	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/jackc/pgx/v5"
	"time"
)

type extractQueryAccountRepository interface {
	FindByAccountNumberAndAgency(uint64, uint64) (*domain.Account, error)
}

type ExtractQuery struct {
	DB                *pgx.Conn
	AccountRepository extractQueryAccountRepository
}

type ExtractTransactionResponse struct {
	From      *adapter.TransactionAccountJSON `json:"from"`
	To        *adapter.TransactionAccountJSON `json:"to"`
	Value     float64                         `json:"value"`
	CreatedAt time.Time                       `json:"created_at"`
}

type ExtractResponse struct {
	Total        float64                       `json:"total"`
	Transactions []*ExtractTransactionResponse `json:"transactions"`
}

func (q *ExtractQuery) Execute(number, agency uint64, start, end time.Time) (*ExtractResponse, error) {

	var data []*ExtractTransactionResponse
	result := &ExtractResponse{
		Total: 0,
	}
	account, err := q.AccountRepository.FindByAccountNumberAndAgency(number, agency)
	if err != nil {
		return nil, err
	}

	if account == nil {
		return nil, command.NotFoundAccountWithNumberError
	}

	query := `
		select
		    af.id as af_id,
			af.agency as af_agency,
			af.account_number as af_number,
			af.cpf as af_cpf,
			at.id as at_id,
			at.agency as at_agency,
			at.account_number as at_number,
			at.cpf as at_cpf,
			t.created_at,
			t.value
		from transaction t
		left join account af on af.id = t."from"
		left join account at on at.id = t."to"
		where 
		    DATE(t.created_at) > $2 
		and DATE(t.created_at) < $3
		and (af.id = $1 or at.id = $1)
		order by t.created_at asc
		`

	rows, err := q.DB.Query(context.Background(), query, account.ID, adapter.DateToSQL(start), adapter.DateToSQL(end))
	if err != nil {
		return nil, err
	}

	for rows.Next() {
		var idFrom, cpfFrom, idTo, cpfTo sql.NullString
		var agencyFrom, numberFrom, agencyTo, numberTo, value sql.NullInt64
		var createdAt time.Time
		var from, to *adapter.TransactionAccountJSON
		err := rows.Scan(&idFrom, &agencyFrom, &numberFrom, &cpfFrom, &idTo, &agencyTo, &numberTo, &cpfTo, &createdAt, &value)
		if err != nil {
			return nil, err
		}

		valueConverter := adapter.BalanceToJSON(uint64(value.Int64))

		if idFrom.Valid {
			if idFrom.String == account.ID {
				result.Total -= valueConverter
			}
			from = adapter.TransactionFromSQL(adapter.TransactionAccountSQL{
				ID:     idFrom,
				CPF:    cpfFrom,
				Number: numberFrom,
				Agency: agencyFrom,
			})
		}
		if idTo.Valid {
			if idTo.String == account.ID {
				result.Total += valueConverter
			}
			to = adapter.TransactionFromSQL(adapter.TransactionAccountSQL{
				ID:     idTo,
				CPF:    cpfTo,
				Number: numberTo,
				Agency: agencyTo,
			})
		}

		data = append(data, &ExtractTransactionResponse{
			From:      from,
			To:        to,
			Value:     valueConverter,
			CreatedAt: createdAt,
		})
	}

	result.Transactions = data
	return result, nil
}
