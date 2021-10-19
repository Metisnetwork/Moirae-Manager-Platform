package com.platon.rosettaflow.grpc.client;

import cn.hutool.core.bean.BeanUtil;
import com.google.protobuf.Empty;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.service.GetTaskResultFileSummaryListResponse;
import com.platon.rosettaflow.grpc.service.GetTaskResultFileSummaryRequest;
import com.platon.rosettaflow.grpc.service.GetTaskResultFileSummaryResponse;
import com.platon.rosettaflow.grpc.service.YarnServiceGrpc;
import com.platon.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/10/13
 * @description 系统状态 接口
 */
@Slf4j
@Component
public class SysServiceClient {

    @GrpcClient("carrier-grpc-server")
    private YarnServiceGrpc.YarnServiceBlockingStub yarnServiceBlockingStub;

    /**
     * 查询指定任务的结果摘要
     *
     * @param taskId 任务id
     * @return 任务结果文件摘要
     */
    public GetTaskResultFileSummaryResponseDto getTaskResultFileSummary(String taskId) {
        GetTaskResultFileSummaryRequest request = GetTaskResultFileSummaryRequest.newBuilder()
                .setTaskId(taskId)
                .build();

        GetTaskResultFileSummaryResponse getTaskResultFileSummaryResponse = yarnServiceBlockingStub.getTaskResultFileSummary(request);
        if (null != getTaskResultFileSummaryResponse) {
            return BeanUtil.toBean(getTaskResultFileSummaryResponse, GetTaskResultFileSummaryResponseDto.class);
        }
        return null;
    }

    /**
     * 获取当前节点所有任务结果摘要
     *
     * @return 结果摘要列表
     */
    public List<GetTaskResultFileSummaryResponseDto> getTaskResultFileSummaryList() {
        List<GetTaskResultFileSummaryResponseDto> responseDtoList = new ArrayList<>();
        GetTaskResultFileSummaryListResponse getTaskResultFileSummaryListResponse = yarnServiceBlockingStub.getTaskResultFileSummaryList(Empty.newBuilder().build());
        if (getTaskResultFileSummaryListResponse.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
            for (GetTaskResultFileSummaryResponse resp : getTaskResultFileSummaryListResponse.getMetadataListList()) {
                responseDtoList.add(BeanUtil.toBean(resp, GetTaskResultFileSummaryResponseDto.class));
            }
        }
        return responseDtoList;
    }
}
