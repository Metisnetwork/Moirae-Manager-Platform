package com.platon.rosettaflow.grpc.task.req.dto;

import com.platon.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
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
     * 发起任务的用户的信息 (task是属于用户的)
     */
    private String user;
    /**
     * 用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址)
     */
    private Integer userType;

    /**
     * 任务发起者 组织信息
     */
    private OrganizationIdentityInfoDto sender;

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
    /**
     *  数据分片合约
     */
    private String dataSplitContractCode;
    /**
     * 合约调用的额外可变入参 (json 字符串, 根据算法来)
     */
    private String contractExtraParams;
    /**
     * 发起任务的账户的签名
     */
    private String sign;

}
