package com.platon.rosettaflow.dto;

import lombok.Data;

import java.util.Date;

/**
 * 项目成员转换dto
 * @author houz
 */
@Data
public class ProjMemberDto {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 项目成员id
     */
    private String memberId;

    /**
     * 用户角色
     */
    private Byte role;

    /**
     * 创建时间
     */
    private Date createTime;

}
