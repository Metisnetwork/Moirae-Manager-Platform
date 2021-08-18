package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_input
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_input")
public class WorkflowNodeInput implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
    /**
     * 数据类型：1:结构化数据，2:非结构化数据
     */
    private String dataType;
    /**
     * 组织的身份标识Id
     */
    private String identityId;
    /**
     * 组织名称
     */
    private String identityName;
    /**
     * 资源所属组织中调度服务的 nodeId
     */
    private String nodeId;
    /**
     * 数据表ID
     */
    private String dataTableId;
    /**
     * 数据表名称
     */
    private String dataTableName;
    /**
     * 数据字段ID,多个以”,“分隔
     */
    private String dataColumnIds;
    /**
     * 数据字段名称,多个以”,“分隔
     */
    private String dataColumnNames;
    /**
     * 数据文件id
     */
    private String dataFileId;
    /**
     * 任务里面定义的 (p0 -> pN 方 ...)
     */
    private String partyId;
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
        WorkflowNodeInput other = (WorkflowNodeInput) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWorkflowNodeId() == null ? other.getWorkflowNodeId() == null : this.getWorkflowNodeId().equals(other.getWorkflowNodeId()))
                && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
                && (this.getIdentityId() == null ? other.getIdentityId() == null : this.getIdentityId().equals(other.getIdentityId()))
                && (this.getIdentityName() == null ? other.getIdentityName() == null : this.getIdentityName().equals(other.getIdentityName()))
                && (this.getNodeId() == null ? other.getNodeId() == null : this.getNodeId().equals(other.getNodeId()))
                && (this.getDataTableId() == null ? other.getDataTableId() == null : this.getDataTableId().equals(other.getDataTableId()))
                && (this.getDataTableName() == null ? other.getDataTableName() == null : this.getDataTableName().equals(other.getDataTableName()))
                && (this.getDataColumnIds() == null ? other.getDataColumnIds() == null : this.getDataColumnIds().equals(other.getDataColumnIds()))
                && (this.getDataColumnNames() == null ? other.getDataColumnNames() == null : this.getDataColumnNames().equals(other.getDataColumnNames()))
                && (this.getDataFileId() == null ? other.getDataFileId() == null : this.getDataFileId().equals(other.getDataFileId()))
                && (this.getPartyId() == null ? other.getPartyId() == null : this.getPartyId().equals(other.getPartyId()))
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
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
        result = prime * result + ((getIdentityName() == null) ? 0 : getIdentityName().hashCode());
        result = prime * result + ((getNodeId() == null) ? 0 : getNodeId().hashCode());
        result = prime * result + ((getDataTableId() == null) ? 0 : getDataTableId().hashCode());
        result = prime * result + ((getDataTableName() == null) ? 0 : getDataTableName().hashCode());
        result = prime * result + ((getDataColumnIds() == null) ? 0 : getDataColumnIds().hashCode());
        result = prime * result + ((getDataColumnNames() == null) ? 0 : getDataColumnNames().hashCode());
        result = prime * result + ((getDataFileId() == null) ? 0 : getDataFileId().hashCode());
        result = prime * result + ((getPartyId() == null) ? 0 : getPartyId().hashCode());
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
                ", dataType=" + dataType +
                ", identityId=" + identityId +
                ", identityName=" + identityName +
                ", nodeId=" + nodeId +
                ", dataTableId=" + dataTableId +
                ", dataTableName=" + dataTableName +
                ", dataColumnIds=" + dataColumnIds +
                ", dataColumnNames=" + dataColumnNames +
                ", dataFileId=" + dataFileId +
                ", partyId=" + partyId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}