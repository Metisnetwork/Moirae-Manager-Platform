package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "mo_org_expand")
public class OrgExpand implements Serializable {
    /**
     * 身份认证标识的id
     */
    @TableId
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
     * 是否公共组织: 0-否，1-是
     */
    private Boolean isPublic;

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
