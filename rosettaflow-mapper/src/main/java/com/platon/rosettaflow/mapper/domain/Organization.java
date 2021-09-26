package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_organization
 *
 * @author admin
 */
@Data
@TableName(value = "t_organization")
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 组织表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 组织的身份名称
     */
    private String nodeName;
    /**
     * 组织中调度服务的 nodeId
     */
    private String nodeId;
    /**
     * 组织的身份标识Id
     */
    private String identityId;
    /**
     * 状态: 0-未知，1- 正常， 2- 异常
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
        Organization other = (Organization) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getNodeName() == null ? other.getNodeName() == null : this.getNodeName().equals(other.getNodeName()))
                && (this.getNodeId() == null ? other.getNodeId() == null : this.getNodeId().equals(other.getNodeId()))
                && (this.getIdentityId() == null ? other.getIdentityId() == null : this.getIdentityId().equals(other.getIdentityId()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getNodeName() == null) ? 0 : getNodeName().hashCode());
        result = prime * result + ((getNodeId() == null) ? 0 : getNodeId().hashCode());
        result = prime * result + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
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
                ", nodeName=" + nodeName +
                ", nodeId=" + nodeId +
                ", identityId=" + identityId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}