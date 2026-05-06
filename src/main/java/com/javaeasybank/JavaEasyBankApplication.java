package com.javaeasybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class JavaEasyBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaEasyBankApplication.class, args);
    }

}
