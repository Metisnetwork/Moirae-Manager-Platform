package com.moirae.rosettaflow.grpc.task.req.dto;

import com.moirae.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
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
    private OrganizationIdentityInfoDto taskOrganizationIdentityInfoDto;

    /**
     * 任务使用的元数据信息
     */
    private TaskMetaDataDeclareDto taskMetaDataDeclareDto;
}
