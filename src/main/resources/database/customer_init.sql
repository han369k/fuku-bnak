SET NOCOUNT ON;

CREATE TABLE CUSTOMER_APPLICATION (
    case_id      VARCHAR(20)  NOT NULL PRIMARY KEY,
    customer_id  VARCHAR(20)  NOT NULL,
    cif          VARCHAR(20)  NULL,
    name         NVARCHAR(50) NOT NULL,
    birthday     DATE         NOT NULL,
    gender       CHAR(1)      NOT NULL,
    id_number    VARCHAR(20)  NOT NULL,
    email        VARCHAR(100) NOT NULL,
    phone        VARCHAR(20)  NOT NULL,
    address      NVARCHAR(255) NOT NULL,
    username     VARCHAR(50)  NULL,
    password     VARCHAR(255) NULL,
    avatar_url   VARCHAR(255) NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'UNVERIFIED',
    reset_pwd_token VARCHAR(255) NULL,
    token_expiry  DATETIME2   NULL,
    created_at    DATETIME2   NOT NULL DEFAULT GETDATE(),
    updated_at    DATETIME2   NOT NULL DEFAULT GETDATE(),
    CONSTRAINT chk_customer_application_gender CHECK (gender IN ('M', 'F'))
);
GO

CREATE TABLE CUSTOMER_PROFILE (
    customer_id                         VARCHAR(20)  NOT NULL PRIMARY KEY,
    cif                                 VARCHAR(20)  NOT NULL UNIQUE,
    id_number                           VARCHAR(20)  NOT NULL,
    name                                NVARCHAR(50) NOT NULL,
    birthday                            DATE         NOT NULL,
    gender                              CHAR(1)      NOT NULL,
    email                               VARCHAR(100) NOT NULL,
    phone                               VARCHAR(20)  NOT NULL,
    address                             NVARCHAR(255) NOT NULL,
    nationality                         VARCHAR(10)  NULL,
    registered_address                  NVARCHAR(255) NULL,
    current_address                     NVARCHAR(255) NULL,
    occupation                          NVARCHAR(50) NULL,
    employer                            NVARCHAR(100) NULL,
    estimated_monthly_tx                INT NULL,
    account_purpose                     VARCHAR(30) NULL,
    fund_source                         NVARCHAR(50) NULL,
    tax_residency                       VARCHAR(10) NULL,
    is_pep                              BIT NOT NULL DEFAULT 0,
    id_front_url                        VARCHAR(255) NULL,
    id_back_url                         VARCHAR(255) NULL,
    second_id_url                       VARCHAR(255) NULL,
    latest_account_application_id       BIGINT NULL,
    latest_account_application_no       VARCHAR(30) NULL,
    latest_applied_account_type         VARCHAR(20) NULL,
    latest_applied_currency             VARCHAR(3) NULL,
    latest_account_application_status   VARCHAR(20) NULL,
    latest_account_application_risk_flag VARCHAR(30) NULL,
    latest_account_application_reviewed_at DATETIME2 NULL,
    latest_account_application_reviewed_by VARCHAR(50) NULL,
    latest_account_application_reject_reason NVARCHAR(500) NULL,
    created_account_number              VARCHAR(12) NULL,
    account_application_synced_at       DATETIME2 NULL,
    avatar_url                          VARCHAR(255) NULL,
    job                                 NVARCHAR(100) NULL,
    annual_income                       INT NULL,
    risk_level                          VARCHAR(20) NULL,
    status                              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at                          DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at                          DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT chk_customer_profile_gender CHECK (gender IN ('M', 'F'))
);
GO

CREATE TABLE CUSTOMER_AUTH (
    auth_id                VARCHAR(20)  NOT NULL PRIMARY KEY,
    customer_id            VARCHAR(20)  NOT NULL UNIQUE,
    username               VARCHAR(50)  NOT NULL UNIQUE,
    password_hash          VARCHAR(255) NOT NULL,
    role                   VARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',
    status                 VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    reset_token            VARCHAR(255) NULL,
    reset_token_expiry     DATETIME2    NULL,
    last_login_date        DATETIME2    NULL,
    verification_token     VARCHAR(255) NULL,
    created_at             DATETIME2    NOT NULL DEFAULT GETDATE(),
    updated_at             DATETIME2    NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_customer_auth_profile FOREIGN KEY (customer_id) REFERENCES CUSTOMER_PROFILE(customer_id)
);
GO

CREATE TABLE CUSTOMER_LOGIN_LOG (
    login_log_id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    customer_id  VARCHAR(20) NULL,
    username     VARCHAR(50) NOT NULL,
    result       NVARCHAR(20) NOT NULL,
    fail_reason  NVARCHAR(200) NULL,
    ip_address   VARCHAR(45) NULL,
    user_agent   NVARCHAR(512) NULL,
    device_name  NVARCHAR(120) NULL,
    login_time   DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_customer_login_log_profile FOREIGN KEY (customer_id) REFERENCES CUSTOMER_PROFILE(customer_id)
);
GO

CREATE INDEX idx_customer_login_log_customer_time
    ON CUSTOMER_LOGIN_LOG(customer_id, login_time DESC);
GO

CREATE TABLE CUSTOMER_DEVICE (
    device_id         BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    customer_id       VARCHAR(20) NOT NULL,
    device_fingerprint VARCHAR(64) NOT NULL,
    device_name       NVARCHAR(120) NOT NULL,
    browser_name      NVARCHAR(60) NULL,
    operating_system  NVARCHAR(60) NULL,
    ip_address        VARCHAR(45) NULL,
    user_agent        NVARCHAR(512) NULL,
    status            VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    trusted           BIT NOT NULL DEFAULT 1,
    first_seen_at     DATETIME2 NOT NULL DEFAULT GETDATE(),
    last_seen_at      DATETIME2 NOT NULL DEFAULT GETDATE(),
    revoked_at        DATETIME2 NULL,
    CONSTRAINT fk_customer_device_profile FOREIGN KEY (customer_id) REFERENCES CUSTOMER_PROFILE(customer_id),
    CONSTRAINT uk_customer_device_fingerprint UNIQUE (customer_id, device_fingerprint)
);
GO

CREATE INDEX idx_customer_device_last_seen
    ON CUSTOMER_DEVICE(customer_id, last_seen_at DESC);
GO

CREATE TABLE CUSTOMER_KYC (
    customer_id      VARCHAR(20) NOT NULL PRIMARY KEY,
    id_issue_date    DATE NOT NULL,
    id_issue_location NVARCHAR(20) NOT NULL,
    id_issue_type    NVARCHAR(20) NOT NULL,
    tax_residency    VARCHAR(10) NOT NULL DEFAULT 'TW',
    marital_status   CHAR(1) NOT NULL DEFAULT 'S',
    education_level  NVARCHAR(20) NULL,
    occupation_category NVARCHAR(50) NULL,
    company_name     NVARCHAR(100) NULL,
    annual_income    INT NULL,
    source_of_wealth NVARCHAR(50) NULL,
    updated_at       DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_customer_kyc_profile FOREIGN KEY (customer_id) REFERENCES CUSTOMER_PROFILE(customer_id)
);
GO

CREATE TABLE CUSTOMER_RISK_TAG (
    customer_id        VARCHAR(20) NOT NULL PRIMARY KEY,
    aml_risk_level     VARCHAR(10) NOT NULL DEFAULT 'LOW',
    pep_status         CHAR(1) NOT NULL DEFAULT 'N',
    is_fraud_suspect   CHAR(1) NOT NULL DEFAULT 'N',
    block_reason       NVARCHAR(100) NULL,
    kyc_last_review_date DATE NOT NULL DEFAULT GETDATE(),
    kyc_next_review_date DATE NOT NULL,
    updated_by         VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at         DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT fk_customer_risk_tag_profile FOREIGN KEY (customer_id) REFERENCES CUSTOMER_PROFILE(customer_id),
    CONSTRAINT chk_customer_risk_tag_aml CHECK (aml_risk_level IN ('LOW', 'MEDIUM', 'HIGH', 'BLACKLIST')),
    CONSTRAINT chk_customer_risk_tag_pep CHECK (pep_status IN ('Y', 'N'))
);
GO
