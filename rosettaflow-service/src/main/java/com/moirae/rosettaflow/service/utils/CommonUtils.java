package com.moirae.rosettaflow.service.utils;

import com.moirae.rosettaflow.common.constants.SysConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class CommonUtils {

    private static final String DATE_TIME_FMT = "yyyyMMddHHmmssSSS";
    private static final String TASK_NAME_PRE = "task_";

    /**
     * 生成随机uuid( replace("-", "") )
     *
     * @return 返回uuid
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static Long convert2UserOfCostMem(Long costMem){
        if (null == costMem || costMem == 0) {
            return 0L;
        }
        return new BigDecimal(costMem)
                .divide(BigDecimal.valueOf(SysConstant.INT_1024 * SysConstant.INT_1024),
                        SysConstant.INT_0, RoundingMode.UP).longValue();
    }

    public static Long convert2UserOfCostBandwidth(Long costBandwidth){
        if (null == costBandwidth || costBandwidth == 0) {
            return 0L;
        }
        return new BigDecimal(costBandwidth)
                .divide(BigDecimal.valueOf(SysConstant.INT_1024 * SysConstant.INT_1024),
                        SysConstant.INT_0, RoundingMode.UP).longValue();
    }

    public static Long convert2UserOfRunTime(Long runTime){
        if (null == runTime || runTime == 0) {
            return 0L;
        }
        return new BigDecimal(runTime)
                .divide(BigDecimal.valueOf(SysConstant.INT_60 * SysConstant.INT_1000),
                        SysConstant.INT_0, RoundingMode.HALF_UP).longValue();
    }

    public static Long convert2DbOfCostMem(Long costMem){
        if (null == costMem || costMem == 0) {
            return 0L;
        }
        return new BigDecimal(costMem)
                .multiply(BigDecimal.valueOf(SysConstant.INT_1024 * SysConstant.INT_1024))
                .setScale(SysConstant.INT_0, RoundingMode.UP)
                .longValue();
    }

    public static Long convert2DbOfCostBandwidth(Long costBandwidth){
        if (null == costBandwidth || costBandwidth == 0) {
            return 0L;
        }
        return new BigDecimal(costBandwidth)
                .multiply(BigDecimal.valueOf(SysConstant.INT_1024 * SysConstant.INT_1024))
                .setScale(SysConstant.INT_0, RoundingMode.UP)
                .longValue();
    }

    public static Long convert2DbOfRunTime(Long runTime){
        if (null == runTime || runTime == 0) {
            return 0L;
        }
        return new BigDecimal(runTime)
                .multiply(BigDecimal.valueOf(SysConstant.INT_60 * SysConstant.INT_1000))
                .setScale(SysConstant.INT_0, RoundingMode.HALF_UP)
                .longValue();
    }
}
