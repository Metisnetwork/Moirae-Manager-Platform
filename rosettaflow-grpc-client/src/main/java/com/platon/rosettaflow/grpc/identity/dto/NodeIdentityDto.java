package com.platon.rosettaflow.grpc.identity.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 查询自己组织的identity信息
 */
@Data
public class NodeIdentityDto {

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
