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
 * 工作流任务输入表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_task_input")
public class WorkflowTaskInput implements Serializable {


    /**
     * 工作流任务配置id
     */
    @TableId("workflow_task_id")
    private Long workflowTaskId;

    /**
     * 数据表ID
     */
    @TableId("meta_data_id")
    private String metaDataId;

    /**
     * 组织的身份标识Id
     */
    @TableField("identity_id")
    private String identityId;

    /**
     * ID列(列索引)(存id值)
     */
    @TableField("key_column")
    private Long keyColumn;

    /**
     * 因变量(标签)(存id值)
     */
    @TableField("dependent_variable")
    private Long dependentVariable;

    /**
     * 数据字段ID索引(存id值)
     */
    @TableField("data_column_ids")
    private String dataColumnIds;

    /**
     * 任务里面定义的 (p0 -> pN 方 ...)
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
