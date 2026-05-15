package com.javaeasybank.loan.repository;

import com.javaeasybank.loan.entity.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanDocumentRepository extends JpaRepository<LoanDocument, String> {

    // 查某申請的所有文件（依上傳時間升序）
    List<LoanDocument> findByApplicationIdOrderByUploadTimeAsc(String applicationId);

    // 計算某申請已上傳的文件數量（用於數量上限檢查）
    long countByApplicationId(String applicationId);
}
