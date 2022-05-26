package com.datum.platform.req.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 创建ip及port与组织绑定关系请求对象
 *
 * @author hudenian
 * @date 2021/12/15
 */
@Data
@ApiModel(value = "创建ip及port与组织绑定关系请求")
public class JoinOrgReq {

    @ApiModelProperty(value = "组织的ip", required = true)
    @Pattern(regexp = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$", message = "{organization.ip.error}")
    private String identityIp;

    @ApiModelProperty(value = "组织的端口", required = true)
    @Min(value = 1, message = "{organization.port.error}")
    @Max(value = 65535, message = "{organization.port.error}")
    private Integer identityPort;
}
