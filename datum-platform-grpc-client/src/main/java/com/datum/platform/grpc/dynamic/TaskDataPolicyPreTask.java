package com.datum.platform.grpc.dynamic;

import lombok.Data;

import java.util.List;

@Data
public class TaskDataPolicyPreTask {
    private String partyId;
    private String taskId;
    // 输入数据的类型，0:unknown, 1:origin_data, 2:psi_output, 3:model
    private Integer inputType;
    private String keyColumnName;
    private List<String> selectedColumnNames;
}
