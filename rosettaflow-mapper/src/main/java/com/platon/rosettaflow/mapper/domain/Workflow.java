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
     * 发起任务的用户钱包地址
     */
    private String address;
    /**
     * 发起任务的账户的签名
     */
    private String sign;

    /**
     * 运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败
     */
    private Byte runStatus;

    /**
     * 版本标识，用于逻辑删除
     */
    private Long delVersion;

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
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", workflowName=" + workflowName +
                ", workflowDesc=" + workflowDesc +
                ", nodeNumber=" + nodeNumber +
                ", runStatus=" + runStatus +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}