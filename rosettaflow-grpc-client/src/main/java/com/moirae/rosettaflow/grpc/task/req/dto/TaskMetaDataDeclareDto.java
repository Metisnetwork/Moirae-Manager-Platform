package com.moirae.rosettaflow.grpc.task.req.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/4
 */
@Data
public class TaskMetaDataDeclareDto {

    /**
     * 元数据id
     */
    private String metaDataId;
    /**
     * 该任务用来作为计算时数据表的索引列 (类比数据库中的id列意思)
     */
    private Integer keyColumn;

    /**
     * 该任务用来参与计算的原始数据的第几列数组 (类比数据库中非id列意思)
     */
    private List<Integer> selectedColumns;
}
