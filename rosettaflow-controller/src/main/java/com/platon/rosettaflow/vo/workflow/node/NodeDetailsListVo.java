package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工作流节点详情列表响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点详情列表响应参数")
public class NodeDetailsListVo {

    @ApiModelProperty(value = "工作流节点列表")
    private List<WorkflowNodeVo> workflowNodeVoList;

}
