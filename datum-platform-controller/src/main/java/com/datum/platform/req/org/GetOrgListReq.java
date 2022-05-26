package com.datum.platform.req.org;

import com.datum.platform.common.enums.OrgOrderByEnum;
import com.datum.platform.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织列表查询")
public class GetOrgListReq extends CommonPageReq {

    @ApiModelProperty(value = "搜索关键字(身份标识、组织名称关键字)")
    private String keyword;

    @ApiModelProperty(value = "排序字段")
    private OrgOrderByEnum orderBy;
}
