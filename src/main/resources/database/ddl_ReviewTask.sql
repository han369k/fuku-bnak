IF OBJECT_ID('review_task', 'U') IS NOT NULL
DROP TABLE review_task;
GO

CREATE TABLE review_task
(
    task_id       BIGINT       IDENTITY(1,1),
    log_id        BIGINT       NOT NULL,
    business_id   VARCHAR(64)  NOT NULL,
    scene         VARCHAR(32)  NULL,
    status        VARCHAR(50)  NULL, -- 255 有點大，通常狀態代碼 50 綽綽有餘
    review_result VARCHAR(20)  NULL,
    assignee      VARCHAR(255) NULL,
    admin_comment VARCHAR(255) NULL,
    priority      INT          DEFAULT 0,
    create_at     DATETIME     DEFAULT GETDATE(), -- 自動記錄建立時間
    processed_at  DATETIME     NULL,
    version       BIGINT       DEFAULT 0,
    CONSTRAINT pk_review_task PRIMARY KEY (task_id)
);

-- 建立外鍵
ALTER TABLE review_task
    ADD CONSTRAINT FK_REVIEW_TASK_ON_LOG FOREIGN KEY (log_id) REFERENCES risk_event_log (log_id);
