CREATE DATABASE account-service;

CREATE SCHEMA finance_app;

CREATE TABLE IF NOT EXISTS finance_app.account
(
    id              CHARACTER(36) PRIMARY KEY,
    dt_create       TIMESTAMP(3),
    dt_update       TIMESTAMP(3),
    title           CHARACTER VARYING(128),
    description     CHARACTER VARYING(128),
    account_type_id CHARACTER VARYING REFERENCES finance_app.account_type (id),
    currency_id     CHARACTER(36)
);

DROP TABLE finance_app.account;

CREATE TABLE IF NOT EXISTS finance_app.account_type
(
    id          CHARACTER VARYING(128) PRIMARY KEY,
    description CHARACTER VARYING(128) NOT NULL
);

DROP TABLE finance_app.account_type;

CREATE TABLE IF NOT EXISTS finance_app.operation
(
    id                    CHARACTER(36) PRIMARY KEY,
    account_id            CHARACTER(36) REFERENCES finance_app.account (id),
    dt_create             TIMESTAMP(3)           NOT NULL,
    dt_update             TIMESTAMP(3)           NOT NULL,
    dt_execute            TIMESTAMP(3),
    description           CHARACTER VARYING(128) NOT NULL,
    operation_category_id CHARACTER(36),
    operation_value       DECIMAL(19, 2)         NOT NULL DEFAULT 0,
    currency_id           CHARACTER(36)
);

DROP TABLE finance_app.operation;

CREATE TABLE finance_app.balance
(
    id CHARACTER(36) PRIMARY KEY,
    balance    DECIMAL(19, 2),
    CONSTRAINT balance_id_fk FOREIGN KEY (id) REFERENCES finance_app.account (id)
);

DROP TABLE finance_app.balance;

CREATE OR REPLACE FUNCTION finance_app.count_balance_function()
    RETURNS TRIGGER AS
$account_balance_update_trigger$
BEGIN
    UPDATE finance_app.balance b
    SET balance = (SELECT sum(op.operation_value)
                   FROM finance_app.operation op
                   WHERE op.account_id = new.account_id)
    WHERE b.id = new.account_id;
    return null;
END;
$account_balance_update_trigger$ LANGUAGE plpgsql;

CREATE TRIGGER account_balance_update_trigger
    AFTER UPDATE OR INSERT OR DELETE
    ON finance_app.operation
    FOR EACH ROW
EXECUTE FUNCTION finance_app.count_balance_function();

DROP TRIGGER account_balance_update_trigger ON finance_app.operation;
