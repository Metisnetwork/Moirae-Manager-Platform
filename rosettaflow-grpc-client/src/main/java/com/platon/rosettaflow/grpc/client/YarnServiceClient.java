package com.platon.rosettaflow.grpc.client;

import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.service.CommonMessage;
import com.platon.rosettaflow.grpc.service.GetNodeInfoResponse;
import com.platon.rosettaflow.grpc.service.YarnServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @date 2021/7/20
 */
@Slf4j
@Component
public class YarnServiceClient {

    @GrpcClient("carrier-grpc-server")
    private YarnServiceGrpc.YarnServiceBlockingStub yarnServiceBlockingStub;

    public String getNodeInfo() {
        CommonMessage.EmptyGetParams emptyGetParams = CommonMessage.EmptyGetParams.newBuilder().build();
        GetNodeInfoResponse nodeInfo = yarnServiceBlockingStub.getNodeInfo(emptyGetParams);

        if (nodeInfo.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, nodeInfo.getMsg());
        }

        return nodeInfo.getInformation().toString();
    }
}
