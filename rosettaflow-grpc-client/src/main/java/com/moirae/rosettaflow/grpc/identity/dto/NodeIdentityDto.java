package com.moirae.rosettaflow.grpc.identity.dto;

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
    private String nodeName;
    /**
     * 组织中调度服务的 nodeId
     */
    private String nodeId;
    /**
     * 组织的身份标识Id
     */
    private String identityId;
    /**
     * the status for organization(deleted/normal)
     */
    private Integer status;
    /**
     * 数据的最后更新时间，UTC毫秒数
     */
    private Long updateAt;
}
