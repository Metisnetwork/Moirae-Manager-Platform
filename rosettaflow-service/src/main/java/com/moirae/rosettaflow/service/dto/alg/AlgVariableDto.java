package com.moirae.rosettaflow.service.dto.alg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import com.moirae.rosettaflow.mapper.enums.AlgorithmVariableTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "算法变量")
public class AlgVariableDto {

    @ApiModelProperty(value = "变量key")
    private String varKey;

    @ApiModelProperty(value = "变量类型. 1-boolean, 2-number, 3-string, 4-numberArray, 5-stringArray")
    private AlgorithmVariableTypeEnum varType;

    @ApiModelProperty(value = "变量默认值")
    private String varValue;

    @ApiModelProperty(value = "变量中文描述")
    private String varDesc;

    @JsonIgnore
    private String varDescEn;

    public String getVarDesc(){
        return LanguageContext.getByLanguage(varDesc, varDescEn);
    }
}
