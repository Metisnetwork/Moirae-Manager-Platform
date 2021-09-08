package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * t_project_member
 * @author 
 */
@Data
@TableName(value = "t_project_member")
public class ProjectMember implements Serializable {
    /**
     * 项目成员ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色：1-管理员，2-编辑着, 3-查看着
     */
    private Byte role;

    /**
     * 版本标识，用于逻辑删除
     */
    private Long delVersion;

    /**
     * 状态: 0-无效，1- 有效
     */
    @TableField(value = "`status`")
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