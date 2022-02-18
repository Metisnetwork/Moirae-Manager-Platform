package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * dc_task_result_consumer
 * @author
 */
@Data
@TableName(value = "dc_task_result_consumer")
public class TaskResultConsumer implements Serializable {
    /**
     * 任务ID,hash
     */
    private String taskId;

    /**
     * 结果消费者组织身份ID
     */
    private String consumerIdentityId;

    /**
     * 任务参与方在本次任务中的唯一识别ID
     */
    private String consumerPartyId;

    /**
     * 结果产生者的组织身份ID
     */
    private String producerIdentityId;

    /**
     * 任务参与方在本次任务中的唯一识别ID
     */
    private String producerPartyId;

    private static final long serialVersionUID = 1L;
}
