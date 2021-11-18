package com.moirae.rosettaflow.mapper.domain;

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
     * 授权方式: 0-未知, 1-按时间, 2-按次数, 3-永久
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
     * 审核意见
     */
    private String auditSuggestion;

    /**
     * 发起授权申请的时间
     */
    private Date applyTime;

    /**
     * 审核授权申请的时间
     */
    private Date auditTime;

    /**
     * 数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>;)
     */
    private Byte authMetadataState;

    /**
     * 是否已过期（按时间时需要）: 0-未过期, 1-已过期
     */
    private Byte expire;

    /**
     * 已经使用的次数(按次数时有效)
     */
    private Integer usedTimes;

    /**
     * 元数据申请授权id
     */
    private String metadataAuthId;

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

}