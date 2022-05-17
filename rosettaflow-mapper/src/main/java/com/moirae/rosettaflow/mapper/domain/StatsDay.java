package com.moirae.rosettaflow.mapper.domain;

import com.moirae.rosettaflow.mapper.enums.StatsDayKeyEnum;
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

    private static final long serialVersionUID = 1L;
}
