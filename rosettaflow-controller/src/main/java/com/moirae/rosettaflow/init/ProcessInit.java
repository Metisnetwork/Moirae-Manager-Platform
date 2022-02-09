package com.moirae.rosettaflow.init;

import com.moirae.rosettaflow.service.NetManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 进程初始化
 */
@Slf4j
@Component
public class ProcessInit {

    @Resource
    private NetManager netManager;

    @PostConstruct
    public void init() {
        // 第一次启动时初始化公共组织
        netManager.initPublicOrg();
        // 初始化组织的连接
        netManager.init();
    }
}
