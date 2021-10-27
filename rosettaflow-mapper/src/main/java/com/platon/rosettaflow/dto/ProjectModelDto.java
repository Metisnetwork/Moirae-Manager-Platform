package com.platon.rosettaflow.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/10/27
 */
@Data
public class ProjectModelDto {

    /**
     * 模型主键id
     */
    private Long modelId;

    /**
     * 模型所属的机构id
     */
    private String originId;

    /**
     * 模型名称
     */
    private String fileName;

    /**
     * 模型的元数据id
     */
    private String metadataId;

    /**
     * 文件路径
     */
    private String filePath;
}
