/*
===============================================================================
Java Easy Bank 系統權限模組 (Auth Module)
===============================================================================
*/

-- [1] 實體部門表 (AUTH_DEPT)
CREATE TABLE AUTH_DEPT (
    dept_id       VARCHAR(10)   PRIMARY KEY,
    dept_code     VARCHAR(20)   NOT NULL UNIQUE, -- 如: CF, CS, CR
    dept_name     NVARCHAR(50)  NOT NULL         -- 如: 消費金融部
);

-- [2] 角色權限表 (AUTH_ROLE)
CREATE TABLE AUTH_ROLE (
    role_id       VARCHAR(10)   PRIMARY KEY,
    dept_id       VARCHAR(10)   NOT NULL,     
    role_code     VARCHAR(20)   NOT NULL UNIQUE, -- 系統代碼 (如: CFSO, CISO)
    role_name     NVARCHAR(50)  NOT NULL,        -- 職務名稱
    perm_level    TINYINT       NOT NULL,        -- 0(Maker), 1(Checker), 2(Approver), 3(Sr.Approver), 4(Admin)
    perm_scope    VARCHAR(10)   NULL,            -- 擴充範圍: 'RO'(唯讀), 'BIZ'(業務), 'SYS'(系統)
    
    CONSTRAINT FK_Role_Dept FOREIGN KEY (dept_id) REFERENCES AUTH_DEPT(dept_id),
    CONSTRAINT CHK_Perm_Level CHECK (perm_level IN (0, 1, 2, 3, 4))
);

-- [3] 員工帳號表 (AUTH_EMP)
CREATE TABLE AUTH_EMP (
    emp_id              VARCHAR(10)     PRIMARY KEY,
    emp_name            NVARCHAR(50)    NOT NULL,
    dept_id             VARCHAR(10)     NOT NULL,     
    role_id             VARCHAR(10)     NOT NULL,     
    email               VARCHAR(100)    NOT NULL UNIQUE, 
    password_hash       VARCHAR(255)    NOT NULL,     
    
    status              VARCHAR(10)     DEFAULT 'ACTIVE',   -- ACTIVE, SUSPENDED, LOCKED
    contract_end_date   DATETIME2       NULL,               -- 約聘到期日
    permission_expire   DATETIME2       NOT NULL,           -- 權限覆審到期日
    
    failed_attempts     TINYINT         DEFAULT 0,          -- 密碼連續錯誤次數
    pwd_updated_at      DATETIME2       DEFAULT GETDATE(),  -- 密碼最後更新時間 (90天換密碼用)
    last_login_date     DATETIME2       NULL,               -- 最後登入時間
    created_at          DATETIME2       DEFAULT GETDATE(),
    updated_at          DATETIME2       DEFAULT GETDATE(),
    
    CONSTRAINT FK_Emp_Dept FOREIGN KEY (dept_id) REFERENCES AUTH_DEPT(dept_id),
    CONSTRAINT FK_Emp_Role FOREIGN KEY (role_id) REFERENCES AUTH_ROLE(role_id)
);

-- [4] 登入日誌表 (AUTH_LOGIN_LOG)
CREATE TABLE AUTH_LOGIN_LOG (
    log_id          BIGINT IDENTITY(1,1) PRIMARY KEY, 
    attempt_email   VARCHAR(100)    NOT NULL,         -- 紀錄當下輸入的信箱
    emp_id          VARCHAR(10)     NULL,             -- 允許 NULL (防假帳號登入報錯)
    login_time      DATETIME2       DEFAULT GETDATE(),
    login_result    VARCHAR(10)     NOT NULL,         
    fail_reason     NVARCHAR(200)   NULL,             
    ip_address      VARCHAR(45)     NULL,             
    
    CONSTRAINT FK_Log_Emp FOREIGN KEY (emp_id) REFERENCES AUTH_EMP(emp_id)
);
GO

/*
===============================================================================
Java Easy Bank 客戶模組 (Customer Module)
===============================================================================
*/

-- [1] 進件申請表
CREATE TABLE CUSTOMER_APPLICATION (
    case_id VARCHAR(20) PRIMARY KEY,
    customer_id VARCHAR(20) NOT NULL,
    cif VARCHAR(20) NULL,
    name NVARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    gender CHAR(1) NOT NULL,
    id_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address NVARCHAR(255) NOT NULL,
    username VARCHAR(50) NULL,
    password VARCHAR(255) NULL,
    avatar_url VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'UNVERIFIED',
    reset_pwd_token VARCHAR(255) NULL,
    token_expiry DATETIME2 NULL,
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT CHK_App_Gender CHECK (gender IN ('M', 'F'))
);

-- [2] 正式客戶視圖
CREATE TABLE CUSTOMER_PROFILE (
    customer_id VARCHAR(20) NOT NULL PRIMARY KEY, -- 預設建立叢集索引 (Clustered Index)
    cif VARCHAR(20) NOT NULL UNIQUE,
    id_number VARCHAR(20) NOT NULL UNIQUE,
    name NVARCHAR(50) NOT NULL,
    birthday DATE NOT NULL,
    gender CHAR(1) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    address NVARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT CHK_Profile_Gender CHECK (gender IN ('M', 'F'))
);

-- [3] KYC 資料表
CREATE TABLE CUSTOMER_KYC (
    customer_id VARCHAR(20) NOT NULL PRIMARY KEY,
    id_issue_date DATE NOT NULL,
    id_issue_location NVARCHAR(20) NOT NULL,
    id_issue_type NVARCHAR(20) NOT NULL, 
    tax_residency VARCHAR(10) NOT NULL DEFAULT 'TW',
    marital_status CHAR(1) NOT NULL DEFAULT 'S',
    education_level NVARCHAR(20) NULL,
    occupation_category NVARCHAR(50) NULL,
    company_name NVARCHAR(100) NULL,
    annual_income INT NULL,
    source_of_wealth NVARCHAR(50) NULL,
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_KYC_Profile FOREIGN KEY (customer_id) REFERENCES CUSTOMER_PROFILE(customer_id),
    CONSTRAINT CHK_Issue_Type CHECK (id_issue_type IN (N'初發', N'補發', N'換發'))
);

-- [4] 風控標籤表
CREATE TABLE CUSTOMER_RISK_TAG (
    customer_id VARCHAR(20) NOT NULL PRIMARY KEY,
    aml_risk_level VARCHAR(10) NOT NULL DEFAULT 'LOW',
    pep_status CHAR(1) NOT NULL DEFAULT 'N',
    is_fraud_suspect CHAR(1) NOT NULL DEFAULT 'N',
    block_reason NVARCHAR(100) NULL,
    kyc_last_review_date DATE NOT NULL DEFAULT GETDATE(),
    kyc_next_review_date DATE NOT NULL,
    updated_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Risk_Profile FOREIGN KEY (customer_id) REFERENCES CUSTOMER_PROFILE(customer_id),
    CONSTRAINT CHK_AML_Risk CHECK (aml_risk_level IN ('LOW', 'MEDIUM', 'HIGH', 'BLACKLIST')),
    CONSTRAINT CHK_PEP_Status CHECK (pep_status IN ('Y', 'N'))
);
GO

