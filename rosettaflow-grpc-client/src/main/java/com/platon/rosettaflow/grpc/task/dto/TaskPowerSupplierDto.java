package com.platon.rosettaflow.grpc.task.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/11
 * @description 任务算力提供方信息
 */
@Data
public class TaskPowerSupplierDto {
    /**
     * 身份信息
     */
    private TaskOrganizationIdentityInfoDto memberInfo;
    /**
     * 算力使用情况
     */
    private ResourceUsedDetailDto powerInfo;
}
