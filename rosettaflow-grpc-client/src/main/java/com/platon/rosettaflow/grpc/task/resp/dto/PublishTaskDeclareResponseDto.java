package com.platon.rosettaflow.grpc.task.resp.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 发布任务响应体
 */
@Data
public class PublishTaskDeclareResponseDto {
    /**
     * 响应码
     */
    private Integer status;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 任务id
     */
    private String taskId;
}
