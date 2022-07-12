package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "mo_stats_meta_data")
public class StatsMetaData implements Serializable {

    /**
     * 数据凭证ID
     */
    @TableId
    private Long metaDataId;

    /**
     * 使用次数
     */
    private Long usageCount;

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private String metaDataName;
}
