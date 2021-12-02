package com.moirae.rosettaflow.req.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 下载任务结果数据入参
 */
@Data
@ApiModel(value = "下载任务结果数据请求参数")
public class DownloadTaskReq {

    @ApiModelProperty(value = "任务结果表id", required = true)
    @NotNull(message = "{taskresult.id.notNull}")
    @Positive(message = "{taskresult.id.positive}")
    private int id;

    @ApiModelProperty(value = "下载任务结果数据压缩格式：1:zip, 2: tar.gz", required = true)
    @NotNull(message = "{taskresult.compress.notNull}")
    @Positive(message = "{taskresult.compress.positive}")
    private int compress;
}
