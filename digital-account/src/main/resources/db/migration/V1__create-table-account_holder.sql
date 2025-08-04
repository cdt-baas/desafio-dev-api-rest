CREATE TABLE account_holder(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    encrypted_cpf BYTEA NOT NULL,
    cpf_hash VARCHAR(64) UNIQUE NOT NULL
);

CREATE INDEX idx_account_holder_cpf_hash ON account_holder (cpf_hash);