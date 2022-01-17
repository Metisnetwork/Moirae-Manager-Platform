package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "t_workflow_run_task_status")
public class WorkflowRunTaskStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 项目工作流ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流运行状态ID
     */
    private Long workflowRunId;
    /**
     * 工作流节点配置id
     */
    private Long workflowNodeId;
    /**
     * 节点在工作流中序号,从1开始
     */
    private Integer nodeStep;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     *  运行状态: 0-未开始 1-运行中,2-运行成功,3-运行失败
     */
    private Byte runStatus;
    /**
     * 任务ID,底层处理完成后返回
     */
    private String taskId;
    /**
     * 任务处理结果描述
     */
    private String runMsg;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private Model model;

}
