package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/9/17
 * @description 工作流节点运行结果
 */
@Data
public class WorkflowNodeResultVo {

    @ApiModelProperty(value = "运行结果测试数据")
    private String result;
}
