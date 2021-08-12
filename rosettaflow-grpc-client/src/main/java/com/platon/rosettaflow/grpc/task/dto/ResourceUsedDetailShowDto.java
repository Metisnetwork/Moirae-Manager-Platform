package com.platon.rosettaflow.grpc.task.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/11
 * @description 系统本身资源抽象
 */
@Data
public class ResourceUsedDetailShowDto {
    /**
     * 服务系统的总内存 (单位: byte)
     */
    private Long totalMem;
    /**
     * 服务系统的已用内存 (单位: byte)
     */
    private Long usedMem;
    /**
     * 服务的总内核数 (单位: 个)
     */
    private Long totalProcessor;
    /**
     * 服务的总内核数 (单位: 个)
     */
    private Long usedProcessor;
    /**
     * 服务的总带宽数 (单位: bps)
     */
    private Long totalBandwidth;
    /**
     * 服务的已用带宽数 (单位: bps)
     */
    private Long usedBandwidth;
}
