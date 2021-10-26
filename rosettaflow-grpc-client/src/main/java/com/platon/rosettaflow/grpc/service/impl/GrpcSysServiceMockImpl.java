package com.platon.rosettaflow.grpc.service.impl;

import com.platon.rosettaflow.grpc.service.GrpcSysService;
import com.platon.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/10/13
 * @description 调度服务处理mock服务
 */
@Slf4j
@Service
@Profile({"dev", "uat"})
public class GrpcSysServiceMockImpl implements GrpcSysService {
    @Override
    public GetTaskResultFileSummaryResponseDto getTaskResultFileSummary(String taskId) {
        GetTaskResultFileSummaryResponseDto responseDto = new GetTaskResultFileSummaryResponseDto();
        responseDto.setTaskId(taskId);
        responseDto.setOriginId("originId");
        responseDto.setIp("localhost");
        responseDto.setPort("9999");
        responseDto.setFileName("mockFileName");
        responseDto.setFilePath("/mockFilePath");
        responseDto.setMetadataId("mockMetaDataId");
        return responseDto;
    }

    @Override
    public List<GetTaskResultFileSummaryResponseDto> getTaskResultFileSummaryList() {
        return null;
    }
}
