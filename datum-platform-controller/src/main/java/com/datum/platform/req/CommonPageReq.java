package com.datum.platform.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

/**
 * @author juzix
 */
@ApiModel(value = "分页数据请求页码")
@Data
public class CommonPageReq {

    @ApiModelProperty(value = "当前页码, 默认第一页")
    @Positive(message = "{page.number.positive}")
    private Long current = 1L;

    @ApiModelProperty(value = "每页大小, 默认每页十条. 最小支持每页1条, 最大支持每页1000条")
    @Min(value = 1L, message = "{each.page.row.min}")
    @Max(value = 1000L, message = "{each.page.row.max}")
    private Long size = 10L;
}
