package com.platon.rosettaflow.req.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 查询项目列表请求参数
 * @author houz
 */
@Data
@ApiModel(value = "查询项目列表请求参数")
public class ProjListReq {

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "当前页码, 默认第一页")
    @NotNull(message = "{page.number.notBlank}")
    @Positive(message = "{page.number.positive}")
    private Long current;

    @ApiModelProperty(value = "每页大小, 默认每页十条. 最小支持每页1条, 最大支持每页1000条")
    @NotNull(message = "{page.number.notBlank}")
    @Min(value = 1L, message = "{each.page.row.min}")
    @Max(value = 1000L, message = "{each.page.row.max}")
    private Long size;


}
