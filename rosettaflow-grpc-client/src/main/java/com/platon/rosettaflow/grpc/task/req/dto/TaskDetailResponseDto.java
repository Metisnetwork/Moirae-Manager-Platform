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
    /**
     * 0-未知的任务角色
     * 1-任务发起方
     * 2-数据提供方
     * 3-算力提供方
     * 4-结果接收方
     * 5-算法提供方
     */
    private Integer role;
}
