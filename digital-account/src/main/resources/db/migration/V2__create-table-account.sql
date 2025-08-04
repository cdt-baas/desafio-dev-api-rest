CREATE TABLE account(
    id SERIAL PRIMARY KEY,
    balance NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    number VARCHAR(20) UNIQUE NOT NULL,
    agency VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    account_holder_id INTEGER NOT NULL,
    FOREIGN KEY (account_holder_id) REFERENCES account_holder(id)
);