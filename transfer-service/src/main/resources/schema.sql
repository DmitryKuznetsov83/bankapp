-- DROP
-- DROP TABLE IF EXISTS transfer.transfer_transaction;
-- DROP SCHEMA IF EXISTS transfer;

-- CREATE
CREATE SCHEMA IF NOT EXISTS transfer;

CREATE TABLE IF NOT EXISTS transfer.transfer_transaction
(
    id UUID PRIMARY KEY,
    status VARCHAR(7) NOT NULL,
    comment VARCHAR(200),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    from_login VARCHAR(16) NOT NULL,
    to_login VARCHAR(16) NOT NULL,
    from_currency VARCHAR(3) NOT NULL,
    to_currency VARCHAR(3) NOT NULL,
    from_sum NUMERIC(15, 2) NOT NULL,
    to_sum NUMERIC(15, 2) NOT NULL
);