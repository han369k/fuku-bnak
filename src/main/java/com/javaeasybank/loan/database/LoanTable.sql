-- ===建表===
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

-- ===測試資料（全部 PENDING_CONTACT）===
INSERT INTO LOAN_APPLICATION (
    application_id, customer_id,
    applicant_name, applicant_phone, applicant_email,
    apply_type, apply_amount, apply_period, rate,
    application_status, create_time,
    latest_contact_status, latest_contact_time
) VALUES

-- 1. 會員 | 個人信貸 36期 | rate = 0.04 + 0.005 = 0.045
('LA202504201031000001', 'C10001',
 NULL, NULL, NULL,
 'PERSONAL', 500000, 36, 0.04500,
 'PENDING_CONTACT', '2025-04-20 10:31:00',
 NULL, NULL),

-- 2. 非會員 | 汽車貸款 60期 | rate = 0.025 + 0.01 = 0.035
('LA202504211445120023', NULL,
 '林美玲', '0912345678', 'meilinlin@gmail.com',
 'CAR', 800000, 60, 0.03500,
 'PENDING_CONTACT', '2025-04-21 14:45:12',
 NULL, NULL),

-- 3. 會員 | 房屋貸款 240期 | rate = 0.018 + 0.004 = 0.022
('LA202504220830054521', 'C10047',
 NULL, NULL, NULL,
 'HOUSE', 12000000, 240, 0.02200,
 'PENDING_CONTACT', '2025-04-22 08:30:05',
 NULL, NULL),

-- 4. 非會員 | 學貸 84期 | fixedRate → rate = 0.015
('LA202504231600337712', NULL,
 '陳柏宇', '0923456789', 'boyuchen@student.edu.tw',
 'STUDENT', 200000, 84, 0.01500,
 'PENDING_CONTACT', '2025-04-23 16:00:33',
 NULL, NULL),

-- 5. 會員 | 創業貸款 60期 | rate = 0.02 + 0.01 = 0.030
('LA202504241120089934', 'C10085',
 NULL, NULL, NULL,
 'BUSINESS', 3000000, 60, 0.03000,
 'PENDING_CONTACT', '2025-04-24 11:20:08',
 NULL, NULL)

TRUNCATE TABLE loan_application
TRUNCATE TABLE loan_contact_log
TRUNCATE TABLE loan_review_detail