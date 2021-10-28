package com.moirae.rosettaflow.grpc.metadata.resp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hudenian
 * @date 2021/9/18
 * @description (本组织) 单个元数据详情
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SelfMetaDataDetailResponseDto extends MetaDataDetailResponseDto {
    /**
     * 是否为本组织本地元数据 (不对外的元数据, true: 是本组织元数据; false: 不是)
     */
    private Boolean isLocal;
}
