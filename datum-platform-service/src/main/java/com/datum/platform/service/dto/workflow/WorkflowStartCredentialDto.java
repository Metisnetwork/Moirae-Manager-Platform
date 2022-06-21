package com.datum.platform.service.dto.workflow;

import com.datum.platform.service.dto.data.CredentialDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@ApiModel
public class WorkflowStartCredentialDto {

    @ApiModelProperty(value = "元数据id")
    private String metaDataId;

    @ApiModelProperty(value = "数据凭证列表")
    List<CredentialDto> credentialList;
}
