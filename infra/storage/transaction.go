package storage

import (
	"context"
	"github.com/jackc/pgx/v5"
	"time"
)

type TransactionRepository struct {
	DB *pgx.Conn
}

func (r *TransactionRepository) ExtractToday(id string) (uint64, error) {
	result := uint64(0)
	query := `select SUM(t.value) from transaction t where t."from" = $1 and DATE(t.created_at) = $2`
	rows, err := r.DB.Query(context.Background(), query, id, time.Now())
	if err != nil {
		return result, err
	}
	for rows.Next() {

		err := rows.Scan(&result)
		if err != nil {
			return result, err
		}
	}

	return result, nil

}
