DROP TABLE IF EXISTS exchange.currency_rate;
DROP SCHEMA IF EXISTS exchange;

-- CREATE
CREATE SCHEMA IF NOT EXISTS exchange;

CREATE TABLE IF NOT EXISTS exchange.currency_rate
(
    id UUID PRIMARY KEY,
    timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    currency VARCHAR(3) NOT NULL,
    rate NUMERIC(15, 2) NOT NULL,
    CONSTRAINT rate_unique UNIQUE (timestamp, currency)
);