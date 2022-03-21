package com.moirae.rosettaflow.vo.home;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(value = "组织算力明细")
public class OrgPowerVo {

    @ApiModelProperty(value = "排行序号")
    private Integer ranking;

    @ApiModelProperty(value = "组织的身份标识id")
    private String identityId;

    @ApiModelProperty(value = "组织的身份名称")
    private String nodeName;

    @ApiModelProperty(value = "组织的算力占比,万分比. 如 25.67% = 2567")
    public Integer getProportion(){
        return (int)((orgTotalCore/totalCore + orgTotalMemory/totalMemory + orgTotalBandwidth/totalBandwidth) / 3 * 10000);
    }

    @JsonIgnore
    private int orgTotalCore;
    @JsonIgnore
    private long orgTotalMemory;
    @JsonIgnore
    private long orgTotalBandwidth;
    @JsonIgnore
    private int totalCore;
    @JsonIgnore
    private long totalMemory;
    @JsonIgnore
    private long totalBandwidth;
}
