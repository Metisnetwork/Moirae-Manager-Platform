package com.moirae.rosettaflow.grpc.metadata.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 元数据授权申请
 */
@Data
public class ApplyMetaDataAuthorityRequestDto {
    /**
     * 发起任务的用户的信息 (task是属于用户的)
     */
    private String user;
    /**
     * 用户类型 (0: 未定义; 1: 第二地址; 2: Alaya地址; 3: PlatON地址)
     */
    private Integer userType;
    /**
     * 元数据使用授权信息
     */
    private MetaDataAuthorityDto auth;
    /**
     * 发起数据授权申请的账户的签名
     */
    private String sign;

}
