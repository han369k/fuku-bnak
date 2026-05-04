-- 使用 CTE 產生大量數字，進而轉換為隨機資料
WITH Numbers AS (
    SELECT TOP 100 ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS n
    FROM master.sys.all_columns a
             CROSS JOIN master.sys.all_columns b
)
-- 修正：表名與欄位名統一改為小寫蛇形
INSERT INTO risk_event_log (
    event_type,
    target_identifier,
    risk_level,
    action_taken,
    trigger_reason,
    transaction_amount,
    created_at
)
SELECT
    -- 隨機分配事件類型
    CASE (n % 4)
        WHEN 0 THEN 'TRANSFER'
        WHEN 1 THEN 'CREDIT_CARD'
        WHEN 2 THEN 'USER_LOGIN'
        ELSE 'LOAN'
        END AS event_type,

    -- 根據類型產生隨機識別碼
    CASE (n % 4)
        WHEN 0 THEN '822' + CAST(100000000 + n AS VARCHAR)
        WHEN 1 THEN '4579-' + CAST(1000 + n AS VARCHAR) + '-0000-1234'
        ELSE 'USER-' + CAST(5000 + n AS VARCHAR)
        END AS target_identifier,

    -- 模擬風險分佈
    CASE
        WHEN n % 10 IN (0, 1, 2, 3, 4, 5, 6) THEN 'LOW'
        WHEN n % 10 IN (7, 8) THEN 'MEDIUM'
        ELSE 'HIGH'
END AS risk_level,

    -- 根據風險決定處置
    CASE
        WHEN n % 10 IN (0, 1, 2, 3, 4, 5, 6) THEN 'PASSED'
        WHEN n % 10 IN (7, 8) THEN 'MANUAL_REVIEW'
        ELSE 'REJECTED'
END AS action_taken,

    -- 隨機填充原因
    CASE (n % 5)
        WHEN 0 THEN N'系統例行掃描正常'
        WHEN 1 THEN N'偵測到常用裝置'
        WHEN 2 THEN N'交易頻率略高於平均'
        WHEN 3 THEN N'觸發大額預警規則'
        ELSE N'黑名單特徵比對成功'
END AS trigger_reason,

    -- 隨機金額
    CASE
        WHEN (n % 4) = 2 THEN NULL
        ELSE CAST((RAND(n) * 100000) AS DECIMAL(18, 2))
END AS transaction_amount,

    -- 分散時間
    DATEADD(SECOND, -n * 6048, GETDATE()) AS created_at
FROM Numbers;