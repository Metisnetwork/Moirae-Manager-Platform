package com.platon.rosettaflow.vo.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hudenian
 * @date 2021/8/30
 * @description 功能描述
 */
@Data
@ApiModel
public class WorkflowNodeOutputVo {

    @ApiModelProperty(value = "工作流节点输出表主键ID(自增长)")
    private Long id;

    @ApiModelProperty(value = "工作流节点表主键id")
    private Long workflowNodeId;

    @ApiModelProperty(value = "协同方组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "协同方组织名称")
    private String identityName;

    @ApiModelProperty(value = "是否发起方: 0-否,1-是")
    private Byte savePartnerFlag;

    @ApiModelProperty(value = "任务里面定义的 (p0 -> pN 方 ...)")
    private String partyId;

    @ApiModelProperty(value = "组织中调度服务的 nodeId")
    private String nodeId;

    @ApiModelProperty(value = "存储形式: 1-明文，2:密文")
    private Byte storePattern;

    @ApiModelProperty(value = "存储路径")
    private String storePath;

    @ApiModelProperty(value = "输出内容")
    private String outputContent;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;
}
