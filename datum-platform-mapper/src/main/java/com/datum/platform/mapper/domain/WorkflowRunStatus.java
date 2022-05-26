package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.WorkflowTaskRunStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工作流运行状态
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_run_status")
public class WorkflowRunStatus implements Serializable {


    /**
     * 工作流节点表ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工作流id
     */
    @TableField("workflow_id")
    private Long workflowId;

    /**
     * 工作流版本号
     */
    @TableField("workflow_version")
    private Long workflowVersion;

    /**
     * 发起任务的账户的签名
     */
    private String sign;

    /**
     * 发起任务的账户的地址
     */
    private String address;

    /**
     * 总步骤
     */
    private Integer step;

    /**
     * 当前步骤
     */
    @TableField("cur_step")
    private Integer curStep;

    /**
     * 开始时间
     */
    @TableField("begin_time")
    private Date beginTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 运行状态: 1-运行中,2-运行成功,3-运行失败
     */
    @TableField("run_status")
    private WorkflowTaskRunStatusEnum runStatus;

    /**
     * 取消状态: 0-待运行, 1-取消中, 2-取消成功, 3-取消失败
     */
    @TableField("cancel_status")
    private WorkflowTaskRunStatusEnum cancelStatus;

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
    private List<WorkflowRunTaskStatus> workflowRunTaskStatusList;
    @TableField(exist = false)
    private Workflow workflow;
}
