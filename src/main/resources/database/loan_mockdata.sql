-- ============================================================
-- Loan Module Mock Data
-- 依賴：customer_profile 表必須先有資料（執行 auth_customer_insert.sql）
-- 清除順序：由下往上刪（FK 依賴）
-- ============================================================

-- ===== 清除舊資料 =====
DELETE FROM LOAN_CONTACT_LOG;
DELETE FROM LOAN_REVIEW_DETAIL;
DELETE FROM LOAN_APPLICATION;

-- ===== LOAN_APPLICATION =====
-- 使用 customer_profile 的真實 customer_id
-- 會員申請：填 customer_id，name/phone/email 留 NULL（從 customer_profile 查）
-- 非會員申請：customer_id 留 NULL，填寫 name/phone/email
INSERT INTO LOAN_APPLICATION (
    application_id, customer_id,
    applicant_name, applicant_phone, applicant_email,
    apply_type, apply_amount, apply_period, rate,
    application_status, create_time,
    latest_contact_status, latest_contact_time
) VALUES

-- 1. 會員 王大明 | 個人信貸 36期
('LA202504201031000001', 'X7K9P2M4',
 NULL, NULL, NULL,
 'PERSONAL', 500000, 36, 0.04500,
 'PENDING_CONTACT', '2025-04-20 10:31:00',
 NULL, NULL),

-- 2. 非會員 | 汽車貸款 60期
('LA202504211445120023', NULL,
 N'林美玲', '0912345678', 'meilinlin@gmail.com',
 'CAR', 800000, 60, 0.03500,
 'PENDING_CONTACT', '2025-04-21 14:45:12',
 NULL, NULL),

-- 3. 會員 陳建國 | 房屋貸款 240期
('LA202504220830054521', 'D3H8F5G2',
 NULL, NULL, NULL,
 'HOUSE', 12000000, 240, 0.02200,
 'PENDING_CONTACT', '2025-04-22 08:30:05',
 NULL, NULL),

-- 4. 非會員 | 學貸 84期
('LA202504231600337712', NULL,
 N'陳柏宇', '0923456789', 'boyuchen@student.edu.tw',
 'STUDENT', 200000, 84, 0.01500,
 'PENDING_CONTACT', '2025-04-23 16:00:33',
 NULL, NULL),

-- 5. 會員 李志明 | 創業貸款 60期
('LA202504241120089934', 'P6M4N2Q8',
 NULL, NULL, NULL,
 'BUSINESS', 3000000, 60, 0.03000,
 'PENDING_CONTACT', '2025-04-24 11:20:08',
 NULL, NULL),

-- 6. 會員 蔡佳蓉 | 個人信貸 24期
('LA202505011400000001', 'Y5R4W1H6',
 NULL, NULL, NULL,
 'PERSONAL', 300000, 24, 0.04500,
 'APPROVED', '2025-05-01 14:00:00',
 'CONTACTED', '2025-05-02 10:00:00'),

-- 7. 會員 劉冠宇 | 汽車貸款 48期
('LA202505021030000002', 'G7N3M8P2',
 NULL, NULL, NULL,
 'CAR', 650000, 48, 0.03500,
 'REJECTED', '2025-05-02 10:30:00',
 'CONTACTED', '2025-05-03 09:15:00'),

-- 8. 非會員 | 房屋貸款 360期
('LA202505030900000003', NULL,
 N'周政廷', '0934567890', 'chengt.chou@gmail.com',
 'HOUSE', 8000000, 360, 0.02200,
 'PENDING_CONTACT', '2025-05-03 09:00:00',
 NULL, NULL),

-- 9. 會員 許家瑩 | 個人信貸 12期
('LA202505041100000004', 'J2F6K9V1',
 NULL, NULL, NULL,
 'PERSONAL', 100000, 12, 0.04500,
 'APPROVED', '2025-05-04 11:00:00',
 'CONTACTED', '2025-05-04 15:30:00'),

-- 10. 會員 鄭宗翰 | 創業貸款 84期
('LA202505051530000005', 'Q4W8C1T7',
 NULL, NULL, NULL,
 'BUSINESS', 5000000, 84, 0.03000,
 'PENDING_CONTACT', '2025-05-05 15:30:00',
 NULL, NULL);
