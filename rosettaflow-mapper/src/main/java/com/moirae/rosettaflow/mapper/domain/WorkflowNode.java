package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node")
public class WorkflowNode implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流id
     */
    private Long workflowId;
    /**
     * 工作流版本号
     */
    private Integer workflowEditVersion;
    /**
     * 工作流节点名称
     */
    private String nodeName;
    /**
     * 算法id
     */
    private Long algorithmId;
    /**
     * 节点在工作流中序号,从1开始
     */
    private Integer nodeStep;
    /**
     * 工作流节点需要的模型id,对应t_task_result表id
     */
    private Long modelId;
    /**
     * 任务发启放组织id
     */
    private String senderIdentityId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 任务ID,底层处理完成后返回
     */
    @TableField(exist = false)
    private String taskId;
    /**
     * 任务处理结果描述
     */
    @TableField(exist = false)
    private String runMsg;
}
