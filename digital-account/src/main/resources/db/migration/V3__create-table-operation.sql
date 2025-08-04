CREATE TABLE operation (
    id SERIAL PRIMARY KEY,
    date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount NUMERIC(15, 2) NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    account_number VARCHAR(20) NOT NULL,
    FOREIGN KEY (account_number) REFERENCES account(number)
);