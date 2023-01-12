package adapter

func BalanceToJSON(balance uint64) float64 {
	value := int(balance / 100)
	return float64(value)
}
