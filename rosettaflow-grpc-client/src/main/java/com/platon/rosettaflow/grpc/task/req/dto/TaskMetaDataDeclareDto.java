package com.platon.rosettaflow.grpc.task.req.dto;

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
     * 该任务使用原始数据的第几列
     */
    private List<Integer> columnIndexList;
}
