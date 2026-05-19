SET NOCOUNT ON;

DELETE FROM SCHEDULED_TRANSFER;
DELETE FROM FAVORITE_ACCOUNT;
DELETE FROM TRANS_LOG;
DELETE FROM ACCOUNT_DAILY_SNAPSHOTS;
DELETE FROM ACCOUNT_STATUS_HISTORY;
DELETE FROM [ACCOUNT];

DECLARE @seedTime DATETIME2 = '2026-05-19 09:00:00';

INSERT INTO [ACCOUNT] (
    account_number, customer_id, account_type, currency, balance, liability,
    interest_rate, status, parent_account_number, created_at, changed_at,
    created_by, changed_by
)
VALUES
('070000000001', 'Q8M4T7K2', 'CHECKING', 'TWD', 250000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -120, @seedTime), @seedTime, 'system', 'system'),
('070000000002', 'R5N9W3A6', 'CHECKING', 'TWD', 180000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -119, @seedTime), @seedTime, 'system', 'system'),
('070000000003', 'H7C2P8D4', 'CHECKING', 'TWD', 220000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -118, @seedTime), @seedTime, 'system', 'system'),
('070000000004', 'V6J3X9M5', 'CHECKING', 'TWD', 210000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -117, @seedTime), @seedTime, 'system', 'system'),
('070000000005', 'K2T8B4R7', 'CHECKING', 'TWD', 195000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -116, @seedTime), @seedTime, 'system', 'system'),
('070000000006', 'M9A5D2Q8', 'CHECKING', 'TWD', 175000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -115, @seedTime), @seedTime, 'system', 'system'),
('070000000007', 'P4W7N6C3', 'CHECKING', 'TWD', 260000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -114, @seedTime), @seedTime, 'system', 'system'),
('070000000008', 'T8H2K5V9', 'CHECKING', 'TWD', 310000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -113, @seedTime), @seedTime, 'system', 'system'),
('070000000009', 'A6R3M8J2', 'CHECKING', 'TWD', 205000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -112, @seedTime), @seedTime, 'system', 'system'),
('070000000010', 'D5Q9T2W7', 'CHECKING', 'TWD', 265000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -111, @seedTime), @seedTime, 'system', 'system'),
('070000000011', 'F7V4C8N2', 'CHECKING', 'TWD', 240000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -110, @seedTime), @seedTime, 'system', 'system'),
('070000000012', 'G3K6P9M5', 'CHECKING', 'TWD', 330000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -109, @seedTime), @seedTime, 'system', 'system'),
('070000000013', 'J8R2D5A7', 'CHECKING', 'TWD', 155000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -108, @seedTime), @seedTime, 'system', 'system'),
('070000000014', 'L4N9T6Q3', 'CHECKING', 'TWD', 290000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -107, @seedTime), @seedTime, 'system', 'system'),
('070000000015', 'N2W5H8C4', 'CHECKING', 'TWD', 280000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -106, @seedTime), @seedTime, 'system', 'system'),
('070000000016', 'S7A3K9P6', 'CHECKING', 'TWD', 150000.0000, 0.0000, 0.00500, 'ACTIVE', NULL, DATEADD(DAY, -105, @seedTime), @seedTime, 'system', 'system');

INSERT INTO [ACCOUNT_STATUS_HISTORY] (
    history_id, account_number, old_status, new_status, change_reason, changed_at, changed_by
)
VALUES
('00000000-0000-0000-0000-000000000001', '070000000001', 'ACTIVE', 'ACTIVE', N'Initial seed', @seedTime, 'system'),
('00000000-0000-0000-0000-000000000002', '070000000015', 'ACTIVE', 'ACTIVE', N'Initial seed', @seedTime, 'system');

INSERT INTO [ACCOUNT_DAILY_SNAPSHOTS] (
    snapshot_id, account_number, snapshot_date, balance, interest_rate, daily_interest, created_at
)
VALUES
('10000000-0000-0000-0000-000000000001', '070000000001', '2026-05-18', 250000.0000, 0.00500, 34.2466, @seedTime),
('10000000-0000-0000-0000-000000000002', '070000000015', '2026-05-18', 280000.0000, 0.00500, 38.3950, @seedTime);

INSERT INTO TRANS_LOG (
    transaction_id, reference_id, account_number, counterpart_account, bank_code, bank_name,
    counterpart_bank_code, counterpart_bank_name, is_interbank, entry_type, transaction_type,
    amount, fee_amount, total_debit_amount, balance_before, balance_after, currency, note, created_at
)
VALUES
('20000000-0000-0000-0000-000000000001', 'REF-001', '070000000001', '070000000002', '909', N'Java Easy Bank', NULL, NULL, 0, 'D', 'TRANSFER', 1000.0000, 0.0000, 1000.0000, 250000.0000, 249000.0000, 'TWD', N'Seed transfer', @seedTime),
('20000000-0000-0000-0000-000000000002', 'REF-002', '070000000001', '070000000003', '909', N'Java Easy Bank', NULL, NULL, 0, 'D', 'TRANSFER', 2000.0000, 0.0000, 2000.0000, 249000.0000, 247000.0000, 'TWD', N'Seed transfer', @seedTime);

INSERT INTO FAVORITE_ACCOUNT (customer_id, bank_code, account_number, alias, bank_name, created_at, updated_at)
VALUES
('Q8M4T7K2', '909', '070000000002', N'Amy', N'Java Easy Bank', @seedTime, @seedTime),
('Q8M4T7K2', '909', '909000000002', N'Bank collection', N'Java Easy Bank', @seedTime, @seedTime);

INSERT INTO SCHEDULED_TRANSFER (
    customer_id, from_account_number, to_account_number, amount, scheduled_date,
    note, status, executed_at, fail_reason, created_at, updated_at
)
VALUES
('Q8M4T7K2', '070000000001', '070000000002', 5000.0000, '2026-05-20', N'Rent', 'PENDING', NULL, NULL, @seedTime, @seedTime);
