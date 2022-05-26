package com.datum.platform.service.dto.workflow.wizard;

import com.datum.platform.service.dto.workflow.common.OutputDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加工作流节点资源请求对象
 *
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel
public class TrainingAndPredictionOutputDto {

    @ApiModelProperty(value = "向导模式下训练任务的计算资源")
    private OutputDto training;

    @ApiModelProperty(value = "向导模式下预测任务的计算资源")
    private OutputDto prediction;
}
