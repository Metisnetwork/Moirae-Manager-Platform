package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * dc_task_power_provider
 * @author
 */
@Data
@TableName(value = "dc_task_power_provider")
public class TaskPowerProvider implements Serializable {
    /**
     * 任务ID,hash
     */
    private String taskId;

    /**
     * 算力提供者组织身份ID
     */
    private String identityId;

    /**
     * 任务参与方在本次任务中的唯一识别ID
     */
    private String partyId;

    /**
     * 任务使用的内存, 字节
     */
    private Long usedMemory;

    /**
     * 任务使用的core
     */
    private Integer usedCore;

    /**
     * 任务使用的带宽, bps
     */
    private Long usedBandwidth;

    private static final long serialVersionUID = 1L;

    /**
     * 组织名称
     */
    @TableField(exist = false)
    private String nodeName;
}
