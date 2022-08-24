package com.datum.platform.vo.publicity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrgVcVo {

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;

    @ApiModelProperty(value = "vc的颁发时间")
    private Date issuanceDate;

    @ApiModelProperty(value = "vc的过期时间")
    private Date expirationDate;

    @ApiModelProperty(value = "组织机构图像url")
    private String imageUrl;

    @ApiModelProperty(value = "持有者组织标识")
    private String issuerIdentityId;

    @ApiModelProperty(value = "颁发者组织名称")
    private String issuerNodeName;

    @ApiModelProperty(value = "持有者组织标识")
    private String holderIdentityId;

    @ApiModelProperty(value = "持有者组织名称")
    private String holderNodeName;

    @ApiModelProperty(value = "持有者组织图像url")
    private String holderImageUrl;
}
