package com.platon.rosettaflow.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author admin
 * @date 2021/7/20
 */
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机
     */
    private String phone;

    /**
     * 性别 0男 1女
     */
    private Byte sex;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态 0锁定 1有效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 用户登录token
     */
    private String token;
    /**
     * 用户角色数组
     */
    private String[] roles;
    /**
     * 用户权限数组
     */
    private String[] permissions;

}
