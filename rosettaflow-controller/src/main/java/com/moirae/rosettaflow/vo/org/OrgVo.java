package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织信息")
public class OrgVo extends BaseOrgVo{

    @ApiModelProperty(value = "组织机构图像url")
    private String imageUrl;

    @ApiModelProperty(value = "数据凭证使用量")
    private long dataTokenUsed;

    @ApiModelProperty(value = "参与任务数量")
    private Integer totalTask;

    @ApiModelProperty(value = "总文件数")
    private Integer totalFile;

    @ApiModelProperty(value = "计算服务的总共内存")
    private Long totalMemory;

    @ApiModelProperty(value = "计算服务的总带宽")
    private Long totalBandwidth;

    @ApiModelProperty(value = "计算服务的总核数")
    private Integer totalCore;
}
