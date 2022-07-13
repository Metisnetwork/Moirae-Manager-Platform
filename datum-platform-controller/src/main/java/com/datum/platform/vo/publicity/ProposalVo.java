package com.datum.platform.vo.publicity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "提案基本信息")
public class ProposalVo {
    @ApiModelProperty(value = "提案id")
    private String id;
    @ApiModelProperty(value = "提交的组织id")
    private String submitterIdentityId;
    @ApiModelProperty(value = "提交的组织名称")
    private String submitterNodeName;
    @ApiModelProperty(value = "候选的组织id")
    private String candidateIdentityId;
    @ApiModelProperty(value = "候选的组织名称")
    private String candidateNodeName;
    @ApiModelProperty(value = "提案的类型 1-增加委员会成员; 2-剔除委员会成员")
    private Integer type;
    @ApiModelProperty(value = "投票开始时间")
    private Date voteBeginTime;
    @ApiModelProperty(value = "投票结束时间")
    private Date voteEndTime;
    @ApiModelProperty(value = "投票状态 0-未开始; 1-投票中; 2-投票通过; 3-投票未通过; 6-已撤销")
    private Integer status;
}
