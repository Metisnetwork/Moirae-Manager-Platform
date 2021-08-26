package com.platon.rosettaflow.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

}
