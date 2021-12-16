package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user_org_maintain
 *
 * @author admin
 */
@Data
@TableName(value = "t_user_org_maintain")
public class UserOrgMaintain implements Serializable {
    /**
     * 用户组织连接绑定关系表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户钱包地址
     */
    private String address;

    /**
     * 组织的身份标识Id
     */
    private String identityId;

    /**
     * 组织的ip
     */
    private String identityIp;

    /**
     * 组织的端口
     */
    private Integer identityPort;

    /**
     * 连接有效状态: 0-无效，1- 有效
     */
    private Byte validFlag;

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
        UserOrgMaintain other = (UserOrgMaintain) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getIdentityId() == null ? other.getIdentityId() == null : this.getIdentityId().equals(other.getIdentityId()))
                && (this.getIdentityIp() == null ? other.getIdentityIp() == null : this.getIdentityIp().equals(other.getIdentityIp()))
                && (this.getIdentityPort() == null ? other.getIdentityPort() == null : this.getIdentityPort().equals(other.getIdentityPort()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
        result = prime * result + ((getIdentityIp() == null) ? 0 : getIdentityIp().hashCode());
        result = prime * result + ((getIdentityPort() == null) ? 0 : getIdentityPort().hashCode());
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
                ", address=" + address +
                ", identityId=" + identityId +
                ", identityIp=" + identityIp +
                ", identityPort=" + identityPort +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}
