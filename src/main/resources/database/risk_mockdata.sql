SET NOCOUNT ON;

-- Risk event log demo data
INSERT INTO RISK_EVENT_LOG (
    event_type,
    business_id,
    target_identifier,
    risk_level,
    disposition,
    trigger_reason,
    meta_data,
    transaction_amount,
    callback_url,
    created_at
)
VALUES
    ('LOAN_APPLY', 'Q8M4T7K2-LA-001', 'Q8M4T7K2', 'LOW', 'PASS', N'Initial loan application', N'{"module":"loan","scene":"apply"}', NULL, NULL, '2026-05-01T09:00:00'),
    ('LOAN_REVIEW', 'Q8M4T7K2-LA-002', 'Q8M4T7K2', 'MEDIUM', 'MANUAL_REVIEW', N'Missing income proof', N'{"module":"loan","scene":"review"}', NULL, NULL, '2026-05-01T10:00:00'),
    ('TRANSFER', 'TXN-20260501001', '070000000001', 'LOW', 'PASS', N'Normal fund transfer', N'{"module":"payment"}', 2500.0000, NULL, '2026-05-01T11:00:00'),
    ('LOGIN', 'AUTH-20260501001', 'customer001@java-bank.demo', 'LOW', 'PASS', N'Normal login', N'{"module":"auth"}', NULL, NULL, '2026-05-01T12:00:00'),
    ('CUSTOMER_CREATE', 'CUST-20260501001', 'R5N9W3A6', 'LOW', 'PASS', N'Customer onboarding completed', N'{"module":"customer"}', NULL, NULL, '2026-05-02T09:30:00'),
    ('CARD_APPLICATION', 'CARD-20260501001', 'Q8M4T7K2', 'MEDIUM', 'RETURN', N'Need additional address proof', N'{"module":"card"}', NULL, NULL, '2026-05-02T10:30:00'),
    ('LOAN_APPLY', 'LAC202605181014223798', 'LAC202605181014223798', 'HIGH', 'MANUAL_REVIEW', N'Loan account test case for overdue flow', N'{"module":"loan","account":"LAC202605181014223798"}', NULL, NULL, '2026-05-03T09:00:00'),
    ('TRANSFER', 'TXN-20260503001', '070000000002', 'MEDIUM', 'PASS', N'Large but explainable transfer', N'{"module":"payment"}', 180000.0000, NULL, '2026-05-03T13:00:00'),
    ('LOGIN', 'AUTH-20260503001', 'customer015@java-bank.demo', 'MEDIUM', 'PASS', N'VPN login from known device', N'{"module":"auth"}', NULL, NULL, '2026-05-03T14:00:00'),
    ('LOAN_REVIEW', 'Q8M4T7K2-LA-003', 'Q8M4T7K2', 'HIGH', 'REJECT', N'Negative credit trend', N'{"module":"loan","scene":"review"}', NULL, NULL, '2026-05-04T09:00:00');

-- Review tasks linked to the above events
INSERT INTO REVIEW_TASK (
    log_id,
    business_id,
    scene,
    status,
    substatus,
    review_result,
    assignee,
    admin_comment,
    attachments,
    required_documents_json,
    priority,
    create_at,
    processed_at,
    version
)
SELECT log_id, business_id, 'LOAN', 'PENDING', NULL, NULL, NULL, N'Awaiting manual review',
       N'[]',
       N'["ID_FRONT","INCOME_PROOF"]',
       2, created_at, NULL, 0
FROM RISK_EVENT_LOG
WHERE business_id = 'Q8M4T7K2-LA-002';

INSERT INTO REVIEW_TASK (
    log_id,
    business_id,
    scene,
    status,
    substatus,
    review_result,
    assignee,
    admin_comment,
    attachments,
    required_documents_json,
    priority,
    create_at,
    processed_at,
    version
)
SELECT log_id, business_id, 'LOAN', 'PROCESSING', 'UNDER_REVIEW', NULL, 'reviewer01', N'Collected the missing document',
       N'["income.pdf"]',
       N'["INCOME_PROOF"]',
       3, created_at, NULL, 0
FROM RISK_EVENT_LOG
WHERE business_id = 'LAC202605181014223798';

INSERT INTO REVIEW_TASK (
    log_id,
    business_id,
    scene,
    status,
    substatus,
    review_result,
    assignee,
    admin_comment,
    attachments,
    required_documents_json,
    priority,
    create_at,
    processed_at,
    version
)
SELECT log_id, business_id, 'CARD', 'COMPLETED', 'APPROVED', 'APPROVED', 'reviewer02', N'Approved for issuance',
       N'["id_front.jpg","id_back.jpg"]',
       N'["ID_FRONT","ID_BACK"]',
       1, created_at, created_at, 1
FROM RISK_EVENT_LOG
WHERE business_id = 'CARD-20260501001';

INSERT INTO REVIEW_TASK (
    log_id,
    business_id,
    scene,
    status,
    substatus,
    review_result,
    assignee,
    admin_comment,
    attachments,
    required_documents_json,
    priority,
    create_at,
    processed_at,
    version
)
SELECT log_id, business_id, 'LOAN', 'COMPLETED', 'REJECTED', 'REJECTED', 'reviewer03', N'Rejected due to risk indicators',
       N'[]',
       N'["INCOME_PROOF","BANK_STATEMENT"]',
       3, created_at, created_at, 1
FROM RISK_EVENT_LOG
WHERE business_id = 'Q8M4T7K2-LA-003';

-- Black list seed
INSERT INTO BLACK_LIST (list_type, list_value, source, reason, status, expires_at, created_at, updated_at)
VALUES
    ('ID_NUMBER', 'Z999999999', 'manual', N'Confirmed fraud case', 1, NULL, '2026-04-01T09:00:00', '2026-04-01T09:00:00'),
    ('PHONE', '0988000123', 'manual', N'Repeated chargeback disputes', 1, NULL, '2026-04-02T09:00:00', '2026-04-02T09:00:00'),
    ('ACCOUNT_NUMBER', '070000009999', 'risk-engine', N'Linked to suspicious transfers', 1, NULL, '2026-04-03T09:00:00', '2026-04-03T09:00:00');

-- Customer credit info seed
INSERT INTO customer_credit_info (
    customer_id,
    annual_income,
    occupation,
    job,
    external_score,
    other_bank_debt,
    has_real_estate,
    fund_source,
    is_pep,
    final_score,
    risk_level,
    last_updated_at
)
VALUES
    ('Q8M4T7K2', 433534.00, 'CLERICAL', N'Office staff', 717, 225437.00, 0, 'SAVINGS', 0, 68, 'MEDIUM', '2026-05-01T09:00:00'),
    ('R5N9W3A6', 1140000.00, 'PROFESSIONAL', N'Engineer', 726, 439442.93, 1, 'SAVINGS', 0, 80, 'LOW', '2026-05-01T09:00:00'),
    ('H7C2P8D4', 900000.00, 'TECHNICIAN', N'Technician', 680, 257354.96, 1, 'SALARY', 0, 73, 'LOW', '2026-05-01T09:00:00'),
    ('V6J3X9M5', 1080000.00, 'OTHER', N'Freelancer', 497, 404440.63, 0, 'SALARY', 0, 51, 'MEDIUM', '2026-05-01T09:00:00'),
    ('K2T8B4R7', 960000.00, 'CLERICAL', N'Clerk', 607, 422309.72, 0, 'SAVINGS', 0, 58, 'MEDIUM', '2026-05-01T09:00:00'),
    ('M9A5D2Q8', 540000.00, 'SERVICE_SALES', N'Sales associate', 551, 162384.18, 1, 'SALARY', 0, 60, 'MEDIUM', '2026-05-01T09:00:00'),
    ('P4W7N6C3', 880000.00, 'CLERICAL', N'Back office', 625, 80669.15, 0, 'SAVINGS', 0, 69, 'MEDIUM', '2026-05-01T09:00:00'),
    ('T8H2K5V9', 1320000.00, 'LEGISLATOR_MANAGER', N'Business owner', 713, 1314847.22, 0, 'BUSINESS_INCOME', 0, 61, 'MEDIUM', '2026-05-01T09:00:00'),
    ('A6R3M8J2', 940000.00, 'SERVICE_SALES', N'Retail manager', 618, 316148.77, 1, 'SAVINGS', 0, 65, 'MEDIUM', '2026-05-01T09:00:00'),
    ('L4N9T6Q3', 1260000.00, 'PROFESSIONAL', N'Consultant', 722, 293944.09, 0, 'SAVINGS', 0, 80, 'LOW', '2026-05-01T09:00:00');
