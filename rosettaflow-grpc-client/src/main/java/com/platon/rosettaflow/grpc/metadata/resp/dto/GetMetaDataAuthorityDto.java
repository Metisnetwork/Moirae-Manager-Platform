package com.platon.rosettaflow.grpc.metadata.resp.dto;

import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/23
 * @description 数据授权信息
 */
@Data
public class GetMetaDataAuthorityDto {
    /**
     * 元数据授权申请Id
     */
    private String metaDataAuthId;
    /**
     * 发起任务的用户的信息 (task是属于用户的)
     */
    private String user;
    /**
     * 用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址)
     */
    private Integer userType;
    /**
     * 元数据使用授权信息
     */
    private MetaDataAuthorityDto metaDataAuthorityDto;
    /**
     * 审核结果：0-等待审核中，1-审核通过，2-审核拒绝
     */
    private Integer auditMetaDataOption;
    /**
     * 发起授权申请的时间 (单位: ms)
     */
    private Long applyAt;
    /**
     * 审核授权申请的时间 (单位: ms)
     */
    private Long auditAt;
}
