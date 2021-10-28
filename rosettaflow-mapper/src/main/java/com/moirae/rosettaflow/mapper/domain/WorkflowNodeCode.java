package com.moirae.rosettaflow.mapper.domain;

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
@TableName(value = "t_workflow_node_code")
public class WorkflowNodeCode implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点代码表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
    /**
     * 编辑类型:1-sql, 2-noteBook
     */
    private Byte editType;
    /**
     * 计算合约
     */
    private String calculateContractCode;
    /**
     * 数据分片合约
     */
    private String dataSplitContractCode;
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
        WorkflowNodeCode other = (WorkflowNodeCode) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWorkflowNodeId() == null ? other.getWorkflowNodeId() == null : this.getWorkflowNodeId().equals(other.getWorkflowNodeId()))
                && (this.getEditType() == null ? other.getEditType() == null : this.getEditType().equals(other.getEditType()))
                && (this.getCalculateContractCode() == null ? other.getCalculateContractCode() == null : this.getCalculateContractCode().equals(other.getCalculateContractCode()))
                && (this.getDataSplitContractCode() == null ? other.getDataSplitContractCode() == null : this.getDataSplitContractCode().equals(other.getDataSplitContractCode()))
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
        result = prime * result + ((getEditType() == null) ? 0 : getEditType().hashCode());
        result = prime * result + ((getCalculateContractCode() == null) ? 0 : getCalculateContractCode().hashCode());
        result = prime * result + ((getDataSplitContractCode() == null) ? 0 : getDataSplitContractCode().hashCode());
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
                ", editType=" + editType +
                ", calculateContractCode=" + calculateContractCode +
                ", dataSplitContractCode=" + dataSplitContractCode +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}