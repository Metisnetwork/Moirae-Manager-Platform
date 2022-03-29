package com.moirae.rosettaflow.service.dto;

import com.moirae.rosettaflow.common.enums.NavigationTypeEnum;
import lombok.Data;

@Data
public class NavigationDto {

    private NavigationTypeEnum type;

    private String id;
}
