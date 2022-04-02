package com.moirae.rosettaflow.vo.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.utils.LanguageContext;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "任务详情")
public class TaskDetailsVo extends BaseTaskVo {

    @ApiModelProperty(value = "任务类别")
    public String getTaskType(){
        if (Objects.nonNull(LanguageContext.get()) && LanguageContext.get().equals(SysConstant.EN_US)) {
            return algorithmNameEn;
        }
        return algorithmName;
    }

    @ApiModelProperty(value = "发起地址")
    private String address;

    @ApiModelProperty(value = "需要的内存")
    private Long requiredMemory;

    @ApiModelProperty(value = "需要的带宽")
    private Long requiredBandwidth;

    @ApiModelProperty(value = "需要的CPU核数")
    private Integer requiredCore;

    @ApiModelProperty(value = "任务发起方")
    private TaskSponsorVo sponsor;

    @ApiModelProperty(value = "结果接收方")
    private List<ResultReceiverVo> resultReceiverList;

    @ApiModelProperty(value = "数据提供方")
    private List<DataProviderVo> dataProviderList;

    @ApiModelProperty(value = "算力提供方")
    private List<PowerProviderVo> powerProviderList;

    @ApiModelProperty(value = "任务事件")
    private List<TaskEventVo> eventList;

    @ApiModelProperty(value = "任务结果文件")
    private List<TaskResultDto> taskResultList;

    @ApiModelProperty(value = "任务结果中模型评估")
    private TaskModelEvaluationVo taskModelEvaluation;

    @JsonIgnore
    private String algorithmName;
    @JsonIgnore
    private String algorithmNameEn;
}
