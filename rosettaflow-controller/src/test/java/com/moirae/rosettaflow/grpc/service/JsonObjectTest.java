package com.moirae.rosettaflow.grpc.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 功能描述
 */
public class JsonObjectTest {

    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("key1", "value1"));
        studentList.add(new Student("key2", "value2"));
        studentList.add(new Student("key3", "value3"));

        JSONObject jsonObject = JSONUtil.createObj();
        for (Student s : studentList) {
            jsonObject.set(s.key, s.value);
        }
        System.out.println(jsonObject.toString());

    }

    static class Student {
        String key;
        String value;

        public Student(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
