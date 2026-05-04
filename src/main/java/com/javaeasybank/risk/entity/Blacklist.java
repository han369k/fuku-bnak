package com.javaeasybank.risk.entity;

import com.javaeasybank.risk.core.enums.BlacklistType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
<<<<<<< HEAD
=======
import org.springframework.data.annotation.CreatedDate;
>>>>>>> main

import java.time.LocalDateTime;

@Entity
<<<<<<< HEAD
@Table(name = "[BlackList]")
=======
@Table(name = "black_list", indexes = {
        @Index(name = "idx_bl_lookup", columnList = "list_type, list_value")
})
>>>>>>> main
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
<<<<<<< HEAD

    @Enumerated(EnumType.STRING)
    @Column(name = "[list_type]", nullable = false, length = 20)
    private BlacklistType listType;

    @Column(name = "[list_value]", nullable = false, length = 100)
    private String listValue;

    @Column(name = "[source]", length = 50)
    private String source;

    @Column(name = "[reason]")
    private String reason;

    @Column(name = "[status]", nullable = false)
    private Boolean status = Boolean.TRUE;

    @Column(name = "[expires_at]")
    private LocalDateTime expireAt;

    @Column(name = "[created_at]")
    private LocalDateTime createdAt;
=======
    //黑名單類型（如：EMAIL, PHONE, IP）
    @Enumerated(EnumType.STRING)
    @Column(name = "list_type", nullable = false, length = 20)
    private BlacklistType listType;
    //具體的黑名單值
    @Column(name = "list_value", nullable = false, length = 100)
    private String listValue;
    //資料來源
    @Column(name = "source", length = 50)
    private String source;

    @Column(name = "reason")
    private String reason;
    //是否啟用
    @Column(name = "status", nullable = false)
    private Boolean status = Boolean.TRUE;
    //解封時間 設null表示永久
    @Column(name = "expires_at")
    private LocalDateTime expireAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
>>>>>>> main
}
