package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.client.SysServiceClient;
import com.moirae.rosettaflow.grpc.client.TaskServiceClient;
import com.moirae.rosettaflow.grpc.service.GrpcSysService;
import com.moirae.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
public class GrpcSysServiceImpl implements GrpcSysService {

    @Resource
    private SysServiceClient sysServiceClient;

    @Override
    public GetTaskResultFileSummaryResponseDto getTaskResultFileSummary(ManagedChannel channel,String taskId) {
        return sysServiceClient.getTaskResultFileSummary(channel,taskId);
    }

    @Override
    public List<GetTaskResultFileSummaryResponseDto> getTaskResultFileSummaryList() {
        return sysServiceClient.getTaskResultFileSummaryList();
    }
}
