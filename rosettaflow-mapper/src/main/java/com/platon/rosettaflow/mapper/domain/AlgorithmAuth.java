package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_algorithm_auth
 * @author admin
 */
@Data
@TableName(value = "t_algorithm_auth")
public class AlgorithmAuth implements Serializable {
    /**
     * 算法授权表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 算法表id
     */
    private Long algorithmId;

    /**
     * 授权方式: 1-按时间, 2-按次数, 3-永久
     */
    private Byte authType;

    /**
     * 授权值: 按次数单位为（次）
     */
    private Long authValue;

    /**
     * 授权开始时间
     */
    private Date authBeginTime;

    /**
     * 授权结束时间
     */
    private Date authEndTime;

    /**
     * 授权状态: 0-待申请,1-申请中, 2-已授权,3-已拒绝
     */
    private Byte authStatus;

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
        AlgorithmAuth other = (AlgorithmAuth) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAlgorithmId() == null ? other.getAlgorithmId() == null : this.getAlgorithmId().equals(other.getAlgorithmId()))
            && (this.getAuthType() == null ? other.getAuthType() == null : this.getAuthType().equals(other.getAuthType()))
            && (this.getAuthValue() == null ? other.getAuthValue() == null : this.getAuthValue().equals(other.getAuthValue()))
            && (this.getAuthBeginTime() == null ? other.getAuthBeginTime() == null : this.getAuthBeginTime().equals(other.getAuthBeginTime()))
            && (this.getAuthEndTime() == null ? other.getAuthEndTime() == null : this.getAuthEndTime().equals(other.getAuthEndTime()))
            && (this.getAuthStatus() == null ? other.getAuthStatus() == null : this.getAuthStatus().equals(other.getAuthStatus()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAlgorithmId() == null) ? 0 : getAlgorithmId().hashCode());
        result = prime * result + ((getAuthType() == null) ? 0 : getAuthType().hashCode());
        result = prime * result + ((getAuthValue() == null) ? 0 : getAuthValue().hashCode());
        result = prime * result + ((getAuthBeginTime() == null) ? 0 : getAuthBeginTime().hashCode());
        result = prime * result + ((getAuthEndTime() == null) ? 0 : getAuthEndTime().hashCode());
        result = prime * result + ((getAuthStatus() == null) ? 0 : getAuthStatus().hashCode());
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
                ", userId=" + userId +
                ", algorithmId=" + algorithmId +
                ", authType=" + authType +
                ", authValue=" + authValue +
                ", authBeginTime=" + authBeginTime +
                ", authEndTime=" + authEndTime +
                ", authStatus=" + authStatus +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}