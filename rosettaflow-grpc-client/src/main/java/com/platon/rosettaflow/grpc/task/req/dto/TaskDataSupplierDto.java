package com.platon.rosettaflow.grpc.task.req.dto;

import com.platon.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
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
    private OrganizationIdentityInfoDto memberInfo;
    /**
     * 元数据Id
     */
    private String metaDataId;
    /**
     * 元数据名称
     */
    private String metaDataName;
}
