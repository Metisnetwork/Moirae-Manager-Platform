package com.platon.rosettaflow.grpc.task.req.dto;

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
    private TaskDetailShowDto information;
}
