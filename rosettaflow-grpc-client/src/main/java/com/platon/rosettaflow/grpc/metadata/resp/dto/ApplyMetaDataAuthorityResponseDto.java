package com.platon.rosettaflow.grpc.metadata.resp.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 元数据授权申请响应dto
 */
@Data
public class ApplyMetaDataAuthorityResponseDto {
    /**
     * 响应码
     */
    private Integer status;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 元数据授权申请Id
     */
    private String metaDataAuthId;
}
