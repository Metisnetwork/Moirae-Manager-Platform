package com.platon.rosettaflow.grpc.metadata.req.dto;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 元数据使用授权请求对象
 */
@Data
public class MetaDataAuthorityDto {
    /**
     * 元数据所属的组织信息
     */
    private NodeIdentityDto owner;
    /**
     * 元数据Id
     */
    private String metaDataId;
    /**
     * 元数据怎么使用
     */
    private MetaDataUsageDto metaDataUsageDto;
}
