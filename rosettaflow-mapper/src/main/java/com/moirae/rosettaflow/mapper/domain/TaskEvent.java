package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_task_event
 *
 * @author admin
 */
@Data
@TableName(value = "t_task_event")
public class TaskEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 任务表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 任务ID,hash
     */
    private String taskId;
    /**
     * 事件类型
     */
    private String type;
    /**
     * 产生事件的组织身份ID
     */
    private String identityId;
    /**
     * 产生事件的时间
     */
    private Date eventTime;
    /**
     * 事件内容
     */
    private String content;
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
        TaskEvent other = (TaskEvent) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTaskId() == null ? other.getTaskId() == null : this.getTaskId().equals(other.getTaskId()))
                && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
                && (this.getIdentityId() == null ? other.getIdentityId() == null : this.getIdentityId().equals(other.getIdentityId()))
                && (this.getEventTime() == null ? other.getEventTime() == null : this.getEventTime().equals(other.getEventTime()))
                && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTaskId() == null) ? 0 : getTaskId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
        result = prime * result + ((getEventTime() == null) ? 0 : getEventTime().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
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
                ", type=" + type +
                ", identityId=" + identityId +
                ", eventTime=" + eventTime +
                ", content=" + content +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}