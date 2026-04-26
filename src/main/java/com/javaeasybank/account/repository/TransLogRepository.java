package com.javaeasybank.account.repository;

import com.javaeasybank.account.entity.TransLog;
import com.javaeasybank.account.enums.EntryType;
import com.javaeasybank.account.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransLogRepository extends JpaRepository<TransLog, String>, JpaSpecificationExecutor<TransLog> {

    /**
     * 依 reference_id 查詢單筆/多筆交易 (例如轉帳會有兩筆同 ref 的紀錄)
     */
    List<TransLog> findByReferenceId(String referenceId);

    /**
     * 依 account_number 查詢 (同時查詢 影響帳號 與 對手方帳號)
     */
    @Query("SELECT t FROM TransLog t WHERE t.accountNumber = :acctNum OR t.counterpartAccount = :acctNum")
    Page<TransLog> findByAccountInvolved(@Param("acctNum") String accountNumber, Pageable pageable);

    /**
     * 依 customer_id 查詢全部紀錄 (跨表 JOIN)
     */
    @Query("SELECT t FROM TransLog t JOIN Account a ON t.accountNumber = a.accountNumber WHERE a.customerId = :customerId")
    Page<TransLog> findByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    /**
     * 依 customer_id + 日期範圍 查詢全部紀錄
     */
    @Query("SELECT t FROM TransLog t JOIN Account a ON t.accountNumber = a.accountNumber " +
           "WHERE a.customerId = :customerId AND t.createdAt >= :startDate AND t.createdAt <= :endDate")
    Page<TransLog> findByCustomerIdAndDateRange(
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    /**
     * [風險監控 B1] 短時間高頻交易偵測
     * 統計指定時間後，某帳戶作為「發起方」(轉出)的交易次數。
     */
    @Query("SELECT COUNT(t) FROM TransLog t " +
           "WHERE t.accountNumber = :accountNumber " +
           "AND t.entryType = :entryType " +
           "AND t.transactionType = :transactionType " +
           "AND t.createdAt > :sinceTime")
    int countRecentTransactions(
            @Param("accountNumber") String accountNumber,
            @Param("entryType") EntryType entryType,
            @Param("transactionType") TransactionType transactionType,
            @Param("sinceTime") LocalDateTime sinceTime);

    /**
     * [風險監控 B2] 24 小時累積金額偵測
     * 統計指定時間後，某帳戶作為「轉出方」(轉帳或提款)的累積金額。
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransLog t " +
           "WHERE t.accountNumber = :accountNumber " +
           "AND t.entryType = :entryType " +
           "AND t.transactionType IN :transactionTypes " +
           "AND t.createdAt > :sinceTime")
    BigDecimal sumRecentOutflow(
            @Param("accountNumber") String accountNumber,
            @Param("entryType") EntryType entryType,
            @Param("transactionTypes") List<TransactionType> transactionTypes,
            @Param("sinceTime") LocalDateTime sinceTime);
}
