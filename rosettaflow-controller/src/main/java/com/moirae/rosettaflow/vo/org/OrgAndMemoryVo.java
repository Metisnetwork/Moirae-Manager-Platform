package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织及算力资源信息")
public class OrgAndMemoryVo extends BaseOrgVo{

    @ApiModelProperty(value = "计算服务的总共内存")
    private Long totalMemory;

    @ApiModelProperty(value = "计算服务的总带宽")
    private Long totalBandwidth;

    @ApiModelProperty(value = "计算服务的总核数")
    private Integer totalCore;
}
