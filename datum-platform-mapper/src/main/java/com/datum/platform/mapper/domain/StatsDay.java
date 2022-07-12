package com.datum.platform.mapper.domain;

import com.datum.platform.mapper.enums.StatsDayKeyEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StatsDay implements Serializable {

    /**
     * 统计日期
     */
    private Date statsTime;

    /**
     * 统计key
     */
    private StatsDayKeyEnum statsKey;

    /**
     * 统计值
     */
    private Long statsValue;

    /**
     * 统计值（隐私）
     */
    Long privacyStatsValue;

    /**
     * 统计值（非隐私）
     */
    Long noPrivacyStatsValue;

    private static final long serialVersionUID = 1L;
}
