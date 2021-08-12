package com.platon.rosettaflow.grpc.task.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/11
 * @description 任务详情
 */
@Data
public class TaskDetailShowDto {
    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务发起发
     */
    private TaskOrganizationIdentityInfoDto owner;
    /**
     * 算法提供方 (目前就是和 任务发起方是同一个 ...)
     */
    private TaskOrganizationIdentityInfoDto algoSupplier;
    /**
     * 数据提供方
     */
    private List<TaskDataSupplierShowDto> dataSuppliers;
    /**
     * 算力提供方
     */
    private List<TaskPowerSupplierShowDto> powerSuppliers;
    /**
     * 任务结果方
     */
    private List<TaskOrganizationIdentityInfoDto> receivers;
    /**
     * 任务发起时间
     */
    private Long createAt;
    /**
     * 任务启动时间
     */
    private Long startAt;
    /**
     * 任务结束时间
     */
    private Long endAt;
    /**
     * 任务的状态 (pending: 等在中; running: 计算中; failed: 失败; success: 成功)
     */
    private String state;
    /**
     * 任务所需资源声明
     */
    private TaskOperationCostDeclareDto operationCost;
}
