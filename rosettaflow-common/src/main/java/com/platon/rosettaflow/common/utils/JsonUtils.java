package com.platon.rosettaflow.common.utils;

import com.alibaba.fastjson.JSON;

/**
 * @author hudenian
 * @date 2021/10/18
 */
public class JsonUtils {

    public static boolean isJson(String str) {
        try {
            JSON.parse(str);
        } catch (Exception e) {
            System.out.println("str is not json format" + e.getMessage());
            return false;
        }
        return true;
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
