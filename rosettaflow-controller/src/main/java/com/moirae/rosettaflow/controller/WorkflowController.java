package com.moirae.rosettaflow.controller;

import com.moirae.rosettaflow.req.workflow.*;
import com.moirae.rosettaflow.req.workflow.expert.CreateWorkflowOfExpertModeReq;
import com.moirae.rosettaflow.req.workflow.expert.GetWorkflowResultOfExpertModeReq;
import com.moirae.rosettaflow.req.workflow.expert.SettingWorkflowOfExpertModeReq;
import com.moirae.rosettaflow.req.workflow.wizard.CreateWorkflowOfWizardModeReq;
import com.moirae.rosettaflow.req.workflow.wizard.SettingWorkflowOfWizardModeReq;
import com.moirae.rosettaflow.service.IWorkflowService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.task.TaskResultVo;
import com.moirae.rosettaflow.vo.workflow.*;
import com.moirae.rosettaflow.vo.workflow.expert.WorkflowSettingOfExpertModeVo;
import com.moirae.rosettaflow.vo.workflow.wizard.WorkflowSettingOfWizardModeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作流管理
 */
@Slf4j
@RestController
@Api(tags = "工作流管理关接口")
@RequestMapping(value = "workflow", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowController {

    @Resource
    private IWorkflowService workflowService;

    @GetMapping("getWorkflowStats")
    @ApiOperation(value = "查询工作流统计", notes = "查询工作流统计")
    public ResponseVo<WorkflowStatsVo> getWorkflowStats() {
        return ResponseVo.createSuccess(new WorkflowStatsVo());
    }

    @GetMapping("getWorkflowList")
    @ApiOperation(value = "查询工作流列表", notes = "查询工作流列表")
    public ResponseVo<PageVo<WorkflowVo>> getDataList(@Valid GetWorkflowListReq req) {
        return ResponseVo.createSuccess();
    }

    @GetMapping("getWorkflowVersionList")
    @ApiOperation(value = "查询指定工作流的版本列表", notes = "查询指定工作流的版本列表")
    public ResponseVo<PageVo<WorkflowVersionVo>> getWorkflowVersionList(@Valid GetWorkflowVersionListReq req) {
        List<WorkflowVersionVo> orgTaskVoList = new ArrayList<>();
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(null, orgTaskVoList));
    }

    @GetMapping("wizard/getCalculationProcessList")
    @ApiOperation(value = "查询计算流程列表", notes = "查询计算流程列表")
    public ResponseVo<List<CalculationProcessVo>> getCalculationProcessList(@Valid GetCalculationProcessListReq req) {
        List<CalculationProcessVo> orgTaskVoList = new ArrayList<>();
        return ResponseVo.createSuccess(orgTaskVoList);
    }

    @PostMapping("wizard/createWorkflowOfWizardMode")
    @ApiOperation(value = "向导模式下创建工作流", notes = "向导模式下创建工作流")
    public ResponseVo<WorkflowKeyVo> createWorkflowOfWizardMode(@RequestBody @Validated CreateWorkflowOfWizardModeReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("wizard/settingWorkflowOfWizardMode")
    @ApiOperation(value = "向导模式设置工作流", notes = "向导模式设置工作流")
    public ResponseVo<WorkflowKeyVo> settingWorkflowOfWizardMode(@RequestBody @Validated SettingWorkflowOfWizardModeReq req) {
        return ResponseVo.createSuccess();
    }

    @GetMapping("wizard/getWorkflowSettingOfWizardMode")
    @ApiOperation(value = "专家模式下获取工作流设置", notes = "专家模式下获取工作流设置")
    public ResponseVo<WorkflowSettingOfWizardModeVo> getWorkflowSettingOfWizardMode(@Validated WorkflowKeyVo req) {
        return ResponseVo.createSuccess(null);
    }

    @PostMapping("expert/createWorkflowOfExpertMode")
    @ApiOperation(value = "专家模式下创建工作流", notes = "专家模式下创建工作流")
    public ResponseVo<WorkflowKeyVo> createWorkflowOfExpertMode(@RequestBody @Validated CreateWorkflowOfExpertModeReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("expert/settingWorkflowOfExpertMode")
    @ApiOperation(value = "专家模式设置工作流", notes = "专家模式设置工作流")
    public ResponseVo<WorkflowKeyVo> settingWorkflowOfExpertMode(@RequestBody @Validated SettingWorkflowOfExpertModeReq req) {
        return ResponseVo.createSuccess();
    }

    @GetMapping("expert/getWorkflowSettingOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流设置", notes = "专家模式下获取工作流设置")
    public ResponseVo<WorkflowSettingOfExpertModeVo> getWorkflowSettingOfExpertMode(@Validated WorkflowKeyVo req) {
        return ResponseVo.createSuccess(null);
    }

    @GetMapping("expert/getWorkflowStatusOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流状态", notes = "获取工作流状态")
    public ResponseVo<GetStatusVo> getWorkflowStatusOfExpertMode(@Validated WorkflowVersionIdReq req) {

        return ResponseVo.createSuccess();
    }

    @GetMapping("expert/getWorkflowLogOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流日志", notes = "专家模式下获取工作流日志")
    public ResponseVo<List<TaskEventVo>> getRunLog(@Validated WorkflowVersionIdReq req) {
        return ResponseVo.createSuccess();
    }

    @GetMapping(value = "getWorkflowResultOfExpertMode")
    @ApiOperation(value = "专家模式下查看工作流节点结果文件", notes = "专家模式下查看工作流节点结果文件")
    public ResponseVo<List<TaskResultVo>> getWorkflowNodeResult(@Validated GetWorkflowResultOfExpertModeReq req) {


        return ResponseVo.createSuccess();
    }

    @PostMapping("copyWorkflow")
    @ApiOperation(value = "复制工作流", notes = "复制工作流，返回工作流id")
    public ResponseVo<WorkflowKeyVo> copyWorkflow(@RequestBody @Validated WorkflowVersionIdReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("deleteWorkflow")
    @ApiOperation(value = "删除工作流", notes = "删除工作流")
    public ResponseVo<?> deleteWorkflow(@RequestBody @Validated WorkflowIdReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("clearWorkflow")
    @ApiOperation(value = "清空工作流", notes = "清空工作流")
    public ResponseVo<?> clear(@RequestBody @Validated WorkflowVersionIdReq req) {
        return ResponseVo.createSuccess();
    }

    @GetMapping("getWorkflowPayList")
    @ApiOperation(value = "查询工作流支付列表", notes = "查询工作流支付列表")
    public ResponseVo<List<WorkflowPayVo>> getWorkflowPaymentList(@Validated WorkflowVersionIdReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("pay")
    @ApiOperation(value = "支付", notes = "支付")
    public ResponseVo<TxHashVo> pay(@Validated PayReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("start")
    @ApiOperation(value = "启动工作流", notes = "启动工作流")
    public ResponseVo<?> start(@RequestBody @Validated StartWorkflowReq startWorkflowReq) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("terminate")
    @ApiOperation(value = "终止工作流", notes = "终止工作流")
    public ResponseVo<?> terminate(@RequestBody @Validated WorkflowVersionIdReq req) {
        return ResponseVo.createSuccess();
    }
}
