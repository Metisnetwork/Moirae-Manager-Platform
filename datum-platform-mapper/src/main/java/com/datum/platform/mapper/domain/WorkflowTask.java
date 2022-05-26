package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
     * 工作流节点需要的模型产生的步骤
     */
    private Integer inputModelStep;

    /**
     * 是否需要输入PSI: 0-否，1:是
     */
    @TableField("input_psi")
    private Boolean inputPsi;

    /**
     * 工作流节点需要的psi产生步骤
     */
    private Integer inputPsiStep;

    /**
     * 是否可用: 0-否，1:是
     */
    private Boolean enable;

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
    @TableField(exist = false)
    private List<WorkflowTaskInput> inputList;
    @TableField(exist = false)
    private List<WorkflowTaskOutput> outputList;
    @TableField(exist = false)
    private WorkflowTaskResource resource;
    @TableField(exist = false)
    private List<WorkflowTaskVariable> variableList;
    @TableField(exist = false)
    private Algorithm algorithm;
    @TableField(exist = false)
    private Org org;
}
