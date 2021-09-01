package com.platon.rosettaflow.grpc.task.req.dto;

import com.platon.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import lombok.Data;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/11
 * @description 任务详情
 */
@Data
public class TaskDetailDto {
    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务发起方
     */
    private OrganizationIdentityInfoDto owner;
    /**
     * 算法提供方 (目前就是和 任务发起方是同一个 ...)
     */
    private OrganizationIdentityInfoDto algoSupplier;
    /**
     * 数据提供方
     */
    private List<TaskDataSupplierDto> dataSuppliers;
    /**
     * 算力提供方
     */
    private List<TaskPowerSupplierDto> powerSuppliers;
    /**
     * 任务结果方
     */
    private List<OrganizationIdentityInfoDto> receivers;
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
     * 任务的状态 (0: 未知; 1: 等在中; 2: 计算中; 3: 失败; 4: 成功)
     */
    private Integer state;
    /**
     * 任务所需资源声明
     */
    private TaskResourceCostDeclareDto operationCost;
    /**
     * 任务描述 (非必须)
     */
    private String desc;
}
