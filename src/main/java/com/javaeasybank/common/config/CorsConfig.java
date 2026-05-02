package com.javaeasybank.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 跨來源請求設定。
 *
 * 開發時 Vue 跑在 localhost:5173，Spring Boot 跑在 localhost:8080，
 * 兩個不同的 port 就是不同的「來源」，瀏覽器預設會擋住跨來源請求。
 * 這個設定告訴瀏覽器：「允許來自 Vue 的請求通過。」
 *
 * 禁止組員修改。
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // 允許來自 Vue 開發伺服器的請求
                .allowedOrigins("http://localhost:5173")
                // 允許的 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                // 允許所有 headers
                .allowedHeaders("*")
                // 允許攜帶 Cookie（Session 需要這個）
                .allowCredentials(true);
    }
}