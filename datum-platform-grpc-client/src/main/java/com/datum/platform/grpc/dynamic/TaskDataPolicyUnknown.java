package com.datum.platform.grpc.dynamic;

import lombok.Data;

@Data
public class TaskDataPolicyUnknown {
    private String partyId;
//    private String metadataId;
//    private String metadataName;
    // 输入数据的类型，0:unknown, 1:origin_data, 2:psi_output, 3:model
    private Integer inputType;
    private String taskId;//0.5.1
}
