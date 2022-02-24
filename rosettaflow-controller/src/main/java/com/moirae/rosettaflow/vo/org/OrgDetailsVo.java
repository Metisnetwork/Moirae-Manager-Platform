package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织详情信息")
public class OrgDetailsVo extends BaseOrgVo{

    @ApiModelProperty(value = "计算服务的总共内存")
    private Long totalMemory;

    @ApiModelProperty(value = "计算服务的总带宽")
    private Long totalBandwidth;

    @ApiModelProperty(value = "计算服务的总核数")
    private Integer totalCore;

    @ApiModelProperty(value = "总文件数")
    private Integer totalFile;

    @ApiModelProperty(value = "参与任务数量")
    private Integer totalTask;
}
