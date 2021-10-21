package com.platon.rosettaflow.common.utils;

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

   /* public static void main(String[] args) {
        String template = "{\"model_restore_party\":\"p0\",\"train_task_id\":\"<task_id>/p3\",\"predict_threshold\":0.5}";
        JSONObject jsonObject = JSON.parseObject(template);
        if (jsonObject.containsKey("model_restore_party")) {
            jsonObject.put("model_restore_party", "p101");
        }
        System.out.println(jsonObject.toJSONString());

        try {
            long start = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(
                    new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            String serial = sc.next();
            System.out.println(property + ": " + serial);
            System.out.println("time:" + (System.currentTimeMillis() - start));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
}
