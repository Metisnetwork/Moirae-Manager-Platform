package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "组织信息")
public class OrgVo extends BaseOrgVo{
}
