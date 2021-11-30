package com.moirae.rosettaflow.dto;

import lombok.Data;

/**
 * 元数据信息
 * @author houz
 */
@Data
public class NodeMetaDataDto {

    /**
     * 元数据id
     */
    private String metaDataId;

    /**
     * 源文件的行数
     */
    private int metaDataRows;

}
