package com.moirae.rosettaflow.mapper.domain;

import lombok.Data;

@Data
public class StatisticsDataTrend {
    /**
     * 统计时间
     */
    String statsTime;
    /**
     * 累计值
     */
    Long totalValue;
    /**
     * 新增值
     */
    Long incrementValue;
}
