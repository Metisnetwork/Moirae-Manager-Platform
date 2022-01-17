package com.moirae.rosettaflow.grpc.task.req.dto;

import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
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
     * 产生事件的partyId (单个组织可以担任任务的多个party, 区分是哪一方产生的event)
     */
    private String partyId;
    /**
     * 事件内容
     */
    private String content;
    /**
     * 事件产生时间
     */
    private Long createAt;

    /**
     * 组织名称
     */
    private String name;
    /**
     * 组织中调度服务的 nodeId
     */
    private String nodeId;
    /**
     * 组织的身份标识Id
     */
    private String identityId;
}
