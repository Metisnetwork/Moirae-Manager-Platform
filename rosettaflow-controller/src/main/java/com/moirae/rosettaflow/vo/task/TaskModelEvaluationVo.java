package com.moirae.rosettaflow.vo.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工作流节点运行结果
 * @author hudenian
 * @date 2021/9/17
 * @description
 */
@Data
@ApiModel(value = "工作流节点运行结果")
public class TaskModelEvaluationVo {

    @ApiModelProperty(value = "对应模型文件id")
    private String metadataId;


    //TODO 指标定义 动态 还是 固定

}
