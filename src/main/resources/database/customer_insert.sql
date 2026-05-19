SET NOCOUNT ON;

DECLARE @pwd_hash VARCHAR(255) = '$2a$10$1Wa/uOR8VZXCrOwfd9.2ZOtgSeATa0xVvdSsYudUgtCSsQJ0M2cDS';

INSERT INTO CUSTOMER_PROFILE (
    customer_id, cif, id_number, name, birthday, gender, email, phone, address,
    nationality, registered_address, current_address, occupation, employer,
    estimated_monthly_tx, account_purpose, fund_source, tax_residency, is_pep,
    id_front_url, id_back_url, second_id_url, avatar_url, job, annual_income,
    risk_level, status, created_at, updated_at
)
VALUES
('Q8M4T7K2', '2605-A3R5W8J1', 'A123456789', N'Ming Wang', '1985-05-15', 'M', 'ming.wang@email.com', '0912000001', N'Taipei City', 'TW', N'Taipei City', N'Taipei City', N'CLERICAL', N'Java Easy Bank', 5, 'SALARY', 'SALARY', 'TW', 0, '/uploads/mock/customer/Q8M4T7K2-front.jpg', '/uploads/mock/customer/Q8M4T7K2-back.jpg', '/uploads/mock/customer/Q8M4T7K2-second.jpg', '/avatars/mock/Q8M4T7K2.png', N'Office staff', 433534, 'MEDIUM', 'ACTIVE', '2026-01-02 09:00:00', '2026-01-02 09:00:00'),
('R5N9W3A6', '2605-N9Q2E7C4', 'B123456789', N'Amy Chen', '1980-02-02', 'F', 'amy.chen@email.com', '0912000002', N'New Taipei City', 'TW', N'New Taipei City', N'New Taipei City', N'PROFESSIONAL', N'Consulting', 10, 'INVESTMENT', 'SAVINGS', 'TW', 0, '/uploads/mock/customer/R5N9W3A6-front.jpg', '/uploads/mock/customer/R5N9W3A6-back.jpg', '/uploads/mock/customer/R5N9W3A6-second.jpg', '/avatars/mock/R5N9W3A6.png', N'Engineer', 1140000, 'LOW', 'ACTIVE', '2026-02-03 10:07:00', '2026-02-03 10:07:00'),
('H7C2P8D4', '2605-M1V6P4K9', 'C123456789', N'Jason Lin', '1982-03-03', 'M', 'jason.lin@email.com', '0912000003', N'Kaohsiung City', 'TW', N'Kaohsiung City', N'Kaohsiung City', N'TECHNICIAN', N'Tech Co.', 20, 'SAVINGS', 'SALARY', 'TW', 0, '/uploads/mock/customer/H7C2P8D4-front.jpg', '/uploads/mock/customer/H7C2P8D4-back.jpg', '/uploads/mock/customer/H7C2P8D4-second.jpg', '/avatars/mock/H7C2P8D4.png', N'Technician', 900000, 'LOW', 'ACTIVE', '2026-03-04 11:14:00', '2026-03-04 11:14:00'),
('V6J3X9M5', '2605-H5T2J8X3', 'D123456789', N'Linda Wu', '1989-04-04', 'F', 'linda.wu@email.com', '0912000004', N'Taichung City', 'TW', N'Taichung City', N'Taichung City', N'OTHER', N'Freelance', 30, 'SAVINGS', 'SALARY', 'TW', 0, '/uploads/mock/customer/V6J3X9M5-front.jpg', '/uploads/mock/customer/V6J3X9M5-back.jpg', '/uploads/mock/customer/V6J3X9M5-second.jpg', '/avatars/mock/V6J3X9M5.png', N'Freelancer', 1080000, 'LOW', 'ACTIVE', '2026-04-05 12:21:00', '2026-04-05 12:21:00'),
('K2T8B4R7', '2605-L7F3Y1W6', 'E123456789', N'Peter Huang', '1996-05-05', 'M', 'peter.huang@email.com', '0912000005', N'New Taipei City', 'TW', N'New Taipei City', N'New Taipei City', N'CLERICAL', N'Office', 50, 'SAVINGS', 'SALARY', 'TW', 0, '/uploads/mock/customer/K2T8B4R7-front.jpg', '/uploads/mock/customer/K2T8B4R7-back.jpg', '/uploads/mock/customer/K2T8B4R7-second.jpg', '/avatars/mock/K2T8B4R7.png', N'Clerk', 960000, 'LOW', 'ACTIVE', '2026-05-06 13:28:00', '2026-05-06 13:28:00'),
('M9A5D2Q8', '2605-C4G8R2M5', 'F123456789', N'Sophia Lee', '1969-06-06', 'F', 'sophia.lee@email.com', '0912000006', N'Yilan County', 'TW', N'Yilan County', N'Yilan County', N'SERVICE_SALES', N'Sales', 80, 'DAILY_EXPENSE', 'SALARY', 'TW', 0, '/uploads/mock/customer/M9A5D2Q8-front.jpg', '/uploads/mock/customer/M9A5D2Q8-back.jpg', '/uploads/mock/customer/M9A5D2Q8-second.jpg', '/avatars/mock/M9A5D2Q8.png', N'Sales associate', 540000, 'LOW', 'ACTIVE', '2026-01-07 14:35:00', '2026-01-07 14:35:00'),
('P4W7N6C3', '2605-T6N1P9V8', 'G123456789', N'Kevin Chang', '1976-07-07', 'M', 'kevin.chang@email.com', '0912000007', N'Hsinchu City', 'TW', N'Hsinchu City', N'Hsinchu City', N'PROFESSIONAL', N'Bank', 100, 'SALARY', 'SALARY', 'TW', 0, '/uploads/mock/customer/P4W7N6C3-front.jpg', '/uploads/mock/customer/P4W7N6C3-back.jpg', '/uploads/mock/customer/P4W7N6C3-second.jpg', '/avatars/mock/P4W7N6C3.png', N'Professional', 880000, 'LOW', 'ACTIVE', '2026-02-08 15:42:00', '2026-02-08 15:42:00'),
('T8H2K5V9', '2605-F2K7M3L9', 'H123456789', N'Grace Ho', '1983-08-08', 'F', 'grace.ho@email.com', '0912000008', N'Chiayi City', 'TW', N'Chiayi City', N'Chiayi City', N'LEGISLATOR_MANAGER', N'Company', 5, 'BUSINESS', 'BUSINESS_INCOME', 'TW', 0, '/uploads/mock/customer/T8H2K5V9-front.jpg', '/uploads/mock/customer/T8H2K5V9-back.jpg', '/uploads/mock/customer/T8H2K5V9-second.jpg', '/avatars/mock/T8H2K5V9.png', N'Business owner', 1320000, 'LOW', 'ACTIVE', '2026-03-09 16:49:00', '2026-03-09 16:49:00'),
('A6R3M8J2', '2605-X1W5C4T6', 'J123456789', N'Howard Liu', '1990-09-09', 'M', 'howard.liu@email.com', '0912000009', N'Changhua County', 'TW', N'Changhua County', N'Changhua County', N'CLERICAL', N'Office', 10, 'PAYMENT', 'SALARY', 'TW', 0, '/uploads/mock/customer/A6R3M8J2-front.jpg', '/uploads/mock/customer/A6R3M8J2-back.jpg', '/uploads/mock/customer/A6R3M8J2-second.jpg', '/avatars/mock/A6R3M8J2.png', N'Office staff', 940000, 'MEDIUM', 'ACTIVE', '2026-04-10 17:56:00', '2026-04-10 17:56:00'),
('D5Q9T2W7', '2605-R8Y4H7N2', 'K123456789', N'Winnie Yang', '1997-10-10', 'F', 'winnie.yang@email.com', '0912000010', N'Tainan City', 'TW', N'Tainan City', N'Tainan City', N'SERVICE_SALES', N'Food service', 20, 'DAILY_EXPENSE', 'SALARY', 'TW', 0, '/uploads/mock/customer/D5Q9T2W7-front.jpg', '/uploads/mock/customer/D5Q9T2W7-back.jpg', '/uploads/mock/customer/D5Q9T2W7-second.jpg', '/avatars/mock/D5Q9T2W7.png', N'Service', 760000, 'LOW', 'ACTIVE', '2026-05-11 09:03:00', '2026-05-11 09:03:00'),
('F7V4C8N2', '2605-V3L9M2P5', 'L123456789', N'Jason Wu', '1970-11-11', 'M', 'jason.wu@email.com', '0912000011', N'Hualien County', 'TW', N'Hualien County', N'Hualien County', N'PROFESSIONAL', N'Finance', 30, 'INVESTMENT', 'SALARY', 'TW', 0, '/uploads/mock/customer/F7V4C8N2-front.jpg', '/uploads/mock/customer/F7V4C8N2-back.jpg', '/uploads/mock/customer/F7V4C8N2-second.jpg', '/avatars/mock/F7V4C8N2.png', N'Professional', 1180000, 'LOW', 'ACTIVE', '2026-01-12 10:10:00', '2026-01-12 10:10:00'),
('G3K6P9M5', '2605-J7X1W6C8', 'M123456789', N'Emily Chen', '1977-12-12', 'F', 'emily.chen@email.com', '0912000012', N'Pingtung County', 'TW', N'Pingtung County', N'Pingtung County', N'LEGISLATOR_MANAGER', N'Business', 50, 'BUSINESS', 'BUSINESS_INCOME', 'TW', 0, '/uploads/mock/customer/G3K6P9M5-front.jpg', '/uploads/mock/customer/G3K6P9M5-back.jpg', '/uploads/mock/customer/G3K6P9M5-second.jpg', '/avatars/mock/G3K6P9M5.png', N'Business owner', 1380000, 'LOW', 'ACTIVE', '2026-02-13 11:17:00', '2026-02-13 11:17:00'),
('J8R2D5A7', '2605-K4F8N5R2', 'N123456789', N'Allen Chou', '1984-01-13', 'M', 'allen.chou@email.com', '0912000013', N'Yunlin County', 'TW', N'Yunlin County', N'Yunlin County', N'CLERICAL', N'Bank', 80, 'SALARY', 'SALARY', 'TW', 0, '/uploads/mock/customer/J8R2D5A7-front.jpg', '/uploads/mock/customer/J8R2D5A7-back.jpg', '/uploads/mock/customer/J8R2D5A7-second.jpg', '/avatars/mock/J8R2D5A7.png', N'Office staff', 1080000, 'LOW', 'ACTIVE', '2026-03-14 12:24:00', '2026-03-14 12:24:00'),
('L4N9T6Q3', '2605-W9T2C7H4', 'O123456789', N'Olivia Ho', '1991-02-14', 'F', 'olivia.ho@email.com', '0912000014', N'Keelung City', 'TW', N'Keelung City', N'Keelung City', N'PROFESSIONAL', N'Consulting', 100, 'INVESTMENT', 'SALARY', 'TW', 0, '/uploads/mock/customer/L4N9T6Q3-front.jpg', '/uploads/mock/customer/L4N9T6Q3-back.jpg', '/uploads/mock/customer/L4N9T6Q3-second.jpg', '/avatars/mock/L4N9T6Q3.png', N'Consultant', 1260000, 'LOW', 'ACTIVE', '2026-04-15 13:31:00', '2026-04-15 13:31:00'),
('N2W5H8C4', '2605-P1V5J3X6', 'P123456789', N'Mark Tsai', '1998-03-15', 'M', 'mark.tsai@email.com', '0912000015', N'Taipei City', 'TW', N'Taipei City', N'Taipei City', N'TECHNICIAN', N'Tech Co.', 5, 'SAVINGS', 'SALARY', 'TW', 1, '/uploads/mock/customer/N2W5H8C4-front.jpg', '/uploads/mock/customer/N2W5H8C4-back.jpg', '/uploads/mock/customer/N2W5H8C4-second.jpg', '/avatars/mock/N2W5H8C4.png', N'Technician', 1020000, 'HIGH', 'ACTIVE', '2026-04-16 14:38:00', '2026-04-16 14:38:00');

INSERT INTO CUSTOMER_AUTH (
    auth_id, customer_id, username, password_hash, role, status,
    last_login_date, verification_token, created_at, updated_at
)
VALUES
('AUTH26050001', 'Q8M4T7K2', 'cust0001', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-01-02 09:00:00', '2026-01-02 09:00:00'),
('AUTH26050002', 'R5N9W3A6', 'cust0002', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-02-03 10:07:00', '2026-02-03 10:07:00'),
('AUTH26050003', 'H7C2P8D4', 'cust0003', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-03-04 11:14:00', '2026-03-04 11:14:00'),
('AUTH26050004', 'V6J3X9M5', 'cust0004', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-04-05 12:21:00', '2026-04-05 12:21:00'),
('AUTH26050005', 'K2T8B4R7', 'cust0005', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-05-06 13:28:00', '2026-05-06 13:28:00'),
('AUTH26050006', 'M9A5D2Q8', 'cust0006', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-01-07 14:35:00', '2026-01-07 14:35:00'),
('AUTH26050007', 'P4W7N6C3', 'cust0007', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-02-08 15:42:00', '2026-02-08 15:42:00'),
('AUTH26050008', 'T8H2K5V9', 'cust0008', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-03-09 16:49:00', '2026-03-09 16:49:00'),
('AUTH26050009', 'A6R3M8J2', 'cust0009', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-04-10 17:56:00', '2026-04-10 17:56:00'),
('AUTH26050010', 'D5Q9T2W7', 'cust0010', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-05-11 09:03:00', '2026-05-11 09:03:00'),
('AUTH26050011', 'F7V4C8N2', 'cust0011', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-01-12 10:10:00', '2026-01-12 10:10:00'),
('AUTH26050012', 'G3K6P9M5', 'cust0012', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-02-13 11:17:00', '2026-02-13 11:17:00'),
('AUTH26050013', 'J8R2D5A7', 'cust0013', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-03-14 12:24:00', '2026-03-14 12:24:00'),
('AUTH26050014', 'L4N9T6Q3', 'cust0014', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-04-15 13:31:00', '2026-04-15 13:31:00'),
('AUTH26050015', 'N2W5H8C4', 'cust0015', @pwd_hash, 'CUSTOMER', 'ACTIVE', '2026-05-18 09:00:00', NULL, '2026-04-16 14:38:00', '2026-04-16 14:38:00');

INSERT INTO CUSTOMER_LOGIN_LOG (
    customer_id, username, result, fail_reason, ip_address, user_agent, device_name, login_time
)
VALUES
('Q8M4T7K2', 'cust0001', N'SUCCESS', NULL, '203.0.113.11', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:00:00'),
('R5N9W3A6', 'cust0002', N'SUCCESS', NULL, '203.0.113.12', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:01:00'),
('H7C2P8D4', 'cust0003', N'SUCCESS', NULL, '203.0.113.13', N'Mozilla/5.0', N'Safari / iOS', '2026-05-18 10:02:00'),
('V6J3X9M5', 'cust0004', N'SUCCESS', NULL, '203.0.113.14', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:03:00'),
('K2T8B4R7', 'cust0005', N'SUCCESS', NULL, '203.0.113.15', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:04:00'),
('M9A5D2Q8', 'cust0006', N'SUCCESS', NULL, '203.0.113.16', N'Mozilla/5.0', N'Safari / iOS', '2026-05-18 10:05:00'),
('P4W7N6C3', 'cust0007', N'SUCCESS', NULL, '203.0.113.17', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:06:00'),
('T8H2K5V9', 'cust0008', N'SUCCESS', NULL, '203.0.113.18', N'Mozilla/5.0', N'Safari / iOS', '2026-05-18 10:07:00'),
('A6R3M8J2', 'cust0009', N'SUCCESS', NULL, '203.0.113.19', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:08:00'),
('D5Q9T2W7', 'cust0010', N'SUCCESS', NULL, '203.0.113.20', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:09:00'),
('F7V4C8N2', 'cust0011', N'SUCCESS', NULL, '203.0.113.21', N'Mozilla/5.0', N'Safari / iOS', '2026-05-18 10:10:00'),
('G3K6P9M5', 'cust0012', N'SUCCESS', NULL, '203.0.113.22', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:11:00'),
('J8R2D5A7', 'cust0013', N'SUCCESS', NULL, '203.0.113.23', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:12:00'),
('L4N9T6Q3', 'cust0014', N'SUCCESS', NULL, '203.0.113.24', N'Mozilla/5.0', N'Safari / iOS', '2026-05-18 10:13:00'),
('N2W5H8C4', 'cust0015', N'SUCCESS', NULL, '203.0.113.25', N'Mozilla/5.0', N'Chrome / macOS', '2026-05-18 10:14:00');

INSERT INTO CUSTOMER_DEVICE (
    customer_id, device_fingerprint, device_name, browser_name, operating_system,
    ip_address, user_agent, status, trusted, first_seen_at, last_seen_at, revoked_at
)
VALUES
('Q8M4T7K2', 'mock-fingerprint-0001', N'Demo laptop', N'Chrome', N'macOS', '203.0.113.11', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:00:00', NULL),
('R5N9W3A6', 'mock-fingerprint-0002', N'Demo phone', N'Chrome', N'Android', '203.0.113.12', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:01:00', NULL),
('H7C2P8D4', 'mock-fingerprint-0003', N'Demo laptop', N'Safari', N'iOS', '203.0.113.13', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:02:00', NULL),
('V6J3X9M5', 'mock-fingerprint-0004', N'Demo laptop', N'Chrome', N'macOS', '203.0.113.14', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:03:00', NULL),
('K2T8B4R7', 'mock-fingerprint-0005', N'Demo laptop', N'Chrome', N'Windows', '203.0.113.15', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:04:00', NULL),
('M9A5D2Q8', 'mock-fingerprint-0006', N'Demo phone', N'Safari', N'iOS', '203.0.113.16', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:05:00', NULL),
('P4W7N6C3', 'mock-fingerprint-0007', N'Demo laptop', N'Chrome', N'macOS', '203.0.113.17', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:06:00', NULL),
('T8H2K5V9', 'mock-fingerprint-0008', N'Demo phone', N'Safari', N'iOS', '203.0.113.18', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:07:00', NULL),
('A6R3M8J2', 'mock-fingerprint-0009', N'Demo laptop', N'Chrome', N'Windows', '203.0.113.19', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:08:00', NULL),
('D5Q9T2W7', 'mock-fingerprint-0010', N'Demo laptop', N'Chrome', N'macOS', '203.0.113.20', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:09:00', NULL),
('F7V4C8N2', 'mock-fingerprint-0011', N'Demo phone', N'Safari', N'iOS', '203.0.113.21', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:10:00', NULL),
('G3K6P9M5', 'mock-fingerprint-0012', N'Demo laptop', N'Chrome', N'Windows', '203.0.113.22', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:11:00', NULL),
('J8R2D5A7', 'mock-fingerprint-0013', N'Demo laptop', N'Chrome', N'macOS', '203.0.113.23', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:12:00', NULL),
('L4N9T6Q3', 'mock-fingerprint-0014', N'Demo phone', N'Safari', N'iOS', '203.0.113.24', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:13:00', NULL),
('N2W5H8C4', 'mock-fingerprint-0015', N'Demo laptop', N'Chrome', N'Windows', '203.0.113.25', N'Mozilla/5.0', 'ACTIVE', 1, '2026-05-01 09:00:00', '2026-05-18 10:14:00', NULL);

INSERT INTO CUSTOMER_KYC (
    customer_id, id_issue_date, id_issue_location, id_issue_type, tax_residency,
    marital_status, education_level, occupation_category, company_name, annual_income,
    source_of_wealth, updated_at
)
VALUES
('Q8M4T7K2', '2015-01-01', N'Taipei', N'ID_CARD', 'TW', 'S', N'Bachelor', N'CLERICAL', N'Java Easy Bank', 433534, N'Salary', '2026-05-18 09:00:00'),
('R5N9W3A6', '2014-01-01', N'Taipei', N'ID_CARD', 'TW', 'M', N'Bachelor', N'PROFESSIONAL', N'Consulting', 1140000, N'Salary', '2026-05-18 09:00:00'),
('H7C2P8D4', '2013-01-01', N'Kaohsiung', N'ID_CARD', 'TW', 'S', N'College', N'TECHNICIAN', N'Tech Co.', 900000, N'Salary', '2026-05-18 09:00:00'),
('V6J3X9M5', '2012-01-01', N'Taichung', N'ID_CARD', 'TW', 'S', N'College', N'OTHER', N'Freelance', 1080000, N'Salary', '2026-05-18 09:00:00'),
('K2T8B4R7', '2011-01-01', N'New Taipei', N'ID_CARD', 'TW', 'M', N'College', N'CLERICAL', N'Office', 960000, N'Salary', '2026-05-18 09:00:00'),
('M9A5D2Q8', '2010-01-01', N'Yilan', N'ID_CARD', 'TW', 'S', N'High School', N'SERVICE_SALES', N'Sales', 540000, N'Salary', '2026-05-18 09:00:00'),
('P4W7N6C3', '2015-01-01', N'Hsinchu', N'ID_CARD', 'TW', 'M', N'Bachelor', N'PROFESSIONAL', N'Bank', 880000, N'Salary', '2026-05-18 09:00:00'),
('T8H2K5V9', '2015-01-01', N'Chiayi', N'ID_CARD', 'TW', 'S', N'Master', N'LEGISLATOR_MANAGER', N'Company', 1320000, N'Business', '2026-05-18 09:00:00'),
('A6R3M8J2', '2015-01-01', N'Changhua', N'ID_CARD', 'TW', 'S', N'Bachelor', N'CLERICAL', N'Office', 940000, N'Salary', '2026-05-18 09:00:00'),
('D5Q9T2W7', '2015-01-01', N'Tainan', N'ID_CARD', 'TW', 'M', N'High School', N'SERVICE_SALES', N'Food service', 760000, N'Salary', '2026-05-18 09:00:00'),
('F7V4C8N2', '2015-01-01', N'Hualien', N'ID_CARD', 'TW', 'S', N'Bachelor', N'PROFESSIONAL', N'Finance', 1180000, N'Salary', '2026-05-18 09:00:00'),
('G3K6P9M5', '2015-01-01', N'Pingtung', N'ID_CARD', 'TW', 'M', N'Master', N'LEGISLATOR_MANAGER', N'Business', 1380000, N'Business', '2026-05-18 09:00:00'),
('J8R2D5A7', '2015-01-01', N'Yunlin', N'ID_CARD', 'TW', 'S', N'Bachelor', N'CLERICAL', N'Bank', 1080000, N'Salary', '2026-05-18 09:00:00'),
('L4N9T6Q3', '2015-01-01', N'Keelung', N'ID_CARD', 'TW', 'S', N'Bachelor', N'PROFESSIONAL', N'Consulting', 1260000, N'Salary', '2026-05-18 09:00:00'),
('N2W5H8C4', '2015-01-01', N'Taipei', N'ID_CARD', 'TW', 'S', N'College', N'TECHNICIAN', N'Tech Co.', 1020000, N'Salary', '2026-05-18 09:00:00');

INSERT INTO CUSTOMER_RISK_TAG (
    customer_id, aml_risk_level, pep_status, is_fraud_suspect, block_reason,
    kyc_last_review_date, kyc_next_review_date, updated_by, updated_at
)
VALUES
('Q8M4T7K2', 'MEDIUM', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('R5N9W3A6', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('H7C2P8D4', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('V6J3X9M5', 'MEDIUM', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('K2T8B4R7', 'MEDIUM', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('M9A5D2Q8', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('P4W7N6C3', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('T8H2K5V9', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('A6R3M8J2', 'MEDIUM', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('D5Q9T2W7', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('F7V4C8N2', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('G3K6P9M5', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('J8R2D5A7', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('L4N9T6Q3', 'LOW', 'N', 'N', NULL, '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00'),
('N2W5H8C4', 'HIGH', 'N', 'Y', N'Overdue loan test case', '2026-05-18', '2026-11-18', 'SYSTEM', '2026-05-18 09:00:00');
