-- ============================================================
-- Risk Module Mock Data (重新生成版)
-- 200 筆風控事件紀錄
-- ============================================================

-- 清除舊資料
DELETE FROM RISK_EVENT_LOG;

-- 使用 CTE 產生 200 筆風控事件
WITH Numbers AS (
    SELECT TOP 200 ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS n
    FROM master.sys.all_columns a
             CROSS JOIN master.sys.all_columns b
)
INSERT INTO RISK_EVENT_LOG (
    event_type,
    target_identifier,
    risk_level,
    action_taken,
    trigger_reason,
    transaction_amount,
    created_at
)
SELECT
    CASE (n % 4)
        WHEN 0 THEN 'TRANSFER'
        WHEN 1 THEN 'CREDIT_CARD'
        WHEN 2 THEN 'USER_LOGIN'
        ELSE 'LOAN'
        END AS event_type,

    CASE (n % 4)
        WHEN 0 THEN '822' + CAST(100000000 + n AS VARCHAR)
        WHEN 1 THEN '4579-' + CAST(1000 + n AS VARCHAR) + '-0000-1234'
        ELSE 'USER-' + CAST(5000 + n AS VARCHAR)
        END AS target_identifier,

    CASE
        WHEN n % 10 IN (0, 1, 2, 3, 4, 5, 6) THEN 'LOW'
        WHEN n % 10 IN (7, 8) THEN 'MEDIUM'
        ELSE 'HIGH'
END AS risk_level,

    CASE
        WHEN n % 10 IN (0, 1, 2, 3, 4, 5, 6) THEN 'PASSED'
        WHEN n % 10 IN (7, 8) THEN 'MANUAL_REVIEW'
        ELSE 'REJECTED'
END AS action_taken,

    CASE (n % 8)
        WHEN 0 THEN N'系統例行掃描正常'
        WHEN 1 THEN N'偵測到常用裝置'
        WHEN 2 THEN N'交易頻率略高於平均'
        WHEN 3 THEN N'觸發大額預警規則'
        WHEN 4 THEN N'黑名單特徵比對成功'
        WHEN 5 THEN N'跨境交易風險偵測'
        WHEN 6 THEN N'異常時段登入警示'
        ELSE N'多重帳戶關聯偵測'
END AS trigger_reason,

    CASE
        WHEN (n % 4) = 2 THEN NULL
        ELSE CAST((RAND(n) * 100000) AS DECIMAL(18, 2))
END AS transaction_amount,

    DATEADD(SECOND, -n * 4320, GETDATE()) AS created_at
FROM Numbers;

PRINT N'風控事件 Mock 資料寫入完畢（200 筆）。';