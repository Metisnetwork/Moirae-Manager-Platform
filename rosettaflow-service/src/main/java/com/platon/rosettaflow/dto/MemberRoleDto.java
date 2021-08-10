package com.platon.rosettaflow.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author admin
 * @date 2021/7/11
 */
@Data
public class MemberRoleDto {

    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
