package com.moirae.rosettaflow.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步执行配置类
 * @author houz
 */
@Slf4j
@Configuration
public class AsyncConfig {

    @Bean
    public Executor asyncExecutor() {
        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(5);
        // 配置最大线程数
        executor.setMaxPoolSize(10);
        // 配置队列大小
        executor.setQueueCapacity(100);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-service-");
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // DiscardPolicy：直接丢弃任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
