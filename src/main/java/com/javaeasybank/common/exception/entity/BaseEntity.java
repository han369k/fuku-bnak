package com.javaeasybank.common.exception.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * [基礎實體類別]
 * 提供所有 Entity 共用欄位：id、createdAt、updatedAt。
 *
 * 規則：
 * - 必須加上 @MappedSuperclass，子類別才能繼承資料庫映射欄位
 * - 所有 Entity 都要 extends BaseEntity
 * - 不要在子類別重複宣告同名屬性，避免 Hibernate 映射衝突
 * - 子類別建議加 @EqualsAndHashCode(callSuper = true) 處理相等判定
 *
 * 設計思維：
 * 雖然程式設計應以 interface oriented 為主，提倡少用繼承多用組合。
 * 但此處為了統一管理審計欄位（Audit Fields）採取繼承做法。
 * 這是犧牲部分耦合度來換取屬性重用性與維護的一致性。
 * 類別設為 abstract 是為了防止被單獨 new 出來，它僅作為結構模板。
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}