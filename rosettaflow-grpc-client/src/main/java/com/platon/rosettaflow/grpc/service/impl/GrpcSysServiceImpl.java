package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.client.SysServiceClient;
import com.platon.rosettaflow.grpc.client.TaskServiceClient;
import com.platon.rosettaflow.grpc.service.GrpcSysService;
import com.platon.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
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
@Profile({"prod", "test", "local"})
public class GrpcSysServiceImpl implements GrpcSysService {

    @Resource
    private SysServiceClient sysServiceClient;

    @Override
    public GetTaskResultFileSummaryResponseDto getTaskResultFileSummary(String taskId) {
        return sysServiceClient.getTaskResultFileSummary(taskId);
    }

    @Override
    public List<GetTaskResultFileSummaryResponseDto> getTaskResultFileSummaryList() {
        return sysServiceClient.getTaskResultFileSummaryList();
    }
}
