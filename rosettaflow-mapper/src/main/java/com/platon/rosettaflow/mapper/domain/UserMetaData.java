package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user_meta_data
 *
 * @author admin
 */
@Data
@TableName(value = "t_user_meta_data")
public class UserMetaData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 数据详情表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 元数据表id
     */
    private Long dataId;
    /**
     * 用户钱包地址
     */
    private String address;
    /**
     * 授权方式: 1-按时间, 2-按次数, 3-永久
     */
    private Byte authType;
    /**
     * 授权值:按次数单位为（次）
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
        UserMetaData other = (UserMetaData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getDataId() == null ? other.getDataId() == null : this.getDataId().equals(other.getDataId()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getAuthType() == null ? other.getAuthType() == null : this.getAuthType().equals(other.getAuthType()))
                && (this.getAuthValue() == null ? other.getAuthValue() == null : this.getAuthValue().equals(other.getAuthValue()))
                && (this.getAuthBeginTime() == null ? other.getAuthBeginTime() == null : this.getAuthBeginTime().equals(other.getAuthBeginTime()))
                && (this.getAuthEndTime() == null ? other.getAuthEndTime() == null : this.getAuthEndTime().equals(other.getAuthEndTime()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDataId() == null) ? 0 : getDataId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getAuthType() == null) ? 0 : getAuthType().hashCode());
        result = prime * result + ((getAuthValue() == null) ? 0 : getAuthValue().hashCode());
        result = prime * result + ((getAuthBeginTime() == null) ? 0 : getAuthBeginTime().hashCode());
        result = prime * result + ((getAuthEndTime() == null) ? 0 : getAuthEndTime().hashCode());
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
                ", dataId=" + dataId +
                ", address=" + address +
                ", authType=" + authType +
                ", authValue=" + authValue +
                ", authBeginTime=" + authBeginTime +
                ", authEndTime=" + authEndTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}