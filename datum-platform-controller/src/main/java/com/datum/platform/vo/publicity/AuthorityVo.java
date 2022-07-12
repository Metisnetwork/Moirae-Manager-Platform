package com.datum.platform.vo.publicity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AuthorityVo {

    @ApiModelProperty(value = "组织身份id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String nodeName;

    @ApiModelProperty(value = "组织机构图像url")
    private String imageUrl;

    @ApiModelProperty(value = "组织加入时间")
    private Date authorityJoinTime;

}
