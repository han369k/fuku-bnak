package com.javaeasybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JavaEasyBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaEasyBankApplication.class, args);
    }

}
