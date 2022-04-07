package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "dc_meta_data_column")
public class MetaDataColumn implements Serializable {
    /**
     * 元数据ID,hash
     */
    private String metaDataId;

    /**
     * 字段索引序号
     */
    private Integer columnIdx;
    /**
     * 字段名称
     */
    private String columnName;

    /**
     * 字段类型
     */
    private String columnType;

    /**
     * 字段大小
     */
    private Integer columnSize;

    /**
     * 字段描述
     */
    private String remarks;

    private static final long serialVersionUID = 1L;
}
