package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织及数据量信息")
public class OrgAndDataVo extends BaseOrgVo{

    @ApiModelProperty(value = "总文件数")
    private Integer totalFile;

    @ApiModelProperty(value = "总文件大小(字节)")
    private Long totalData;
}
