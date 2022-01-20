package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_temp_node
 * @author houz
 */
@Data
@TableName(value = "t_workflow_temp_node")
public class WorkflowTempNode implements Serializable {
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
     * 工作流节点需要的模型id
     */
    private Long modelId;

    /**
     * 是否需要输入模型: 0-否，1:是
     */
    private Integer inputModel;

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
