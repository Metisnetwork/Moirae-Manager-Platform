package com.platon.rosettaflow.grpc.task.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/11
 * @description 任务数据提供方信息
 */
@Data
public class TaskDataSupplierDto {
    /**
     * 身份信息
     */
    private TaskOrganizationIdentityInfoDto memberInfo;
    /**
     * 元数据Id
     */
    private String metaDataId;
    /**
     * 元数据名称
     */
    private String metaDataName;
}
