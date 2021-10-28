package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_output
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_output")
public class WorkflowNodeOutput implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点输出表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
    /**
     * 协同方组织的身份标识Id
     */
    private String identityId;
    /**
     * 任务里面定义的 (p0 -> pN 方 ...)
     */
    private String partyId;
    /**
     * 是否发起方: 0-否,1-是
     */
    private Byte senderFlag;

    /**
     * 存储形式: 1-明文，2:密文
     */
    private Byte storePattern;
    /**
     * 存储路径
     */
    private String storePath;
    /**
     * 输出内容
     */
    private String outputContent;
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
        WorkflowNodeOutput other = (WorkflowNodeOutput) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWorkflowNodeId() == null ? other.getWorkflowNodeId() == null : this.getWorkflowNodeId().equals(other.getWorkflowNodeId()))
                && (this.getIdentityId() == null ? other.getIdentityId() == null : this.getIdentityId().equals(other.getIdentityId()))
                && (this.getPartyId() == null ? other.getPartyId() == null : this.getPartyId().equals(other.getPartyId()))
                && (this.getSenderFlag() == null ? other.getSenderFlag() == null : this.getSenderFlag().equals(other.getSenderFlag()))
                && (this.getStorePattern() == null ? other.getStorePattern() == null : this.getStorePattern().equals(other.getStorePattern()))
                && (this.getStorePath() == null ? other.getStorePath() == null : this.getStorePath().equals(other.getStorePath()))
                && (this.getOutputContent() == null ? other.getOutputContent() == null : this.getOutputContent().equals(other.getOutputContent()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWorkflowNodeId() == null) ? 0 : getWorkflowNodeId().hashCode());
        result = prime * result + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
        result = prime * result + ((getPartyId() == null) ? 0 : getPartyId().hashCode());
        result = prime * result + ((getSenderFlag() == null) ? 0 : getSenderFlag().hashCode());
        result = prime * result + ((getStorePattern() == null) ? 0 : getStorePattern().hashCode());
        result = prime * result + ((getStorePath() == null) ? 0 : getStorePath().hashCode());
        result = prime * result + ((getOutputContent() == null) ? 0 : getOutputContent().hashCode());
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
                ", workflowNodeId=" + workflowNodeId +
                ", identityId=" + identityId +
                ", partyId=" + partyId +
                ", senderFlag=" + senderFlag +
                ", storePattern=" + storePattern +
                ", storePath=" + storePath +
                ", outputContent=" + outputContent +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}