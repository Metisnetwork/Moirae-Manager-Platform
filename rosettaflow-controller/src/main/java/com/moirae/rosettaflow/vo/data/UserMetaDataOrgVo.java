package com.moirae.rosettaflow.vo.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/9/10
 * @description 用户已授权元数据所有机构信息
 */
@Data
@ApiModel
public class UserMetaDataOrgVo {

    @ApiModelProperty(value = "用户元数据表ID")
    private Long id;

    @ApiModelProperty(value = "资源所属组织中调度服务的 nodeId")
    private String nodeId;

    @ApiModelProperty(value = "资源所属组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "资源所属组织名称")
    private String identityName;

    @ApiModelProperty(value = "数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权;3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的or被拒绝的>;)")
    private Integer authMetadataState;
}
