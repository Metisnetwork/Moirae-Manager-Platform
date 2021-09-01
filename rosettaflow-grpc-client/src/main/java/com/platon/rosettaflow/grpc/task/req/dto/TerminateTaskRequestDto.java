package com.platon.rosettaflow.grpc.task.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/9/1
 * @description 功能描述
 */
@Data
public class TerminateTaskRequestDto {
    /**
     * 发起任务的用户的信息 (task是属于用户的)
     */
    private String user;
    /**
     * 用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址)
     */
    private Integer userType;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 发起任务的账户的签名
     */
    private String sign;
}
