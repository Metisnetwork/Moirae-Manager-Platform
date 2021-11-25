package com.moirae.rosettaflow.grpc.metadata.resp.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 撤销元数据授权申请响应dto
 */
@Data
public class RevokeMetadataAuthorityResponseDto {
    /**
     * 响应码
     */
    private Integer status;
    /**
     * 错误信息
     */
    private String msg;

}
