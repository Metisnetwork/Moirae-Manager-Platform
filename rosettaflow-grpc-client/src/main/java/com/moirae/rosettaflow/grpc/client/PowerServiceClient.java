package com.moirae.rosettaflow.grpc.client;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.ByteString;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import com.moirae.rosettaflow.grpc.service.*;
import com.moirae.rosettaflow.grpc.task.req.dto.*;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
public class PowerServiceClient {

    @GrpcClient("carrier-grpc-server")
    PowerServiceGrpc.PowerServiceBlockingStub powerServiceStub;

    public List<GetGlobalPowerDetailResponse> getGlobalPowerDetailList(Long latestSynced) {
        GetGlobalPowerDetailListRequest request = GetGlobalPowerDetailListRequest.newBuilder()
                .setLastUpdated(latestSynced)
                .setPageSize(GrpcConstant.PAGE_SIZE)
                .build();

        GetGlobalPowerDetailListResponse response = powerServiceStub.getGlobalPowerDetailList(request);
        if (response.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("TaskServiceClient->getTaskDetailList() fail reason:{}", response.getMsg());
            throw new BusinessException(response.getStatus(), response.getMsg());
        }
        return  response.getPowerListList();
    }
}
