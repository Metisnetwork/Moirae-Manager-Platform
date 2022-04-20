package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.WorkflowTaskRunStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工作流不同版本设置表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_version")
public class WorkflowVersion implements Serializable {


    /**
     * 工作流ID
     */
    private Long workflowId;

    /**
     * 编辑版本标识,从1开始
     */
    private Long workflowVersion;

    /**
     * 工作流版本名称
     */
    @TableField("workflow_version_name")
    private String workflowVersionName;

    /**
     * 状态: 0-待支付、1-支付中、2-已支付、3-运行中、4-运行成功、5-运行失败
     */
    @TableField("`status`")
    private WorkflowTaskRunStatusEnum status;

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

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private Date beginTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private Date endTime;
}
