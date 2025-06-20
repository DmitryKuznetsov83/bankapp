-- DROP
-- DROP TABLE IF EXISTS accounts.account;
-- DROP TABLE IF EXISTS accounts.users;
-- DROP SCHEMA IF EXISTS accounts;

-- CREATE
CREATE SCHEMA IF NOT EXISTS accounts;

CREATE TABLE IF NOT EXISTS accounts.users
(
    id UUID PRIMARY KEY,
    login VARCHAR(16) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    name VARCHAR(32) NOT NULL,
    birthdate DATE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT login_unique UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS accounts.account
(
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    currency VARCHAR(3) NOT NULL,
    state VARCHAR(20) NOT NULL,
    balance NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT account_user_id_fk FOREIGN KEY (owner_id) REFERENCES accounts.users (id),
    CONSTRAINT currency_unique UNIQUE (owner_id, currency)
);