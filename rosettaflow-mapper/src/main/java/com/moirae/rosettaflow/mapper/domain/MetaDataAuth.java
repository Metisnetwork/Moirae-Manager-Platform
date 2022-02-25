package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthOptionEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthStatusEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthTypeEnum;
import com.moirae.rosettaflow.mapper.enums.UserTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * dc_meta_data_auth
 * @author
 */
@Data
@TableName(value = "dc_meta_data_auth")
public class MetaDataAuth implements Serializable {
    /**
     * 申请数据授权的ID
     */
    @TableId
    private String metaDataAuthId;

    /**
     * 申请用户所属组织身份ID
     */
    private String userIdentityId;

    /**
     * 申请数据授权的用户ID
     */
    private String userId;

    /**
     * 用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址
     */
    private UserTypeEnum userType;

    /**
     * 元数据ID,hash
     */
    private String metaDataId;

    /**
     * 元数据在分布式存储环境中的状态 (0: DataStatus_Unknown ; DataStatus_Normal = 1; DataStatus_Deleted = 2)
     */
    private Integer dfsDataStatus;

    /**
     * 元数据在分布式存储环境中的ID
     */
    private String dfsDataId;

    /**
     * 申请收集授权类型：(0: 未定义; 1: 按照时间段来使用; 2: 按照次数来使用)
     */
    private MetaDataAuthTypeEnum authType;

    /**
     * 授权开始时间(auth_type=1时)
     */
    private Date startAt;

    /**
     * 授权结束时间(auth_type=1时)
     */
    private Date endAt;

    /**
     * 授权次数(auth_type=2时)
     */
    private Integer times;

    /**
     * 是否已过期 (当 usage_type 为 1 时才需要的字段)
     */
    private Boolean expired;

    /**
     * 已经使用的次数 (当 usage_type 为 2 时才需要的字段)
     */
    private Integer usedTimes;

    /**
     * 授权申请时间，精确到毫秒
     */
    private Date applyAt;

    /**
     * 审核结果，0：等待审核中；1：审核通过；2：审核拒绝
     */
    private MetaDataAuthOptionEnum auditOption;

    /**
     * 审核意见 (允许""字符)
     */
    private String auditDesc;

    /**
     * 授权审核时间，精确到毫秒
     */
    private Date auditAt;

    /**
     * 授权签名hex
     */
    private String authSign;

    /**
     * 数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的>)
     */
    private MetaDataAuthStatusEnum authStatus;

    /**
     * 修改时间
     */
    private Date updateAt;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
