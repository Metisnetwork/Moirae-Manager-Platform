package com.moirae.rosettaflow.grpc.sys.resp.dto;

import lombok.Data;

/**
 * @author hudenian
 * @date 2021/10/13
 * @description 任务结果文件摘要
 */
@Data
public class GetTaskResultFileSummaryResponseDto {

    /**
     * 任务结果文件对应的任务Id
     */
    private String taskId;
    /**
     * 任务结果文件的名称
     */
    private String fileName;
    /**
     * 任务结果文件的元数据Id <系统默认生成的元数据>
     */
    private String metadataId;
    /**
     * 任务结果文件的原始文件Id
     */
    private String originId;
    /**
     * 任务结果文件的完整相对路径名
     */
    private String filePath;
    /**
     * 任务结果文件所在的 数据服务内网ip
     */
    private String ip;
    /**
     * 任务结果文件所在的 数据服务内网port
     */
    private String port;
}
