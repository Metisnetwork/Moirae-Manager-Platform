package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "dc_task_event")
public class TaskEvent implements Serializable {

    /**
     * 任务表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务ID,hash
     */
    private String taskId;
    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 产生事件的组织身份ID
     */
    private String identityId;

    /**
     * 产生事件的组织身份ID
     */
    private String partyId;

    /**
     * 产生事件的时间
     */
    private Date eventAt;

    /**
     * 事件内容
     */
    private String eventContent;

    private static final long serialVersionUID = 1L;
}
