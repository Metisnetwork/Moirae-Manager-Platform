package com.platon.rosettaflow.dto;

import com.platon.rosettaflow.mapper.domain.Project;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 项目信息转换dto
 *
 * @author houz
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectDto extends Project {

    /**
     * 项目ID(自增长)
     */
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

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
