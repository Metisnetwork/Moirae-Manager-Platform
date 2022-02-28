package com.moirae.rosettaflow.dto;

import lombok.Data;

import java.util.Date;

/**
 * 项目成员转换dto
 * @author houz
 */
@Data
public class ProjectMemberDto {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 项目成员id
     */
    private Long memberId;

    /**
     * 用户角色
     */
    private Byte role;

    /**
     * 创建时间
     */
    private Date createTime;

}
