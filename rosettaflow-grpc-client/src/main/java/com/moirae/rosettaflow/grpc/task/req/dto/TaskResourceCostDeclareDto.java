package com.moirae.rosettaflow.grpc.task.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/4
 * @description 任务需要花费的资源声明
 */
@Data
public class TaskResourceCostDeclareDto {
    /**
     * 任务所需的内存 (单位: byte)
     */
    private Long memory;

    /**
     * 任务所需的核数 (单位: 个)
     */
    private Integer processor;

    /**
     * 任务所需的带宽 (单位: bps)
     */
    private Long bandwidth;

    /**
     * 任务所需的运行时长 (单位: ms)
     */
    private Long duration;

}
