package com.moirae.rosettaflow.grpc.data.provider.req.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author juzix
 * @description 任务结果文件下载请求
 */
@Data
public class DownloadRequestDto {
    /**
     * 文件目录
     */
    private String filePath;

    /**
     * 任务结果目录压缩格式： "compress": "zip"  或者 "compress": "tar.gz"
     */
    private String compress;

    /**
     * file_root_dir(文件目录): data|result，data表示从数据目录下载，result表示从结果目录下载
     */
    private String fileRootDir;

    /**
     * 任务结果访问ip
     */
    private String ip;

    /**
     * 任务结果访问port
     */
    private String port;

}
