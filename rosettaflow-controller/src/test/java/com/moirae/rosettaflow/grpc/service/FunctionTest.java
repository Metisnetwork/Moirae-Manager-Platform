package com.moirae.rosettaflow.grpc.service;

/**
 * @author hudenian
 * @date 2021/8/16
 * @description 功能描述
 */
public class FunctionTest {
    public static void main(String[] args) {
        String text = "自定义函数测试";
        printString(text, System.out::print);
    }

    private static void printString(String text, MyFunction myFunction) {
        myFunction.print(text);
    }
}
