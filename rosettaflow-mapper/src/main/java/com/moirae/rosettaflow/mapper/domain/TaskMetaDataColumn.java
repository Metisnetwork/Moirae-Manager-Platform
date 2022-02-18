package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * dc_task_meta_data_column
 * @author
 */
@Data
@TableName(value = "dc_task_meta_data_column")
public class TaskMetaDataColumn implements Serializable {
    /**
     * 任务ID,hash
     */
    private String taskId;

    /**
     * 参与任务的元数据ID
     */
    private String metaDataId;

    /**
     * 元数据在此次任务中的参与计算的字段索引序号
     */
    private Integer selectedColumnIdx;

    private static final long serialVersionUID = 1L;
}
