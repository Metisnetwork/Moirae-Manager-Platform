package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_variable
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_variable")
public class WorkflowNodeVariable implements Serializable {
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
     * 变量类型: 1-自变量, 2-因变量
     */
    private Byte varNodeType;
    /**
     * 变量key
     */
    private String varNodeKey;
    /**
     * 变量值
     */
    private String varNodeValue;
    /**
     * 变量描述
     */
    private String varNodeDesc;
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
        WorkflowNodeVariable other = (WorkflowNodeVariable) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWorkflowNodeId() == null ? other.getWorkflowNodeId() == null : this.getWorkflowNodeId().equals(other.getWorkflowNodeId()))
                && (this.getVarNodeType() == null ? other.getVarNodeType() == null : this.getVarNodeType().equals(other.getVarNodeType()))
                && (this.getVarNodeKey() == null ? other.getVarNodeKey() == null : this.getVarNodeKey().equals(other.getVarNodeKey()))
                && (this.getVarNodeValue() == null ? other.getVarNodeValue() == null : this.getVarNodeValue().equals(other.getVarNodeValue()))
                && (this.getVarNodeDesc() == null ? other.getVarNodeDesc() == null : this.getVarNodeDesc().equals(other.getVarNodeDesc()))
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
        result = prime * result + ((getVarNodeType() == null) ? 0 : getVarNodeType().hashCode());
        result = prime * result + ((getVarNodeKey() == null) ? 0 : getVarNodeKey().hashCode());
        result = prime * result + ((getVarNodeValue() == null) ? 0 : getVarNodeValue().hashCode());
        result = prime * result + ((getVarNodeDesc() == null) ? 0 : getVarNodeDesc().hashCode());
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
                ", varNodeType=" + varNodeType +
                ", varNodeKey=" + varNodeKey +
                ", varNodeValue=" + varNodeValue +
                ", varNodeDesc=" + varNodeDesc +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}