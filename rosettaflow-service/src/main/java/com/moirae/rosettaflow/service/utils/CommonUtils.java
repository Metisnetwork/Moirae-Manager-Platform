package com.moirae.rosettaflow.service.utils;

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

}
