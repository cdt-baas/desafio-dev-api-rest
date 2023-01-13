package storage

import (
	"context"

	"gihub.com/victorfernandesraton/dev-api-rest/domain"
	"github.com/jackc/pgx/v5"
)

type CarrierRepository struct {
	DB *pgx.Conn
}

func (r *CarrierRepository) Save(carrier *domain.Carrier) error {
	_, err := r.DB.Exec(context.Background(), "insert into public.carrier(id, name, cpf) values ($1, $2, $3)",
		carrier.ID, carrier.Name, carrier.CPF,
	)
	return err
}

func (r *CarrierRepository) FindByCPF(cpf string) (*domain.Carrier, error) {
	var result *domain.Carrier
	rows, err := r.DB.Query(context.Background(), "select id, name, cpf from public.carrier where cpf = $1 limit 1", cpf)
	if err != nil {
		return nil, err
	}
	for rows.Next() {
		var id, name, cpf string
		err := rows.Scan(&id, &name, &cpf)
		if err != nil {
			return nil, err
		}

		result = &domain.Carrier{
			ID:   id,
			CPF:  cpf,
			Name: name,
		}
	}

	return result, nil
}
