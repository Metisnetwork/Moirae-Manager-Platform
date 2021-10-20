package com.platon.rosettaflow;

import com.zengtengpeng.annotation.EnableMQ;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author admin
 * @date 2021/7/20
 */
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.platon.rosettaflow.mapper"})
@EnableMQ
public class RosettaFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(RosettaFlowApplication.class, args);
    }
}
