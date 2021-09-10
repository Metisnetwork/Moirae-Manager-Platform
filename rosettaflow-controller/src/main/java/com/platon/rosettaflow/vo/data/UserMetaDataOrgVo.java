package com.platon.rosettaflow.vo.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.platon.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author  hudenian
 * @date    2021/9/10
 * @description 用户已授权元数据所有机构信息
 */
@Data
@ApiModel
public class UserMetaDataOrgVo {

    @ApiModelProperty(value = "用户元数据表ID")
    private Long id;

    @ApiModelProperty(value = "资源所属组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "资源所属组织名称")
    private String identityName;
}
