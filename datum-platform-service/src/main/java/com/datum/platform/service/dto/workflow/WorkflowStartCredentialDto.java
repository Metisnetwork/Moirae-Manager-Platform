package com.datum.platform.service.dto.workflow;

import com.datum.platform.service.dto.data.HaveAttributesCredentialDto;
import com.datum.platform.service.dto.data.NoAttributesCredentialDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class WorkflowStartCredentialDto {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "元数据名称")
    private String metaDataName;

    @ApiModelProperty(value = "无属性凭证")
    NoAttributesCredentialDto noAttributesCredential;

    @ApiModelProperty(value = "有属性凭证")
    List<HaveAttributesCredentialDto> haveAttributesCredentialList;
}
