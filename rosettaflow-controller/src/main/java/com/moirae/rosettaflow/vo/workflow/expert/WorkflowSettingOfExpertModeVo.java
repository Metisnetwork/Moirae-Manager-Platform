package com.moirae.rosettaflow.vo.workflow.expert;

import com.moirae.rosettaflow.req.workflow.SettingWorkflowOfExpertModeReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 工作流节点详情列表响应参数
 * @author hudenian
 * @date 2021/8/30
 */
@Data
@ApiModel(value = "工作流节点详情列表响应参数")
public class WorkflowSettingOfExpertModeVo extends SettingWorkflowOfExpertModeReq {

}
