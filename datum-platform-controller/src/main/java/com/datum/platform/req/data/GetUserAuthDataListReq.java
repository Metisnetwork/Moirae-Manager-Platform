package com.datum.platform.req.data;

import com.datum.platform.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class GetUserAuthDataListReq extends CommonPageReq {
    @ApiModelProperty(value = "搜索关键字(凭证名称或元数据名称关键字)")
    private String keyword;
}
