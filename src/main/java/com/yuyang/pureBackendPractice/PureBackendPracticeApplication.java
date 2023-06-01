package com.yuyang.pureBackendPractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
public class PureBackendPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PureBackendPracticeApplication.class, args);
    }

}
