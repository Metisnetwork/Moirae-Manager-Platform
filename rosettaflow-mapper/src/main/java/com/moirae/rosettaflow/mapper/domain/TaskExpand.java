package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * mo_task_expand
 * @author
 */
@Data
@TableName(value = "mo_task_expand")
public class TaskExpand implements Serializable {
    /**
     * 任务ID,hash
     */
    @TableId
    private String id;

    /**
     * 事件是否同步完成 0-否 1-是
     */
    private Boolean eventSynced;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
