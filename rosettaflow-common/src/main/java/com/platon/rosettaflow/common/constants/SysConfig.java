package com.platon.rosettaflow.common.constants;

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
    private long loginTimeOut = 1800000;
    private boolean kickMode = true;
    private boolean masterNode = false;
}
