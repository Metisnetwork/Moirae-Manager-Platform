package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow")
public class Workflow implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 项目工作流ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 用户id(创建方id)
     */
    private Long userId;
    /**
     * 工作流名称
     */
    private String workflowName;
    /**
     * 工作流描述
     */
    private String workflowDesc;
    /**
     * 节点数
     */
    private Integer nodeNumber;
    /**
     * 运行状态:0-未完成,1-已完成
     */
    private Byte runStatus;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 任务处理结果描述
     */
    private String runMsg;
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Workflow other = (Workflow) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
                && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
                && (this.getWorkflowName() == null ? other.getWorkflowName() == null : this.getWorkflowName().equals(other.getWorkflowName()))
                && (this.getWorkflowDesc() == null ? other.getWorkflowDesc() == null : this.getWorkflowDesc().equals(other.getWorkflowDesc()))
                && (this.getNodeNumber() == null ? other.getNodeNumber() == null : this.getNodeNumber().equals(other.getNodeNumber()))
                && (this.getRunStatus() == null ? other.getRunStatus() == null : this.getRunStatus().equals(other.getRunStatus()))
                && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
                && (this.getRunMsg() == null ? other.getRunMsg() == null : this.getRunMsg().equals(other.getRunMsg()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getWorkflowName() == null) ? 0 : getWorkflowName().hashCode());
        result = prime * result + ((getWorkflowDesc() == null) ? 0 : getWorkflowDesc().hashCode());
        result = prime * result + ((getNodeNumber() == null) ? 0 : getNodeNumber().hashCode());
        result = prime * result + ((getRunStatus() == null) ? 0 : getRunStatus().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getRunMsg() == null) ? 0 : getRunMsg().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", workflowName=" + workflowName +
                ", workflowDesc=" + workflowDesc +
                ", nodeNumber=" + nodeNumber +
                ", runStatus=" + runStatus +
                ", taskId=" + taskId +
                ", runMsg=" + runMsg +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}