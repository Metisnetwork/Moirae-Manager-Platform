package com.moirae.rosettaflow.vo.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "任务列表")
public class TaskVo extends BaseTaskVo {

    @ApiModelProperty(value = "任务类别")
    public String getTaskType(){
        if (Objects.nonNull(LanguageContext.get()) && LanguageContext.get().equals(SysConstant.EN_US)) {
            return algorithmNameEn;
        }
        return algorithmName;
    }

    @JsonIgnore
    private String algorithmName;
    @JsonIgnore
    private String algorithmNameEn;
}
