package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "mo_stats_data")
public class StatsData implements Serializable {

    /**
     * 元数据ID,hash
     */
    @TableId
    private String metaDataId;

    /**
     * 数据凭证使用量
     */
    private Long dataTokenUsed;



    private static final long serialVersionUID = 1L;
}
