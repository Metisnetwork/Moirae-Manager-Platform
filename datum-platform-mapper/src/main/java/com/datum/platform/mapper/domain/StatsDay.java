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

    private static final long serialVersionUID = 1L;
}
