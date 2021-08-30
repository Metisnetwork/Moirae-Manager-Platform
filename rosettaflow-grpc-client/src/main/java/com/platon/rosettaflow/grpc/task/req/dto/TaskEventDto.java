package com.platon.rosettaflow.grpc.task.req.dto;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/12
 * @description 某个任务的全部事件响应体
 */
@Data
public class TaskEventDto {
    /**
     * 事件类型码
     */
    private String type;
    /**
     * 事件对应的任务id
     */
    private String taskId;
    /**
     * 产生事件的节点身份
     */
    private NodeIdentityDto owner;
    /**
     * 事件内容
     */
    private String content;
    /**
     * 事件产生时间
     */
    private Long createAt;
}
