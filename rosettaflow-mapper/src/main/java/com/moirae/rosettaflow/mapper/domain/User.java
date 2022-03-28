package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "mo_user")
public class User implements Serializable {

    /**
     * 用户钱包地址
     */
    @TableId
    private String address;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 是否有效: 0-否，1-是
     */
    private Boolean isValid;
    /**
     * 默认连接的组织id
     */
    private String orgIdentityId;
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
