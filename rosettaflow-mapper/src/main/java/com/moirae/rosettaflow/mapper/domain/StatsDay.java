package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.StatsDayKeyEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "mo_stats_day")
public class StatsDay implements Serializable {

    /**
     * 统计日期
     */
    @TableId
    private Date statsTime;

    /**
     * 统计key
     */
    private StatsDayKeyEnum statsKey;

    /**
     * 统计值
     */
    private Long statsValue;

    private static final long serialVersionUID = 1L;
}
