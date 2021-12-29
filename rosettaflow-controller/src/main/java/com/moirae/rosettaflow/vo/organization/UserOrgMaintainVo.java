package com.moirae.rosettaflow.vo.organization;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moirae.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/12/15
 */
@Data
@ApiModel(value = "用户维护组织信息详情返回参数")
public class UserOrgMaintainVo {

    @ApiModelProperty(value = "组织的身份名称")
    private String nodeName;

    @ApiModelProperty(value = "组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "组织的ip")
    private String identityIp;

    @ApiModelProperty(value = "组织的端口")
    private Integer identityPort;

    @ApiModelProperty(value = "是否公共可看的：0-否，1-是")
    private Byte publicFlag;

    @ApiModelProperty(value = "是否可连接: 0-无效，1- 有效")
    private Byte validFlag;

    @ApiModelProperty(value = "是否已连接：0-否，1-是")
    private Byte connectFlag;
}
