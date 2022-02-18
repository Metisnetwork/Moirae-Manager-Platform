package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * dc_task_algo_provider
 * @author
 */
@Data
@TableName(value = "dc_task_algo_provider")
public class TaskAlgoProvider implements Serializable {
    /**
     * 任务ID,hash
     */
    @TableId
    private String taskId;

    /**
     * 算法提供者组织身份ID
     */
    private String identityId;

    /**
     * 任务参与方在本次任务中的唯一识别ID
     */
    private String partyId;

    private static final long serialVersionUID = 1L;
}
