package com.moirae.rosettaflow.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/12/15
 */
@Data
@ApiModel(value = "用户维护组织信息详情返回参数")
public class OrgUserChooseVo {

    @ApiModelProperty(value = "组织的身份名称")
    private String nodeName;

    @ApiModelProperty(value = "组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "组织的身份名称")
    public String getIdentityName(){
        return nodeName;
    }
}
