package com.moirae.rosettaflow.req.data;

import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 下载任务结果数据入参
 */
@Data
@ApiModel(value = "下载任务结果数据请求参数")
public class DownloadTaskReq {

    @ApiModelProperty(value = "任务结果数据目录filePath", required = true)
    private String filePath;

    @ApiModelProperty(value = "下载任务结果数据压缩格式：1:zip, 2: tar.gz", required = true)
    private int compress;

}
