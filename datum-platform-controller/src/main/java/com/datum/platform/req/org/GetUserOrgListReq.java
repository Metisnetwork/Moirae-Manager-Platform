package com.datum.platform.req.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class GetUserOrgListReq {

    @ApiModelProperty(value = "是否包含元数据, 包含=true, 不包含=false, 默认值 false")
    private Boolean includeData = false;
}
