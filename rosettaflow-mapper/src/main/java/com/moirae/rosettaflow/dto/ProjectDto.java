package com.moirae.rosettaflow.dto;

import lombok.Data;

import java.util.Date;

/**
 * 项目信息转换dto
 * @author houz
 */
@Data
public class ProjectDto {

    /**
     * 项目ID(自增长)
     */
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户成员角色
     */
    private Byte role;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目描述
     */
    private String projectDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 项目模板ID(自增长)
     */
    private Long projectTempId;

}
