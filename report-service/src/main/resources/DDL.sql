CREATE SCHEMA report;

CREATE TABLE report.report
(
    id     CHARACTER(36) PRIMARY KEY,
    dt_create TIMESTAMP,
    dt_update TIMESTAMP,
    status VARCHAR(128),
    description VARCHAR,
    report_type VARCHAR(128),
    report bytea
);

CREATE TABLE report.report_info
(
    id CHARACTER(36) PRIMARY KEY,
    account_id CHARACTER(36),
    report_id CHARACTER(36),
    FOREIGN KEY (report_id) REFERENCES report.report (id)
);

DROP TABLE report.report;
DROP TABLE report.report_info;
