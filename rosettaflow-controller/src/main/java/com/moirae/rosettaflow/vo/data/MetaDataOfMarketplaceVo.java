package com.moirae.rosettaflow.vo.data;

import com.moirae.rosettaflow.mapper.enums.MetaDataAuthOptionEnum;
import com.moirae.rosettaflow.mapper.enums.MetaDataAuthStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据信息")
public class MetaDataOfMarketplaceVo extends BaseMetaDataVo {

    @ApiModelProperty(value = "元数据所属行业  1：金融业（银行）、2：金融业（保险）、3：金融业（证券）、4：金融业（其他）、5：ICT、 6：制造业、 7：能源业、 8：交通运输业、 9 ：医疗健康业、 10 ：公共服务业、 11：传媒广告业、 12 ：其他行业'")
    private String industry;

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;

    @ApiModelProperty(value = "数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的>)")
    private MetaDataAuthStatusEnum authStatus;

    @ApiModelProperty(value = "审核结果，0：等待审核中；1：审核通过；2：审核拒绝")
    private MetaDataAuthOptionEnum auditOption;

    @ApiModelProperty(value = "数据授权信息的状态 (0: 未知; 1: 还未发布的数据授权; 2: 已发布的数据授权; 3: 已撤销的数据授权 <失效前主动撤回的>; 4: 已经失效的数据授权 <过期or达到使用上限的>)-后期调整为metaDataAuthStatus")
    public MetaDataAuthStatusEnum getAuthMetadataState(){
        return authStatus;
    }

    @ApiModelProperty(value = "审核结果，-1-未知(1.未登录故获取不到授权状态 2.用户未申请使用元数据) 0：等待审核中；1：审核通过；2：审核拒绝 -后期调整为metaDataAuthStatus")
    public MetaDataAuthOptionEnum getAuthStatus(){
        return auditOption;
    }

    @ApiModelProperty(value = "元数据名称")
    public String getDataName(){
        return getMetaDataName();
    }

    @ApiModelProperty(value = "组织名称")
    public String getIdentityName(){
        return nodeName;
    }
}
