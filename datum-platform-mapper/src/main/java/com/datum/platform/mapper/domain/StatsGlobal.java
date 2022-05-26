package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "mo_stats_global")
public class StatsGlobal implements Serializable {

    @TableId
    private int id;

    /**
     * 隐私计算总次数(总的任务数,包括成功和失败的)
     */
    private int taskCount;

    /**
     * 参与计算地址总数
     */
    private int addressCountOfTask;

    /**
     * 24h活跃地址总数
     */
    private int addressCountOfActive;

    /**
     * 全网数据总量，单位：字节
     */
    private long dataSize;

    /**
     * 数据凭证数量
     */
    private int dataTokenCount;

    /**
     * 数据凭证使用量
     */
    private long dataTokenUsed;

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

    private static final long serialVersionUID = 1L;
}
