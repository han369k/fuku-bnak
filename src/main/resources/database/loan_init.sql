-- ============================================================
-- Loan Module Schema（僅建表，不含測試資料）
-- ============================================================

CREATE TABLE LOAN_APPLICATION (
    application_id      NVARCHAR(50)    NOT NULL PRIMARY KEY,
    customer_id         NVARCHAR(50)    NULL,
    applicant_name      NVARCHAR(50)    NULL,
    applicant_phone     NVARCHAR(20)    NULL,
    applicant_email     NVARCHAR(100)   NULL,
    apply_type          NVARCHAR(20)    NOT NULL,
    apply_amount        BIGINT          NOT NULL,
    apply_period        INT             NOT NULL,
    rate                DECIMAL(15,4)   NOT NULL,
    application_status  NVARCHAR(20)    NOT NULL,
    create_time         DATETIME2       NOT NULL,
    latest_contact_status NVARCHAR(20)  NULL,
    latest_contact_time   DATETIME2     NULL
);