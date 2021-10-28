package com.moirae.rosettaflow.grpc.task.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/9/1
 * @description 终止任务响应对象
 */
@Data
public class TerminateTaskRespDto {
    /**
     * 响应码
     */
    private Integer status;
    /**
     * 错误信息
     */
    private String msg;
}
