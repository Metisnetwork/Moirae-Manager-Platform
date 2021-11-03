package com.moirae.rosettaflow.service;


import com.moirae.rosettaflow.common.constants.SysConfig;
import com.platon.sm.SM3Utils;
import jodd.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

/**
 * 异步处理业务
 * @author houz
 */
@Slf4j
@Component
public class AsyncService {

    @Resource
    private SysConfig sysConfig;

    public static final String MOIRAE = "moirae";
    public static final String LOG = ".log";

    @Async("asyncExecutor")
    public void asyncReadLog(){
        log.info("开始异步读取日志");
        try {
            String logPath = "/home/juzix/hudenian/moirae-back/logs/";
            String dt = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String logName = logPath + MOIRAE + "-"+ dt + LOG;
            File file = new File(logName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    log.info("readLogFile--路径不存在, logName:{}", logName);
                }
            }
            // 复制日志文件
            FileUtil.copy(logPath + MOIRAE + LOG, file.getPath());
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
                // 换行
                fw.write("\r\n");
            }
        } catch (Exception e) {
            log.info("asyncReadLog--异步备注日志失败, 异常信息:{}", e.getMessage());
        }
    }

    /**
     * @Async注解表示异步，后面的参数对应于线程池配置类ExecutorConfig中的方法名asyncServiceExecutor()，
     * 如果不写后面的参数，直接使用@Async注解，则是使用默认的线程池
     * Future<String>为异步返回的结果。可以通过get()方法获取结果。
     *
     */
    @Async("asyncExecutor")
    public Future<String> getDataResult( ){
        log.info("开始异步处理");
        String result="asyncResultTest";
        return new AsyncResult<String>(result);
    }
}
