package com.moirae.rosettaflow.common.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author hudenian
 * @date 2021/10/18
 */
public class JsonUtils {
    static final String QUOTATION_MARKS = "\"";

    public static boolean isJson(String str) {
        try {
            JSON.parse(str);
        } catch (Exception e) {
            System.out.println("str is not json format" + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 去除json字符串两边引号
     *
     * @param str 字符串
     * @return 去除两边引号的字符串
     */
    public static String delQuotationMarks(String str) {
        if (str.startsWith(QUOTATION_MARKS)) {
            str = str.substring(1);
        }
        if (str.endsWith(QUOTATION_MARKS)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
