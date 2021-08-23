package com.platon.rosettaflow.grpc.task.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/11
 * @description 获取任务详情响应体
 */
@Data
public class TaskDetailResponseDto {
    /**
     * 任务详情
     */
    private TaskDetailDto information;
    /**
     * 我在任务中的角色 (owner: 任务发起方; dataSupplier: 数据提供方: powerSupplier: 算力提供方; receiver: 结果接收方)
     */
    private String role;
}
