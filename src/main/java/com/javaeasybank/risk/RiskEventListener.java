package com.javaeasybank.risk;

import com.javaeasybank.risk.dto.RiskManualReviewEvent;
import com.javaeasybank.risk.entity.RiskEventLog;
import com.javaeasybank.risk.repository.ReviewTaskRepository;
import com.javaeasybank.risk.repository.RiskEventLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RiskEventListener {

    private final RiskEventLogRepository logRepository;
    private final ReviewTaskRepository taskRepository;

    public RiskEventListener(RiskEventLogRepository logRepository, ReviewTaskRepository taskRepository) {
        this.logRepository = logRepository;
        this.taskRepository = taskRepository;
    }

    @EventListener
    public void handleRiskManualReview(RiskManualReviewEvent event) {
        log.info("接收到風控事件，準備建立任務：{}", event.scene());

        // 1. 寫入歷史 Log (靜態紀錄)
        RiskEventLog eventLog = new RiskEventLog();
        eventLog.setEventType(event.scene().name());
        eventLog.setTargetIdentifier(event.businessId());
        eventLog.setActionTaken(event.disposition());
        eventLog.setTriggerReason(event.reason());
        // 這裡可以把 metadata 轉成 JSON 存入
        logRepository.save(eventLog);

//        // 2. 建立審核任務 (動態工作流)
//        RiskReviewTask task = new RiskReviewTask();
//        task.setEventLog(eventLog);
//        task.setBusinessId(event.businessId());
//        task.setStatus(TaskStatus.PENDING); // 初始狀態：待處理
//        taskRepository.save(task);
//
//        log.info("已成功建立人工審核任務，ID: {}", task.getTaskId());
    }
}
