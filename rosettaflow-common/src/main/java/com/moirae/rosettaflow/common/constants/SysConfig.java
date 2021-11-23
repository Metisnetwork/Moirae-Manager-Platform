package com.moirae.rosettaflow.common.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @date 2021/7/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "system.config")
public class SysConfig {
    private long loginTimeOut = 1800000000;
    private boolean kickMode = true;
    private long nonceTimeOut = 5 * 60 * 1000;
    private String algorithmFilepath = "classpath:script/algorithm.csv";
    private int batchSize = 500;
    private long redisTimeOut = 24 * 60 * 60 * 1000;
}
