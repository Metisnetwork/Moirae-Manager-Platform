package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.common.utils.LanguageContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "计算流程响应对象")
public class CalculationProcessDto {

    @ApiModelProperty(value = "配置ID")
    private Long calculationProcessId;

    @ApiModelProperty(value = "配置名称")
    private String name;
    @JsonIgnore
    private String nameEn;

    @ApiModelProperty(value = "计算步骤")
    private List<CalculationProcessStepDto> stepItem;

    public String getName(){
       return LanguageContext.getByLanguage(name, nameEn);
    }
}
