package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_project
 *
 * @author admin
 */
@Data
@TableName(value = "t_project")
public class Project implements Serializable {
    /**
     * 项目ID(自增长)
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
     * 版本标识，用于逻辑删除
     */
    private Long delVersion;

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