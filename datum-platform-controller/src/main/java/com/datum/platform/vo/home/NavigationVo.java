package com.datum.platform.vo.home;

import com.datum.platform.common.enums.NavigationTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询导航")
public class NavigationVo {

    @ApiModelProperty(value = "类型")
    private NavigationTypeEnum type;

    @ApiModelProperty(value = "标识, 任务id或组织标识")
    private String id;
}
