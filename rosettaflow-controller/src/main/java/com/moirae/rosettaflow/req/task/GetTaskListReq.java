package com.moirae.rosettaflow.req.task;

import com.moirae.rosettaflow.common.enums.TaskStatusEnum;
import com.moirae.rosettaflow.req.CommonPageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "任务列表查询")
public class GetTaskListReq extends CommonPageReq {

    @ApiModelProperty(value = "搜索关键字(任务id)")
    private String keyword;

    @ApiModelProperty(value = "时间的开始")
    private Date begin;

    @ApiModelProperty(value = "时间的结束")
    private Date end;

    public void setBegin(String begin){
        if(StringUtils.isNotBlank(begin)){
            this.begin = new Date(Long.valueOf(begin));
        }
    }

    public void setEnd(String end){
        if(StringUtils.isNotBlank(end)){
            this.end = new Date(Long.valueOf(end));
        }
    }

    @ApiModelProperty(value = "任务状态")
    private TaskStatusEnum taskStatus;
}
