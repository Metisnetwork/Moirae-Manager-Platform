package com.datum.platform.service.dto.task;

import com.datum.platform.service.dto.org.OrgNameDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工作流节点运行结果
 * @author hudenian
 * @date 2021/9/17
 * @description
 */
@Data
@ApiModel(value = "工作流节点运行结果")
public class TaskResultDto {

    @ApiModelProperty(value = "事件组织信息")
    private OrgNameDto org;

    @ApiModelProperty(value = "任务结果文件的元数据Id <系统默认生成的元数据>")
    private String metadataId;

    @ApiModelProperty(value = "任务结果文件的名称")
    private String fileName;

    @ApiModelProperty(value = "任务结果文件的原始文件Id")
    private String originId;

    @ApiModelProperty(value = "任务结果文件的完整相对路径名")
    private String filePath;

    @ApiModelProperty(value = "任务结果文件所在的 数据服务内网ip")
    private String ip;

    @ApiModelProperty(value = "任务结果文件所在的 数据服务内网port")
    private String port;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
