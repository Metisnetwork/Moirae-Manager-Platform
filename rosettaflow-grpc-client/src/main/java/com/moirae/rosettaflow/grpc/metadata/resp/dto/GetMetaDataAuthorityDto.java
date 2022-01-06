package com.moirae.rosettaflow.grpc.metadata.resp.dto;

import com.moirae.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
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
     * 用户类型 (0: 未定义; 1: 第二地址; 2: 测试网地址; 3: 主网地址)
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
     * 审核意见 (允许""字符)
     */
    private String auditSuggestion;
    /**
     * 对应数据授权信息中元数据的使用实况
     */
    private MetadataUsedQuoDto metadataUsedQuoDto;
    /**
     * 发起授权申请的时间 (单位: ms)
     */
    private Long applyAt;
    /**
     * 审核授权申请的时间 (单位: ms)
     */
    private Long auditAt;
    /**
     * 数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>;)
     */
    private Integer metadataAuthorityState;
    /**
     * 数据的最后更新时间，UTC毫秒数
     */
    private Long updateAt;
}
