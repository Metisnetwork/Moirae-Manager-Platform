package com.datum.platform.service.dto.statistics;

import com.datum.platform.common.enums.NavigationTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NavigationDto {

    @ApiModelProperty(value = "导航类型")
    private NavigationTypeEnum type;

    @ApiModelProperty(value = "导航id")
    private String id;
}
