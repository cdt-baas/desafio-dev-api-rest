package domain

import (
	"database/sql"
	"time"
)

// Transaction is a single struct to represent cbanges in accounts
type Transaction struct {
	From      sql.NullString `json:"from,omitempty"`
	To        sql.NullString `json:"to,omitempty"`
	Value     uint64         `json:"value"`
	CreatedAt time.Time      `json:"created_at"`
}
