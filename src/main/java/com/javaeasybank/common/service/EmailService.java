package com.javaeasybank.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Async
    public void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("Email sent to {}: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    public void sendVerificationEmail(String to, String token) {
        String link = frontendUrl + "/verify-email?token=" + token;
        String content = "親愛的客戶您好：\n\n" +
                "感謝您註冊 Java Easy Bank！請點擊以下連結以驗證您的電子郵件地址：\n\n" +
                link + "\n\n" +
                "如果您並未執行此操作，請忽略此郵件。\n\n" +
                "Java Easy Bank 敬上";
        sendEmail(to, "Java Easy Bank - 電子郵件驗證", content);
    }

    public void sendLoginNotification(String to, String username, boolean success, String ipAddress) {
        String time = LocalDateTime.now().format(formatter);
        String status = success ? "成功" : "失敗";
        String content = "親愛的客戶您好：\n\n" +
                "您的帳號 [" + username + "] 於 " + time + " 進行了登入嘗試。\n" +
                "登入結果：" + status + "\n" +
                "登入位置：" + ipAddress + "\n\n" +
                (success ? "如果您並未執行此操作，請立即聯絡客服或變更密碼。" : "如果您多次嘗試登入失敗，請確認您的密碼或使用忘記密碼功能。") + "\n\n" +
                "Java Easy Bank 敬上";
        sendEmail(to, "Java Easy Bank - 登入通知", content);
    }

    public void sendTransferNotification(String to, String fromAccount, String toAccount, BigDecimal amount, String currency, String referenceId) {
        String time = LocalDateTime.now().format(formatter);
        String content = "親愛的客戶您好：\n\n" +
                "您的帳戶已成功執行一筆轉帳交易。\n" +
                "交易時間：" + time + "\n" +
                "交易編號：" + referenceId + "\n" +
                "轉出帳號：" + fromAccount + "\n" +
                "轉入帳號：" + toAccount + "\n" +
                "交易金額：" + amount + " " + currency + "\n\n" +
                "感謝您使用 Java Easy Bank。\n\n" +
                "Java Easy Bank 敬上";
        sendEmail(to, "Java Easy Bank - 轉帳交易通知", content);
    }

    public void sendPasswordResetEmail(String to, String link) {
        String content = "親愛的客戶您好：\n\n" +
                "我們收到了您的密碼重設請求。請點擊以下連結以重設您的密碼：\n\n" +
                link + "\n\n" +
                "※ 此連結將於 30 分鐘後失效。\n" +
                "如果您並未發送此請求，請忽略這封信件。\n\n" +
                "Java Easy Bank 敬上";
        sendEmail(to, "Java Easy Bank - 密碼重設連結", content);
    }
}
