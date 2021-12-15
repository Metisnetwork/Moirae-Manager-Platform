package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_temp
 * @author houz
 */
@Data
@TableName(value = "t_workflow_node_temp")
public class WorkflowNodeTemp implements Serializable {
    /**
     * 工作流节点模板表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工作流模板表id
     */
    private Long workflowTempId;

    /**
     * 算法id
     */
    private Long algorithmId;

    /**
     * 中文节点名称
     */
    private String nodeName;

    /**
     * 英文节点名称
     */
    private String nodeNameEn;

    /**
     * 节点在工作流中序号,从1开始
     */
    private Integer nodeStep;

    /**
     * 下一个节点,如果为空则无下个节点
     */
    private Integer nextNodeStep;

    /**
     * 运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败
     */
    private Byte runStatus;

    /**
     * 状态: 0-无效，1- 有效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}