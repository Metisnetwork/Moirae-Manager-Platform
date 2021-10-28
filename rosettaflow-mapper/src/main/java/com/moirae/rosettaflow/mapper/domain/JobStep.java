package com.moirae.rosettaflow.mapper.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_job_step
 *
 * @author admin
 */
@Data
public class JobStep implements Serializable {
    /**
     * 任务表ID(自增长)
     */
    @ApiModelProperty(value = "任务表ID(自增长)")
    private Long id;

    /**
     * 任务编号
     */
    @ApiModelProperty(value = "任务编号")
    private String jobNo;

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id")
    private Long jobId;

    /**
     * 工作流节点id
     */
    @ApiModelProperty(value = "工作流节点id")
    private Long workflowNodeId;

    /**
     * 工作流节点步骤表id
     */
    @ApiModelProperty(value = "工作流节点步骤表id")
    private Long step;

    /**
     * 作业状态:0-未开始,1-运行中,2-待协作方同意,3-运行成功,4-运行失败
     */
    @ApiModelProperty(value = "作业状态:0-未开始,1-运行中,2-待协作方同意,3-运行成功,4-运行失败")
    private Byte status;

    /**
     * 保存路径
     */
    @ApiModelProperty(value = "保存路径")
    private String savePath;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
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
        JobStep other = (JobStep) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getJobNo() == null ? other.getJobNo() == null : this.getJobNo().equals(other.getJobNo()))
                && (this.getJobId() == null ? other.getJobId() == null : this.getJobId().equals(other.getJobId()))
                && (this.getWorkflowNodeId() == null ? other.getWorkflowNodeId() == null : this.getWorkflowNodeId().equals(other.getWorkflowNodeId()))
                && (this.getStep() == null ? other.getStep() == null : this.getStep().equals(other.getStep()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getSavePath() == null ? other.getSavePath() == null : this.getSavePath().equals(other.getSavePath()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getJobNo() == null) ? 0 : getJobNo().hashCode());
        result = prime * result + ((getJobId() == null) ? 0 : getJobId().hashCode());
        result = prime * result + ((getWorkflowNodeId() == null) ? 0 : getWorkflowNodeId().hashCode());
        result = prime * result + ((getStep() == null) ? 0 : getStep().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getSavePath() == null) ? 0 : getSavePath().hashCode());
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
                ", jobNo=" + jobNo +
                ", jobId=" + jobId +
                ", workflowNodeId=" + workflowNodeId +
                ", step=" + step +
                ", status=" + status +
                ", savePath=" + savePath +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}