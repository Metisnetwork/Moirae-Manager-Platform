package com.moirae.rosettaflow.grpc.service;

import com.moirae.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/10/13
 * @description grpc sys处理服务
 */
public interface GrpcSysService {
    /**
     * 查询指定任务的结果摘要
     *
     * @param taskId 任务id
     * @return 任务结果文件摘要
     */
    GetTaskResultFileSummaryResponseDto getTaskResultFileSummary(String taskId);

    /**
     * 获取当前节点所有任务结果摘要
     *
     * @return 结果摘要列表
     */
    List<GetTaskResultFileSummaryResponseDto> getTaskResultFileSummaryList();
}
