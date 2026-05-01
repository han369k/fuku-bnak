/* =========================
   1️⃣ 重建資料庫（最乾淨）
   ========================= */
USE master;
IF DB_ID('java_easy_bank') IS NOT NULL
BEGIN
    ALTER DATABASE java_easy_bank SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE java_easy_bank;
END
GO

CREATE DATABASE java_easy_bank;
GO

USE java_easy_bank;
GO

/* =========================
   2️⃣ 建立資料表
   ========================= */

-- 客戶資料表
CREATE TABLE [CUSTOMER] (
   customer_id INT PRIMARY KEY,           -- 客戶ID（主鍵）
    name NVARCHAR(50) NOT NULL             -- 客戶姓名
);

-- 卡別資料表
CREATE TABLE [CARD_TYPE] (
    card_type_id INT IDENTITY(1,1) PRIMARY KEY, -- 卡別ID（主鍵）
    card_type_name NVARCHAR(50) NOT NULL,       -- 卡別名稱（如：現金回饋卡、旅遊卡）
    brand NVARCHAR(20) NOT NULL,                -- 卡片品牌（VISA / Master / JCB）
    annual_fee DECIMAL(10,2),                   -- 年費
    cashback_rate DECIMAL(5,2),                 -- 回饋比例（%）
    card_image_url VARCHAR(255)                 -- 卡片圖片URL
    
);

-- 商家資料表
CREATE TABLE [MERCHANT] (
    merchant_id INT PRIMARY KEY,  -- 商家ID（主鍵）
    merchant_name NVARCHAR(100) not null,       -- 商家名稱
    merchant_category NVARCHAR(50) not null              -- 商家分類（餐飲 / 交通 / 購物 / 娛樂）
);

-- 信用卡申請主表
CREATE TABLE [CARD_APPLICATION] (
    application_id INT IDENTITY(1,1) PRIMARY KEY, -- 申請單ID（主鍵）
    customer_id INT NOT NULL,                     -- 客戶ID（對應 CUSTOMER）
    apply_date DATETIME DEFAULT GETDATE(),        -- 申請時間
    status NVARCHAR(20) DEFAULT 'PENDING',        -- 申請狀態（PENDING / APPROVED / REJECTED / PARTIAL）
    remark NVARCHAR(200),                         -- 備註（補件說明 / 人工審核內容）
    FOREIGN KEY (customer_id) REFERENCES [CUSTOMER](customer_id)
    
);

-- 信用卡申請明細（可一次申請多張卡）
CREATE TABLE [CARD_APPLICATION_ITEM] (
    item_id INT IDENTITY(1,1) PRIMARY KEY,       -- 明細ID（主鍵）
    application_id INT NOT NULL,                 -- 申請單ID（對應主表）
    card_type_id INT NOT NULL,                   -- 卡別ID（對應 CARD_TYPE）
    result NVARCHAR(20) DEFAULT 'PENDING',       -- 審核結果（PENDING / APPROVED / REJECTED）
    approved_limit DECIMAL(15,2),                -- 核准額度
    annual_fee DECIMAL(10,2),                    -- 核准後年費（可能不同原卡別）
    create_card_flag BIT DEFAULT 0,              -- 是否已建立信用卡（0=否 / 1=是）
    remark NVARCHAR(200),                        -- 備註（拒絕原因等）
    FOREIGN KEY (application_id) REFERENCES CARD_APPLICATION(application_id),
    FOREIGN KEY (card_type_id) REFERENCES CARD_TYPE(card_type_id)
);

-- 信用卡主表
CREATE TABLE [CREDIT_CARD] (
    card_id INT IDENTITY(1,1) PRIMARY KEY,       -- 信用卡ID（主鍵）
    customer_id INT NOT NULL,                   -- 客戶ID（持卡人）
    card_type_id INT NOT NULL,                  -- 卡別ID
    application_item_id INT NULL,               -- 對應申請明細（從哪個申請來）
    card_number VARCHAR(16) UNIQUE NOT NULL,    -- 信用卡卡號（唯一）
    expiry_date DATE NOT NULL,                  -- 到期日
    credit_limit DECIMAL(15,2),                 -- 信用額度
    current_balance DECIMAL(15,2) DEFAULT 0,    -- 已使用金額
    create_date DATETIME DEFAULT GETDATE(),     -- 開卡日期
    status NVARCHAR(20) CHECK (status IN ('ACTIVE','BLOCKED')), -- 卡片狀態
    FOREIGN KEY (card_type_id) REFERENCES [CARD_TYPE](card_type_id),
    FOREIGN KEY (application_item_id) REFERENCES CARD_APPLICATION_ITEM(item_id)
);

-- 信用卡帳單
CREATE TABLE [CARD_BILL] (
    bill_id INT IDENTITY(1,1) PRIMARY KEY,      -- 帳單ID（主鍵）
    card_id INT NOT NULL,                       -- 信用卡ID
    billing_month VARCHAR(7),                   -- 帳單月份（YYYY-MM）
    bill_date DATE,                             -- 帳單產生日期
    due_date DATE,                              -- 繳款截止日
    total_amount DECIMAL(15,2),                 -- 帳單總金額
    minimum_payment DECIMAL(15,2),              -- 最低應繳金額
    paid_amount DECIMAL(15,2),                  -- 已繳金額
    bill_status NVARCHAR(20),                   -- 帳單狀態（UNPAID / PARTIAL / PAID）
    FOREIGN KEY (card_id) REFERENCES [CREDIT_CARD](card_id)
);
-- 信用卡交易紀錄
CREATE TABLE [CARD_TRANSACTION] (
    txn_id INT IDENTITY(1,1) PRIMARY KEY,       -- 交易ID（主鍵）
    card_id INT NOT NULL,                       -- 信用卡ID
    merchant_id INT  NULL,                      -- 商家ID(可為null，退款用)
    ref_txn_id INT NULL,                        -- 關聯交易（例如退款對應原交易）
    txn_amount DECIMAL(15,2) NOT NULL,          -- 交易金額
    txn_type NVARCHAR(20),                      -- 交易類型（PURCHASE / REFUND / PAYMENT）
    txn_date DATETIME ,                         -- 交易時間
    description NVARCHAR(200),                  -- 備註（交易說明）
    FOREIGN KEY (card_id) REFERENCES [CREDIT_CARD](card_id),
    FOREIGN KEY (merchant_id) REFERENCES [MERCHANT](merchant_id),
    -- 自關聯 FK（退款）
    FOREIGN KEY (ref_txn_id) REFERENCES CARD_TRANSACTION(txn_id)
);