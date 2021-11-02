package com.moirae.rosettaflow.utils;

import com.platon.sm.SM3Utils;
import jodd.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 追加日志文件
 * @author houz
 */
@Slf4j
public class WriterLogUtils {

    public static void readLogFile(String logPath) throws Exception {
        logPath = "/home/juzix/hudenian/moirae-back/logs/";
        String dt = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String logName = logPath + "moirae-" + dt + ".log";
        File file = new File(logName);
        if (!file.exists()) {
            if (file.createNewFile()) {
                log.info("readLogFile--路径不存在, logName:{}", logName);
            }
        }
        // 复制日志文件
        FileUtil.copy(logPath + "moirae.log", file.getPath());
        // 读取日志文件
        String logFile = FileUtil.readString(file.getPath());
        // 日志hash计算
        String sm3Log = SM3Utils.sm3(logFile);
        // 将hash值追加到日志文件
        try (FileWriter fw = new FileWriter(file, true)) {
            // 换行
            fw.write("\r\n");
            // 写入内容
            fw.write(sm3Log);
        }
    }
}