package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@TableName(value = "t_workflow_run_status")
public class WorkflowRunStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 项目工作流ID(自增长)
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
    private Integer curStep;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     *  运行状态: 1-运行中,2-运行成功,3-运行失败
     */
    private Byte runStatus;
    /**
     *  取消状态: 1-取消中,2-取消成功,3-取消失败
     */
    private Byte cancelStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 步骤对应的任务状态
     */
    @TableField(exist = false)
    private Map<Integer, WorkflowRunTaskStatus> workflowRunTaskStatusMap;
    @TableField(exist = false)
    private Workflow workflow;
    @TableField(exist = false)
    private String workflowName;
    @TableField(exist = false)
    private Long projectId;
}
