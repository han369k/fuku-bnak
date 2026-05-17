SET NOCOUNT ON;

;WITH Numbers AS (
    SELECT TOP 200 ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS n
    FROM master.sys.all_columns a
    CROSS JOIN master.sys.all_columns b
)
INSERT INTO risk_event_log (
    event_type,
    business_id,
    target_identifier,
    risk_level,
    disposition,
    trigger_reason,
    transaction_amount,
    created_at
)
SELECT
    CASE (n % 6)
        WHEN 0 THEN 'TRANSFER'
        WHEN 1 THEN 'LOGIN'
        WHEN 2 THEN 'LOAN_APPLY'
        WHEN 3 THEN 'CREATE_CUSTOMER'
        WHEN 4 THEN 'UPDATE_CUSTOMER'
        ELSE 'GENERAL'
    END AS event_type,
    CASE (n % 3)
        WHEN 0 THEN 'TXN-' + CAST(2026000 + n AS varchar)
        WHEN 1 THEN 'CUST-' + CAST(9000 + n AS varchar)
        ELSE 'APPLY-' + CAST(8000 + n AS varchar)
    END AS business_id,
    CASE (n % 4)
        WHEN 0 THEN '822' + CAST(100000000 + n AS varchar)
        WHEN 1 THEN '4579-' + CAST(1000 + n AS varchar) + '-0000-1234'
        ELSE 'USER-' + CAST(5000 + n AS varchar)
    END AS target_identifier,
    CASE
        WHEN n % 15 = 0 THEN 'SUSPENDED'
        WHEN n % 10 IN (0, 1, 2, 3, 4, 5) THEN 'LOW'
        WHEN n % 10 IN (6, 7, 8) THEN 'MEDIUM'
        ELSE 'HIGH'
    END AS risk_level,
    CASE
        WHEN n % 10 IN (0, 1, 2, 3, 4, 5, 6) THEN 'PASS'
        WHEN n % 10 IN (7, 8) THEN 'MANUAL_REVIEW'
        ELSE 'REJECT'
    END AS disposition,
    CASE (n % 8)
        WHEN 0 THEN N'High transaction amount'
        WHEN 1 THEN N'Frequent account activity'
        WHEN 2 THEN N'Unusual login pattern'
        WHEN 3 THEN N'Blacklist watch signal'
        WHEN 4 THEN N'Loan application review'
        WHEN 5 THEN N'Customer profile update review'
        WHEN 6 THEN N'Manual review sampling'
        ELSE N'General risk review'
    END AS trigger_reason,
    CASE
        WHEN (n % 6) = 1 THEN NULL
        ELSE CAST((RAND(n) * 100000) AS decimal(18, 4))
    END AS transaction_amount,
    DATEADD(SECOND, -n * 4320, GETDATE()) AS created_at
FROM Numbers;

INSERT INTO customer_credit_info
(
    customer_id,
    annual_income,
    occupation,
    external_score,
    other_bank_debt,
    has_real_estate,
    fund_source,
    final_score,
    risk_level,
    job,
    last_updated_at
)
VALUES
(
    'Q8M4T7K2',
    433534.00,
    'CLERICAL',
    717,
    225437.00,
    0,
    'SALARY',
    68,
    'MEDIUM',
    N'資訊科技業',
    '2026-05-14 12:42:42.377'
);

PRINT N'risk_mockdata.sql completed: risk events and customer_credit_info seeded.';

SET NOCOUNT OFF;
GO
