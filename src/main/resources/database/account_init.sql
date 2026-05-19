IF OBJECT_ID('SCHEDULED_TRANSFER', 'U') IS NOT NULL DROP TABLE SCHEDULED_TRANSFER;
IF OBJECT_ID('FAVORITE_ACCOUNT', 'U') IS NOT NULL DROP TABLE FAVORITE_ACCOUNT;
IF OBJECT_ID('TRANS_LOG', 'U') IS NOT NULL DROP TABLE TRANS_LOG;
IF OBJECT_ID('ACCOUNT_DAILY_SNAPSHOTS', 'U') IS NOT NULL DROP TABLE ACCOUNT_DAILY_SNAPSHOTS;
IF OBJECT_ID('ACCOUNT_STATUS_HISTORY', 'U') IS NOT NULL DROP TABLE ACCOUNT_STATUS_HISTORY;
IF OBJECT_ID('ACCOUNT', 'U') IS NOT NULL DROP TABLE ACCOUNT;
IF OBJECT_ID('ACCOUNT_APPLICATION', 'U') IS NOT NULL DROP TABLE ACCOUNT_APPLICATION;
GO

CREATE TABLE ACCOUNT_APPLICATION (
    id                     BIGINT IDENTITY(10001,1) PRIMARY KEY,
    application_no         VARCHAR(30)    NOT NULL UNIQUE,
    customer_id            VARCHAR(20)    NOT NULL,
    account_type           VARCHAR(20)    NOT NULL,
    currency               VARCHAR(3),
    customer_name          NVARCHAR(50)   NOT NULL,
    id_number              VARCHAR(20)    NOT NULL,
    birthday               DATE           NOT NULL,
    gender                 CHAR(1)        NOT NULL,
    email                  VARCHAR(100)   NOT NULL,
    address                NVARCHAR(255)  NOT NULL,
    nationality            VARCHAR(10)    NOT NULL,
    phone                  VARCHAR(20)    NOT NULL,
    registered_address     NVARCHAR(255)  NOT NULL,
    current_address        NVARCHAR(255)  NOT NULL,
    occupation             NVARCHAR(50),
    employer               NVARCHAR(100),
    estimated_monthly_tx   INT,
    account_purpose        VARCHAR(30),
    fund_source            VARCHAR(30),
    tax_residency          VARCHAR(10),
    is_pep                 BIT            NOT NULL DEFAULT 0,
    id_front_url           VARCHAR(255)   NOT NULL,
    id_back_url            VARCHAR(255)   NOT NULL,
    second_id_url          VARCHAR(255)   NOT NULL,
    risk_flag              VARCHAR(30)    NOT NULL DEFAULT 'NORMAL',
    apply_ip               VARCHAR(45),
    status                 VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    reject_reason          NVARCHAR(500),
    reviewed_at            DATETIME2,
    reviewed_by            VARCHAR(50),
    created_account_number VARCHAR(14),
    created_at             DATETIME2      NOT NULL,
    updated_at             DATETIME2      NOT NULL
);
GO

CREATE INDEX idx_aa_customer ON ACCOUNT_APPLICATION(customer_id);
CREATE INDEX idx_aa_status ON ACCOUNT_APPLICATION(status);
GO

CREATE TABLE [ACCOUNT] (
    [account_number]        VARCHAR(14)     PRIMARY KEY,
    [customer_id]           VARCHAR(20)     NOT NULL,
    [account_type]          VARCHAR(20)     NOT NULL,
    [currency]              CHAR(3)         NOT NULL,
    [balance]               DECIMAL(19,4)   NULL DEFAULT 0,
    [liability]             DECIMAL(19,4)   NULL DEFAULT 0,
    [interest_rate]         DECIMAL(7,5)    NULL,
    [status]                VARCHAR(20)     NOT NULL,
    [parent_account_number] VARCHAR(14)     NULL,
    [created_at]            DATETIME2       NOT NULL,
    [changed_at]            DATETIME2       NOT NULL,
    [created_by]            VARCHAR(20)     NULL,
    [changed_by]            VARCHAR(20)     NULL,

    FOREIGN KEY ([parent_account_number]) REFERENCES [ACCOUNT]([account_number])
);
GO

CREATE INDEX idx_account_customer ON [ACCOUNT]([customer_id]);
CREATE INDEX idx_account_status ON [ACCOUNT]([status]);
GO

IF NOT EXISTS (SELECT 1 FROM [ACCOUNT] WHERE [account_number] = '909000000001')
BEGIN
    INSERT INTO [ACCOUNT] (
        [account_number], [customer_id], [account_type], [currency],
        [balance], [liability], [interest_rate], [status],
        [parent_account_number], [created_at], [changed_at], [created_by], [changed_by]
    ) VALUES (
        '909000000001', 'BANK_INTERNAL', 'BUSINESS', 'TWD',
        999999999999.0000, 0.0000, NULL, 'ACTIVE',
        NULL, SYSDATETIME(), SYSDATETIME(), 'system', 'system'
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM [ACCOUNT] WHERE [account_number] = '909000000002')
BEGIN
    INSERT INTO [ACCOUNT] (
        [account_number], [customer_id], [account_type], [currency],
        [balance], [liability], [interest_rate], [status],
        [parent_account_number], [created_at], [changed_at], [created_by], [changed_by]
    ) VALUES (
        '909000000002', 'BANK_INTERNAL', 'BUSINESS', 'TWD',
        0.0000, 0.0000, NULL, 'ACTIVE',
        NULL, SYSDATETIME(), SYSDATETIME(), 'system', 'system'
    );
END
GO

CREATE TABLE [ACCOUNT_STATUS_HISTORY] (
    [history_id]        CHAR(36)        PRIMARY KEY,
    [account_number]    VARCHAR(14)     NOT NULL,
    [old_status]        VARCHAR(20)     NULL,
    [new_status]        VARCHAR(20)     NOT NULL,
    [change_reason]     NVARCHAR(200)   NOT NULL,
    [changed_at]        DATETIME2       NOT NULL,
    [changed_by]        VARCHAR(20)     NULL,

    FOREIGN KEY ([account_number]) REFERENCES [ACCOUNT]([account_number])
);
GO

CREATE INDEX idx_ash_account ON [ACCOUNT_STATUS_HISTORY]([account_number]);
CREATE INDEX idx_ash_changed_at ON [ACCOUNT_STATUS_HISTORY]([changed_at]);
GO

CREATE TABLE [ACCOUNT_DAILY_SNAPSHOTS] (
    [snapshot_id]       CHAR(36)        PRIMARY KEY,
    [account_number]    VARCHAR(14)     NOT NULL,
    [snapshot_date]     DATE            NOT NULL,
    [balance]           DECIMAL(19,4)   NOT NULL,
    [interest_rate]     DECIMAL(7,5)    NOT NULL,
    [daily_interest]    DECIMAL(19,4)   NOT NULL,
    [created_at]        DATETIME2       NOT NULL,

    FOREIGN KEY ([account_number]) REFERENCES [ACCOUNT]([account_number])
);
GO

CREATE UNIQUE INDEX idx_ads_account_date ON [ACCOUNT_DAILY_SNAPSHOTS]([account_number], [snapshot_date]);
CREATE INDEX idx_ads_snapshot_date ON [ACCOUNT_DAILY_SNAPSHOTS]([snapshot_date]);
GO

CREATE TABLE [TRANS_LOG] (
    [transaction_id]        CHAR(36)        PRIMARY KEY,
    [reference_id]          VARCHAR(30)     NOT NULL,
    [account_number]        VARCHAR(14)     NOT NULL,
    [counterpart_account]   VARCHAR(20)     NULL,
    [bank_code]             VARCHAR(10)     NOT NULL DEFAULT '909',
    [bank_name]             NVARCHAR(50)    NOT NULL DEFAULT N'Java Easy Bank',
    [counterpart_bank_code] VARCHAR(10)     NULL,
    [counterpart_bank_name] NVARCHAR(50)    NULL,
    [is_interbank]          BIT             NOT NULL DEFAULT 0,
    [entry_type]            VARCHAR(10)     NOT NULL,
    [transaction_type]      VARCHAR(25)     NOT NULL,
    [amount]                DECIMAL(19,4)   NOT NULL,
    [fee_amount]            DECIMAL(19,4)   NOT NULL DEFAULT 0,
    [total_debit_amount]    DECIMAL(19,4)   NULL,
    [balance_before]        DECIMAL(19,4)   NOT NULL,
    [balance_after]         DECIMAL(19,4)   NOT NULL,
    [currency]              CHAR(3)         NOT NULL,
    [note]                  NVARCHAR(200)   NULL,
    [created_at]            DATETIME2(3)    NOT NULL,

    FOREIGN KEY ([account_number]) REFERENCES [ACCOUNT]([account_number])
);
GO

CREATE INDEX idx_tx_ref ON [TRANS_LOG]([reference_id]);
CREATE INDEX idx_tx_account_time ON [TRANS_LOG]([account_number], [created_at]);
CREATE INDEX idx_tx_counterpart ON [TRANS_LOG]([counterpart_account]);
GO

CREATE TABLE [FAVORITE_ACCOUNT] (
    [id]                BIGINT IDENTITY(1,1) PRIMARY KEY,
    [customer_id]       VARCHAR(20)     NOT NULL,
    [bank_code]         VARCHAR(10)     NOT NULL,
    [account_number]    VARCHAR(20)     NOT NULL,
    [alias]             NVARCHAR(50)    NOT NULL,
    [bank_name]         NVARCHAR(50)    NULL,
    [created_at]        DATETIME2       NOT NULL DEFAULT GETDATE(),
    [updated_at]        DATETIME2       NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_FAV_CUST_BANK_ACCT UNIQUE ([customer_id], [bank_code], [account_number])
);
GO

CREATE INDEX idx_fav_customer ON [FAVORITE_ACCOUNT]([customer_id]);
GO

CREATE TABLE [SCHEDULED_TRANSFER] (
    [id]                    BIGINT IDENTITY(1,1) PRIMARY KEY,
    [customer_id]           VARCHAR(20)     NOT NULL,
    [from_account_number]   VARCHAR(14)     NOT NULL,
    [to_account_number]     VARCHAR(20)     NOT NULL,
    [amount]                DECIMAL(19,4)   NOT NULL,
    [scheduled_date]        DATE            NOT NULL,
    [note]                  NVARCHAR(200)   NULL,
    [status]                VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    [executed_at]           DATETIME2       NULL,
    [fail_reason]           NVARCHAR(500)   NULL,
    [created_at]            DATETIME2       NOT NULL DEFAULT GETDATE(),
    [updated_at]            DATETIME2       NOT NULL DEFAULT GETDATE(),

    FOREIGN KEY ([from_account_number]) REFERENCES [ACCOUNT]([account_number])
);
GO

CREATE INDEX idx_st_customer ON [SCHEDULED_TRANSFER]([customer_id]);
CREATE INDEX idx_st_status_date ON [SCHEDULED_TRANSFER]([status], [scheduled_date]);
GO
