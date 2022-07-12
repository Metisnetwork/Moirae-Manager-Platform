package com.datum.platform.req.publicity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "获得最新的已认证组织列表请求")
public class GetLatestOrgVcListReq {
    @ApiModelProperty(value = "返回数量, 默认10条.")
    private Integer size = 10;
}
