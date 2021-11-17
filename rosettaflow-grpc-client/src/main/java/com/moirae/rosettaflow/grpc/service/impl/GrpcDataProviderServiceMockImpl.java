package com.moirae.rosettaflow.grpc.service.impl;

import com.moirae.rosettaflow.grpc.data.provider.req.dto.DownloadRequestDto;
import com.moirae.rosettaflow.grpc.data.provider.resp.dto.DownloadReplyResponseDto;
import com.moirae.rosettaflow.grpc.service.GrpcDataProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author juzix
 * @description 获取任务结果数据
 */
@Slf4j
@Service
@Profile({"dev", "uat"})
public class GrpcDataProviderServiceMockImpl implements GrpcDataProviderService {



    @Override
    public DownloadReplyResponseDto downloadTask(DownloadRequestDto requestDto) {
        return null;
    }
}
