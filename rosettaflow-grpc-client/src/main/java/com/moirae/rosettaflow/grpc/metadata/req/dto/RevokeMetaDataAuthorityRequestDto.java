package com.moirae.rosettaflow.grpc.metadata.req.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 撤销元数据授权申请
 */
@Data
public class RevokeMetaDataAuthorityRequestDto {
    /**
     * 发起任务的用户的信息 (task是属于用户的)
     */
    private String user;
    /**
     * 用户类型 (0: 未定义; 1: 第二地址; 2: 测试网地址; 3: 主网地址)
     */
    private Integer userType;
    /**
     * 元数据授权申请Id
     */
    private String metadataAuthId;
    /**
     * 撤销数据授权申请的账户的签名
     */
    private String sign;

}
