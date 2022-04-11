package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 工作流任务配置表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_task")
public class WorkflowTask implements Serializable {


    /**
     * 工作流任务配置id
     */
    @TableId(value = "workflow_task_id", type = IdType.AUTO)
    private Long workflowTaskId;

    /**
     * 工作流ID
     */
    @TableField("workflow_id")
    private Long workflowId;

    /**
     * 编辑版本标识,从1开始
     */
    @TableField("workflow_version")
    private Long workflowVersion;

    /**
     * 工作流中任务的顺序,从1开始
     */
    private Integer step;

    /**
     * 算法id
     */
    @TableField("algorithm_id")
    private Long algorithmId;

    /**
     * 任务发启放组织id
     */
    @TableField("identity_id")
    private String identityId;

    /**
     * 是否需要输入模型: 0-否，1:是
     */
    @TableField("input_model")
    private Boolean inputModel;

    /**
     * 工作流节点需要的模型id
     */
    @TableField("input_model_id")
    private String inputModelId;

    /**
     * 是否需要输入PSI: 0-否，1:是
     */
    @TableField("input_psi")
    private Boolean inputPsi;

    /**
     * 工作流节点需要的模型id
     */
    @TableField("input_psi_id")
    private String inputPsiId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
