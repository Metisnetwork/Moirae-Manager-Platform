package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_resource
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_resource")
public class WorkflowNodeResource implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 节点资源表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
    /**
     * 所需的内存 (单位: byte)
     */
    private Long costMem;
    /**
     * 所需的核数 (单位: 个)
     */
    private Integer costCpu;
    /**
     * GPU核数(单位：核)
     */
    private Integer costGpu;
    /**
     * 所需的带宽 (单位: bps)
     */
    private Long costBandwidth;
    /**
     * 所需的运行时长 (单位: ms)
     */
    private Long runTime;
    /**
     * 状态: 0-无效，1- 有效
     */
    @TableField(value = "`status`")
    private Byte status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}