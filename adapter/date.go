package adapter

import "time"

const YYYYMMDD = "2006-01-02"

func DateToSQL(value time.Time) string {
	return value.Format(YYYYMMDD)
}

func StringToDate(value string) (time.Time, error) {
	return time.Parse(YYYYMMDD, value)
}
