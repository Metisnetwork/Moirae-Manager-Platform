package com.datum.platform.vo.publicity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "提案详情")
public class ProposalDetailsVo extends ProposalVo {
    @ApiModelProperty(value = "提交时间")
    private Date submissionTime;
    @ApiModelProperty(value = "赞成票数量")
    private Integer voteAgreeNumber;
    @ApiModelProperty(value = "委员会成员数，即总票数")
    private Integer authorityNumber;
    @ApiModelProperty(value = "提案公示资料")
    private String publicityId;
    @ApiModelProperty(value = "提案公式信息")
    private PublicityVo publicity;
}
