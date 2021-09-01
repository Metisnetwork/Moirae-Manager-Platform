package com.platon.rosettaflow.grpc.task.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/4
 * @description 任务的所需操作成本 (定义任务的大小)
 */
@Data
public class TaskResourceCostDeclareDto {
    /**
     * 任务所需的内存 (单位: byte)
     */
    private Long costMem;

    /**
     * 任务所需的核数 (单位: 个)
     */
    private Integer costProcessor;

    /**
     * 任务所需的带宽 (单位: bps)
     */
    private Long costBandwidth;

    /**
     * 任务所需的运行时长 (单位: ms)
     */
    private Long duration;

}
