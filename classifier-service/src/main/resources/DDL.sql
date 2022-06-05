CREATE DATABASE classifier-service;

CREATE SCHEMA classifier;

CREATE TABLE IF NOT EXISTS classifier.currency
(
    id          CHARACTER(36) PRIMARY KEY,
    dt_create   TIMESTAMP              NOT NULL,
    dt_update   TIMESTAMP              NOT NULL,
    title       CHARACTER VARYING(5)   NOT NULL,
    description CHARACTER VARYING(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS classifier.category
(
    id        CHARACTER(36) PRIMARY KEY,
    dt_create TIMESTAMP         NOT NULL,
    dt_update TIMESTAMP         NOT NULL,
    title     CHARACTER VARYING NOT NULL
);

DROP TABLE classifier.currency;

DROP TABLE classifier.category;

DROP SCHEMA classifier;

DROP DATABASE "classifier-service";