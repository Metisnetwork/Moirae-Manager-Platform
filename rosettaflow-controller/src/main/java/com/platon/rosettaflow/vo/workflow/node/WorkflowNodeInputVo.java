package com.platon.rosettaflow.vo.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 工作流节点输入响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点输入响应参数")
public class WorkflowNodeInputVo {

    @ApiModelProperty(value = "工作流节点输入表主键ID")
    private Long id;

    @ApiModelProperty(value = "工作流节点id")
    private Long workflowNodeId;

    @ApiModelProperty(value = "组织的身份标识Id")
    private String identityId;

    @ApiModelProperty(value = "组织名称")
    private String identityName;

    @ApiModelProperty(value = "资源所属组织中调度服务的 nodeId")
    private String nodeId;

    @ApiModelProperty(value = "元数据id")
    private String dataTableId;

    @ApiModelProperty(value = "元数据名称|数据名称 (表名)")
    private String dataTableName;

    @ApiModelProperty(value = "数据字段ID,多个以”,“分隔")
    private String dataColumnIds;

    @ApiModelProperty(value = "数据字段名称,多个以”,“分隔")
    private String dataColumnNames;

    @ApiModelProperty(value = "数据文件id")
    private String dataFileId;

    @ApiModelProperty(value = "任务里面定义的 (p0 -> pN 方 ...)")
    private String partyId;

    @ApiModelProperty(value = "状态: 0-无效，1- 有效")
    private Byte status;

    @ApiModelProperty(value = "工作流节点变量列表")
    private List<WorkflowNodeVariableVo> workflowNodeVariableVoList;

}
