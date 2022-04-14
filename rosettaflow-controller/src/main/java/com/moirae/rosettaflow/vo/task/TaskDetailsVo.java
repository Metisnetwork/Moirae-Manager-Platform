package com.moirae.rosettaflow.vo.task;

import com.moirae.rosettaflow.service.dto.task.TaskEventDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "任务详情")
public class TaskDetailsVo extends BaseTaskVo {

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
    private List<TaskEventDto> eventList;
}
