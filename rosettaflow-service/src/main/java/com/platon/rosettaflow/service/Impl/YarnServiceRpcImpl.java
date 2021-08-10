package com.platon.rosettaflow.service.Impl;

import com.platon.rosettaflow.grpc.client.YarnServiceClient;
import com.platon.rosettaflow.service.IYarnServiceRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/7/15
 */
@Slf4j
@Service
public class YarnServiceRpcImpl implements IYarnServiceRpc {

    @Resource
    private YarnServiceClient yarnServiceClient;

    @Override
    public String getNodeInfo() {
        return yarnServiceClient.getNodeInfo();
    }
}
