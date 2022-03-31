package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 项目工作流节点输出表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_task_output")
public class WorkflowTaskOutput implements Serializable {


    /**
     * 工作流任务配置id
     */
    @TableId("workflow_task_id")
    private Long workflowTaskId;

    /**
     * 协同方组织的身份标识Id
     */
    @TableId("identity_id")
    private String identityId;

    /**
     * 存储形式: 1-明文，2:密文
     */
    @TableField("store_pattern")
    private Integer storePattern;

    /**
     * 输出内容
     */
    @TableField("output_content")
    private String outputContent;

    /**
     * 任务里面定义的 (q0 -> qN 方 ...)
     */
    @TableField("party_id")
    private String partyId;

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
