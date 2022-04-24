package com.moirae.rosettaflow.vo.home;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "数据凭证使用明细")
public class DataTokenUsedVo {

    @ApiModelProperty(value = "元数据id")
    private String  metaDataId;;

    @ApiModelProperty(value = "数据凭证合约地址")
    private String tokenAddress;

    @ApiModelProperty(value = "数据凭证名称")
    private String tokenName;

    @ApiModelProperty(value = "数据凭证使用量")
    private Long tokenUsed;
}
