package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
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
    private Long tokenUsed;

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private String tokenAddress;
    @TableField(exist = false)
    private String tokenName;
}
