package com.moirae.rosettaflow.common.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
    private long nonceTimeOut = 5 * 60 * 1000;
    private List<OrgConfig> publicOrgList = new ArrayList<>();
    private long defaultPsi = 1001L;

    @Data
    public static class OrgConfig {
        private String identity;
        private String host;
        private int port;
    }
}
