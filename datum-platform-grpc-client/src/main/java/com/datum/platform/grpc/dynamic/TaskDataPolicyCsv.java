package com.datum.platform.grpc.dynamic;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskDataPolicyCsv extends TaskDataPolicyHaveConsume {
    private String partyId;
    private String metadataId;
    private String metadataName;
    // 输入数据的类型，0:unknown, 1:origin_data, 2:psi_output, 3:model
    private Integer inputType;
    private Long keyColumn;
    private List<Integer> selectedColumns;
}
