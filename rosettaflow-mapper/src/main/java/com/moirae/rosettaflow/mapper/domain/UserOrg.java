package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_user_org
 *
 * @author admin
 */
@Data
@TableName(value = "t_user_org")
public class UserOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户组织关系
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户的钱包地址
     */
    private String userAddress;

    /**
     * 组织的身份标识Id
     */
    private String  orgIdentityId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
