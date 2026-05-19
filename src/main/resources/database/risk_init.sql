SET NOCOUNT ON;

CREATE TABLE RISK_EVENT_LOG (
    log_id             BIGINT IDENTITY(1,1) NOT NULL,
    event_type         VARCHAR(50) NOT NULL,
    business_id        VARCHAR(64) NOT NULL,
    target_identifier  VARCHAR(100) NOT NULL,
    risk_level         VARCHAR(20) NOT NULL,
    disposition        VARCHAR(50) NOT NULL,
    trigger_reason     NVARCHAR(500) NULL,
    meta_data          NVARCHAR(MAX) NULL,
    transaction_amount DECIMAL(18,4) NULL,
    callback_url       VARCHAR(255) NULL,
    created_at         DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT pk_risk_event_log PRIMARY KEY (log_id)
);
GO

CREATE TABLE REVIEW_TASK (
    task_id                 BIGINT IDENTITY(1,1) NOT NULL,
    log_id                  BIGINT NOT NULL,
    business_id             VARCHAR(64) NOT NULL,
    scene                   VARCHAR(32) NULL,
    status                  VARCHAR(50) NULL,
    substatus               VARCHAR(50) NULL,
    review_result           VARCHAR(20) NULL,
    assignee                VARCHAR(255) NULL,
    admin_comment           NVARCHAR(500) NULL,
    attachments             NVARCHAR(MAX) NULL,
    required_documents_json NVARCHAR(MAX) NULL,
    priority                INT NOT NULL DEFAULT 0,
    create_at               DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    processed_at            DATETIME2 NULL,
    version                 BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_review_task PRIMARY KEY (task_id),
    CONSTRAINT fk_review_task_log FOREIGN KEY (log_id) REFERENCES RISK_EVENT_LOG (log_id)
);
GO

CREATE TABLE BLACK_LIST (
    id         BIGINT IDENTITY(1,1) NOT NULL,
    list_type  VARCHAR(20) NOT NULL,
    list_value VARCHAR(100) NOT NULL,
    source     VARCHAR(50) NULL,
    reason     NVARCHAR(255) NULL,
    status     BIT NOT NULL,
    expires_at DATETIME2 NULL,
    created_at DATETIME2 NULL,
    updated_at DATETIME2 NULL,
    CONSTRAINT pk_black_list PRIMARY KEY (id)
);
GO

CREATE NONCLUSTERED INDEX idx_bl_lookup ON BLACK_LIST (list_type, list_value);
GO

CREATE TABLE customer_credit_info (
    customer_id     VARCHAR(20) NOT NULL,
    annual_income   DECIMAL(15,2) NULL,
    occupation      NVARCHAR(50) NULL,
    job             NVARCHAR(100) NULL,
    external_score  INT NULL,
    other_bank_debt DECIMAL(15,2) NULL,
    has_real_estate BIT NULL,
    fund_source     VARCHAR(30) NULL,
    is_pep          BIT NOT NULL DEFAULT 0,
    final_score     INT NULL,
    risk_level      VARCHAR(10) NULL,
    last_updated_at DATETIME2 NOT NULL,
    CONSTRAINT pk_customer_credit_info PRIMARY KEY (customer_id)
);
GO
