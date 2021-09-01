package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_meta_data_details
 *
 * @author admin
 */
@Data
@TableName(value = "t_user_meta_data")
public class UserMetaData implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户数据详情表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 元数据id
     */
    private String metaDataId;
    /**
     * 资源所属组织的身份标识Id
     */
    private String identityId;
    /**
     * 资源所属组织名称
     */
    private String identityName;
    /**
     * 资源所属组织中调度服务的 nodeId
     */
    private String nodeId;
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
    private Integer authValue;
    /**
     * 授权开始时间
     */
    private Date authBeginTime;
    /**
     * 授权结束时间
     */
    private Date authEndTime;
    /**
     * 授权状态: 0-等待审核中, 1-审核通过, 2-审核拒绝
     */
    private Byte authStatus;
    /**
     * 发起授权申请的时间
     */
    private Date applyTime;
    /**
     * 审核授权申请的时间
     */
    private Date auditTime;
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
        UserMetaData other = (UserMetaData) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getMetaDataId() == null ? other.getMetaDataId() == null : this.getMetaDataId().equals(other.getMetaDataId()))
                && (this.getIdentityId() == null ? other.getIdentityId() == null : this.getIdentityId().equals(other.getIdentityId()))
                && (this.getIdentityName() == null ? other.getIdentityName() == null : this.getIdentityName().equals(other.getIdentityName()))
                && (this.getNodeId() == null ? other.getNodeId() == null : this.getNodeId().equals(other.getNodeId()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
                && (this.getAuthType() == null ? other.getAuthType() == null : this.getAuthType().equals(other.getAuthType()))
                && (this.getAuthValue() == null ? other.getAuthValue() == null : this.getAuthValue().equals(other.getAuthValue()))
                && (this.getAuthBeginTime() == null ? other.getAuthBeginTime() == null : this.getAuthBeginTime().equals(other.getAuthBeginTime()))
                && (this.getAuthEndTime() == null ? other.getAuthEndTime() == null : this.getAuthEndTime().equals(other.getAuthEndTime()))
                && (this.getAuthStatus() == null ? other.getAuthStatus() == null : this.getAuthStatus().equals(other.getAuthStatus()))
                && (this.getApplyTime() == null ? other.getApplyTime() == null : this.getApplyTime().equals(other.getApplyTime()))
                && (this.getAuditTime() == null ? other.getAuditTime() == null : this.getAuditTime().equals(other.getAuditTime()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMetaDataId() == null) ? 0 : getMetaDataId().hashCode());
        result = prime * result + ((getIdentityId() == null) ? 0 : getIdentityId().hashCode());
        result = prime * result + ((getIdentityName() == null) ? 0 : getIdentityName().hashCode());
        result = prime * result + ((getNodeId() == null) ? 0 : getNodeId().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getAuthType() == null) ? 0 : getAuthType().hashCode());
        result = prime * result + ((getAuthValue() == null) ? 0 : getAuthValue().hashCode());
        result = prime * result + ((getAuthBeginTime() == null) ? 0 : getAuthBeginTime().hashCode());
        result = prime * result + ((getAuthEndTime() == null) ? 0 : getAuthEndTime().hashCode());
        result = prime * result + ((getAuthStatus() == null) ? 0 : getAuthStatus().hashCode());
        result = prime * result + ((getApplyTime() == null) ? 0 : getApplyTime().hashCode());
        result = prime * result + ((getAuditTime() == null) ? 0 : getAuditTime().hashCode());
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
                ", metaDataId=" + metaDataId +
                ", identityId=" + identityId +
                ", identityName=" + identityName +
                ", nodeId=" + nodeId +
                ", address=" + address +
                ", authType=" + authType +
                ", authValue=" + authValue +
                ", authBeginTime=" + authBeginTime +
                ", authEndTime=" + authEndTime +
                ", authStatus=" + authStatus +
                ", applyTime=" + applyTime +
                ", auditTime=" + auditTime +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}