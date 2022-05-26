package com.datum.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author admin
 * @date 2021/7/20
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@MapperScan(basePackages = {"com.datum.platform.mapper"})
public class DatumPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatumPlatformApplication.class, args);
    }
}
