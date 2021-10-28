package com.moirae.rosettaflow.grpc.metadata.resp.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 数据授权列表响应对象
 */
@Data
public class GetMetaDataAuthorityListResponseDto {
    /**
     * 响应码
     */
    private Integer status;
    /**
     * 错误信息
     */
    private String msg;
    /**
     * 数据授权信息列表
     */
    private List<GetMetaDataAuthorityDto> getMetaDataAuthorityDtoList;
}
