package com.moirae.rosettaflow.util;

import com.platon.sm.SM3Utils;
import jodd.io.FileUtil;

import java.io.*;


public class Md5UtilTest {

    public static void main(String[] args) throws IOException {
        FileUtil.copy("c:/abc.html","c:/def.html");
        String logFile = FileUtil.readString("c:/def.html");
        System.out.println(SM3Utils.sm3(logFile));
    }
}
