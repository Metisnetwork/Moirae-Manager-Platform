package com.moirae.rosettaflow.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "任务详情")
public class TaskDetailsVo extends BaseTaskVo {

    @ApiModelProperty(value = "任务发起方")
    private TaskSponsorVo taskSponsor;

    @ApiModelProperty(value = "算法提供方")
    private AlgoProviderVo algoProvider;

    @ApiModelProperty(value = "数据提供方")
    private List<DataProviderVo> dataProviderList;

    @ApiModelProperty(value = "算力提供方")
    private List<PowerProviderVo> powerProviderList;

    @ApiModelProperty(value = "结果接收方")
    private List<ResultReceiverVo> resultReceiverList;
}
