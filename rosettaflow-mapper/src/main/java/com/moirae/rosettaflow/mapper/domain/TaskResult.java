package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user
 *
 * @author admin
 */
@Data
@TableName(value = "t_task_result")
public class TaskResult implements Serializable {
    /**
     * 任务运行结果表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务结果文件的名称
     */
    private String fileName;

    /**
     * 任务结果文件的元数据Id <系统默认生成的元数据>
     */
    private String metadataId;

    /**
     * 任务结果文件的原始文件Id
     */
    private String originId;

    /**
     * 任务结果文件的完整相对路径名
     */
    private String filePath;

    /**
     * 任务结果文件所在的 数据服务内网ip
     */
    private String ip;

    /**
     * 任务结果文件所在的 数据服务内网port
     */
    private String port;

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
        TaskResult other = (TaskResult) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
                && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
                && (this.getMetadataId() == null ? other.getMetadataId() == null : this.getMetadataId().equals(other.getMetadataId()))
                && (this.getOriginId() == null ? other.getOriginId() == null : this.getOriginId().equals(other.getOriginId()))
                && (this.getFilePath() == null ? other.getFilePath() == null : this.getFilePath().equals(other.getFilePath()))
                && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
                && (this.getPort() == null ? other.getPort() == null : this.getPort().equals(other.getPort()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getMetadataId() == null) ? 0 : getMetadataId().hashCode());
        result = prime * result + ((getOriginId() == null) ? 0 : getOriginId().hashCode());
        result = prime * result + ((getFilePath() == null) ? 0 : getFilePath().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getPort() == null) ? 0 : getPort().hashCode());
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
                ", taskId=" + taskId +
                ", fileName=" + fileName +
                ", metadataId=" + metadataId +
                ", originId=" + originId +
                ", filePath=" + filePath +
                ", ip=" + ip +
                ", port=" + port +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}