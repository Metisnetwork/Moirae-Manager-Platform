package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * dc_task_data_provider
 * @author
 */
@Data
@TableName(value = "dc_task_data_provider")
public class TaskDataProvider implements Serializable {
    /**
     * 任务ID,hash
     */
    private String taskId;

    /**
     * 参与任务的元数据ID
     */
    private String metaDataId;

    /**
     * (冗余)参与任务的元数据的所属组织的identity_id
     */
    private String identityId;

    /**
     * 任务参与方在本次任务中的唯一识别ID
     */
    private String partyId;

    /**
     * 元数据在此次任务中的主键列下标索引序号
     */
    private Integer keyColumnIdx;

    private static final long serialVersionUID = 1L;
}
