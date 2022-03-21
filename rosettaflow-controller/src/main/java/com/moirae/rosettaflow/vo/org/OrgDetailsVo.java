package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织详情信息")
public class OrgDetailsVo extends OrgVo{

    @ApiModelProperty(value = "总文件数")
    private Integer totalFile;
}
