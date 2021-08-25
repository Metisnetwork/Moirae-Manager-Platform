package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_project_template
 * @author 
 */
@Data
@TableName(value = "t_project_template")
public class ProjectTemplate implements Serializable {
    /**
     * 项目模板表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id(创建者id)
     */
    private Long userId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目描述
     */
    private String projectDesc;

    /**
     * 状态: 0-无效，1- 有效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}