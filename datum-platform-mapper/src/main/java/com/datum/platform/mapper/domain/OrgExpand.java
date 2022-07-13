package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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
     * 是否委员会成员: 0-否，1-是
     */
    private Boolean isAuthority;

    /**
     * 是否已认证: 0-否，1-是
     */
    private Boolean isCertified;

    /**
     * 加入委员会的时间
     */
    private Date authorityJoinTime;

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

    /**
     * 组织名称
     */
    @TableField(exist = false)
    private String nodeName;

    /**
     * 组织机构图像url
     */
    @TableField(exist = false)
    private String imageUrl;
}
