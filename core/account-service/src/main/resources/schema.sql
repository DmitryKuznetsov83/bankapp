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

insert into accounts.users (id, login, password_hash, name, birthdate)
VALUES
    ('68cce47b-768f-411f-a0f0-adfeb0cf41d0', 'Thelma', '$2a$10$NVDN6BBBJ7dY3B8vDQvoSulUktpG6GfnB4X4DXGggai2M4aTLsbQ.', 'Thelma', '1900-01-01'),
    ('e106e22c-aeea-4156-9c20-2a51f5c6a926', 'Louisa', '$2a$10$wlFeHKuRMZK36cDPkIVINufSCMHI/Kc5glKD3IHaEN2EmXASpcfUS', 'Louisa', '1900-01-01');