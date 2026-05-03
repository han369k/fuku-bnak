-- =============================================
-- 重構：RiskEventLog 統一蛇形命名 (snake_case)
-- 適用環境：SQL Server (SSMS)
-- =============================================

CREATE TABLE [dbo].[RISK_EVENT_LOG] (
                                        [log_id]             BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                                        [event_type]         NVARCHAR(50)         NOT NULL,
                                        [target_identifier]  NVARCHAR(100)        NOT NULL,
                                        [risk_level]         NVARCHAR(20)         NOT NULL,
                                        [action_taken]       NVARCHAR(50)         NOT NULL,
                                        [trigger_reason]     NVARCHAR(500)        NULL,
                                        [transaction_amount] DECIMAL(18,4)        NULL,
                                        [created_at]         DATETIME2            NOT NULL DEFAULT GETDATE()
);

-- 建立索引 (Index)
CREATE INDEX IX_RiskEvent_EventType ON RISK_EVENT_LOG(risk_level);
CREATE INDEX IX_RiskEvent_CreatedAt ON RISK_EVENT_LOG(created_at DESC);