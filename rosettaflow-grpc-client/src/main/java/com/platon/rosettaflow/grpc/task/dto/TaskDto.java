package com.platon.rosettaflow.grpc.task.dto;

import lombok.Data;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/4
 */
@Data
public class TaskDto {

    /**
     * 工作流节点id
     */
    private Long workFlowNodeId;
    
    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务发起者 组织信息
     */
    private TaskOrganizationIdentityInfoDto owner;

    /**
     * 任务的数据提供方, 包含发起者和参与方
     */
    private List<TaskDataSupplierDeclareDto> taskDataSupplierDeclareDtoList;

    /**
     * 算力提供方未来要用的 标签
     */
    private List<String> powerPartyIds;

    /**
     * 任务结果接受者
     */
    private List<TaskResultReceiverDeclareDto> taskResultReceiverDeclareDtoList;

    /**
     * 任务的所需操作成本 (定义任务的大小)
     */
    private TaskOperationCostDeclareDto taskOperationCostDeclareDto;

    /**
     * 算法代码（python代码）
     */
    private String calculateContractCode;

    private String dataSplitContractCode;

    private String contractExtraParams;

}
