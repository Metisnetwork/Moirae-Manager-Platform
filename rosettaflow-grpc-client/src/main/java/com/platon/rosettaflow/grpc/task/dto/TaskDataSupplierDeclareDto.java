package com.platon.rosettaflow.grpc.task.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/4
 * @description 任务的数据提供方, 包含发起者和参与方
 */
@Data
public class TaskDataSupplierDeclareDto {
    /**
     * 身份信息
     */
    private TaskOrganizationIdentityInfoDto taskOrganizationIdentityInfoDto;

    /**
     * 任务使用的元数据信息
     */
    private TaskMetaDataDeclareDto taskMetaDataDeclareDto;
}
