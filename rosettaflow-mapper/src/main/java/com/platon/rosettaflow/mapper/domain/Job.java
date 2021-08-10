package com.platon.rosettaflow.mapper.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_job
 * @author admin
 */
@Data
public class Job implements Serializable {
    /**
     * 任务表ID(自增长)
     */
    @ApiModelProperty(value="任务表ID(自增长)")
    private Long id;

    /**
     * 工作流id
     */
    @ApiModelProperty(value="工作流id")
    private Long workflowId;

    /**
     * 任务名称
     */
    @ApiModelProperty(value="任务名称")
    private String name;

    /**
     * 作业状态:0-未开始,1-运行中,2-待协作方同意,3-运行成功,4-运行失败
     */
    @ApiModelProperty(value="作业状态:0-未开始,1-运行中,2-待协作方同意,3-运行成功,4-运行失败")
    private Byte status;

    /**
     * 当前运行节点序号:0-未启动
     */
    @ApiModelProperty(value="当前运行节点序号:0-未启动")
    private Byte currentStep;

    /**
     * 即将到运行节点序号
     */
    @ApiModelProperty(value="即将到运行节点序号")
    private Byte distStep;

    /**
     * 最后一个运行节点序号:默认工作流最后一个节点
     */
    @ApiModelProperty(value="最后一个运行节点序号:默认工作流最后一个节点")
    private Byte lastStep;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

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
        Job other = (Job) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWorkflowId() == null ? other.getWorkflowId() == null : this.getWorkflowId().equals(other.getWorkflowId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCurrentStep() == null ? other.getCurrentStep() == null : this.getCurrentStep().equals(other.getCurrentStep()))
            && (this.getDistStep() == null ? other.getDistStep() == null : this.getDistStep().equals(other.getDistStep()))
            && (this.getLastStep() == null ? other.getLastStep() == null : this.getLastStep().equals(other.getLastStep()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWorkflowId() == null) ? 0 : getWorkflowId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCurrentStep() == null) ? 0 : getCurrentStep().hashCode());
        result = prime * result + ((getDistStep() == null) ? 0 : getDistStep().hashCode());
        result = prime * result + ((getLastStep() == null) ? 0 : getLastStep().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", workflowId=").append(workflowId);
        sb.append(", name=").append(name);
        sb.append(", status=").append(status);
        sb.append(", currentStep=").append(currentStep);
        sb.append(", distStep=").append(distStep);
        sb.append(", lastStep=").append(lastStep);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}