package com.moirae.rosettaflow.grpc.metadata.resp.dto;

import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataDetailDto;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 获取元数据详情响应体
 */
@Data
public class MetaDataDetailResponseDto {
    /**
     * 元数据的拥有者
     */
    private NodeIdentityDto owner;

    /**
     * 元文件详情主体
     */
    private MetaDataDetailDto metaDataDetailDto;
}
