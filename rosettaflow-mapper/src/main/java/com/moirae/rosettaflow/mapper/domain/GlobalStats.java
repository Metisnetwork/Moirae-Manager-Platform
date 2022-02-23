package com.moirae.rosettaflow.mapper.domain;

import lombok.Data;

@Data
public class GlobalStats {

    /**
     * 全网组织数量
     */
    private int totalOrgCount;

    /**
     * 全是提供算力的组织数量
     */
    private int powerOrgCount;

    /**
     * 全网目前的元数据量，单位：字节
     */
    private long dataFileSize;

    /**
     * 全网元数据累计使用量，单位：字节
     */
    private long usedDataFileSize;

    /**
     * 全网任务数
     */
    private int taskCount;

    /**
     * 全网参与过任务的组织数，包括角色是：发起人， 算法提供方，算力提供者，数据提供者，结果消费者
     */
    private int partnerCount;

    /**
     * 全网目前的算力核心数，单位：个
     */
    private int totalCore;

    /**
     * 全网目前的算力内存数，单位：字节
     */
    private long totalMemory;

    /**
     * 全网目前的带宽，单位：字节
     */
    private long totalBandwidth;
}
