package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    private Long workflowTaskId;

    /**
     * 数据表ID
     */
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
     * 用于排序的字段
     */
    private Integer sortKey;

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
    @TableField(exist = false)
    private Org org;
}
