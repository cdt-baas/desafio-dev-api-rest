package domain

// Transaction is a single struct to represent cbanges in accounts
type Transaction struct {
	OriginId  string `json:"origin_id"`
	DestinyId string `json:"destiny_id"`
	Value     int64  `json:"value"`
}
