package com.platon.rosettaflow.dto;

import com.platon.rosettaflow.mapper.domain.Workflow;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/17
 * @description 功能描述
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WorkflowDto extends Workflow {
    /**
     * 起始节点
     */
    private Integer startNode;
    /**
     * 截止节点
     */
    private Integer endNode;
    /**
     * 是否是job任务
     */
    private boolean jobFlg;
    /**
     * 创建者
     */
    private String userName;
    /**
     * 项目协同方
     */
    private String coordinators;

    /**
     * 工作流节点列表
     */
    private List<WorkflowNodeDto> workflowNodeDtoList;

}
