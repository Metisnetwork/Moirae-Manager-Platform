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
     * 算法id
     */
    private Long algorithmId;
    /**
     * 节点在工作流中序号,从1开始
     */
    private Integer nodeStep;
    /**
     * 运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败
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
        WorkflowNode other = (WorkflowNode) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWorkflowId() == null ? other.getWorkflowId() == null : this.getWorkflowId().equals(other.getWorkflowId()))
                && (this.getAlgorithmId() == null ? other.getAlgorithmId() == null : this.getAlgorithmId().equals(other.getAlgorithmId()))
                && (this.getNodeStep() == null ? other.getNodeStep() == null : this.getNodeStep().equals(other.getNodeStep()))
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
        result = prime * result + ((getWorkflowId() == null) ? 0 : getWorkflowId().hashCode());
        result = prime * result + ((getAlgorithmId() == null) ? 0 : getAlgorithmId().hashCode());
        result = prime * result + ((getNodeStep() == null) ? 0 : getNodeStep().hashCode());
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
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", workflowId=" + workflowId +
                ", algorithmId=" + algorithmId +
                ", nodeStep=" + nodeStep +
                ", runStatus=" + runStatus +
                ", taskId=" + taskId +
                ", runMsg=" + runMsg +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}