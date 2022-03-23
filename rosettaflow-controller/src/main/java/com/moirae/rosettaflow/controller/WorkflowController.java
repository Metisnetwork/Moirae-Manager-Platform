package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.req.workflow.*;
import com.moirae.rosettaflow.service.IWorkflowService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.workflow.*;
import com.moirae.rosettaflow.vo.workflow.node.NodeDetailsListVo;
import com.moirae.rosettaflow.vo.workflow.node.NodeTaskResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @PostMapping("wizard/settingOfWizardMode")
    @ApiOperation(value = "向导模式设置工作流", notes = "向导模式设置工作流")
    public ResponseVo<WorkflowKeyVo> settingOfWizardMode(@RequestBody @Validated SettingOfWizardModeReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("expert/createWorkflowOfExpertMode")
    @ApiOperation(value = "专家模式下创建工作流", notes = "专家模式下创建工作流")
    public ResponseVo<WorkflowKeyVo> createWorkflowOfExpertMode(@RequestBody @Validated CreateWorkflowOfExpertModeReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("expert/settingOfExpertMode")
    @ApiOperation(value = "专家模式设置工作流", notes = "专家模式设置工作流")
    public ResponseVo<Long> settingOfExpertMode(@RequestBody @Validated SettingOfExpertModeReq req) {
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



















    @GetMapping("getWorkflowPaymentList")
    @ApiOperation(value = "查询工作流支付列表", notes = "查询工作流支付列表")
    public ResponseVo<List<WorkflowPaymentVo>> getWorkflowPaymentList(@Validated GetWorkflowPaymentListReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("pay")
    @ApiOperation(value = "支付", notes = "支付")
    public ResponseVo<?> pay(@Validated PayReq req) {
        return ResponseVo.createSuccess();
    }

    @PostMapping("start")
    @ApiOperation(value = "启动工作流", notes = "启动工作流")
    public ResponseVo<?> start(@RequestBody @Validated StartWorkflowReq startWorkflowReq) {
        Workflow workflow = BeanUtil.toBean(startWorkflowReq, Workflow.class);
        workflowService.saveWorkflowDetailAndStart(workflow);
        return ResponseVo.createSuccess();
    }

    @PostMapping("terminate")
    @ApiOperation(value = "终止工作流", notes = "终止工作流")
    public ResponseVo<?> terminate(@RequestBody @Validated TerminateWorkflowReq terminateWorkflowReq) {
        workflowService.terminate(terminateWorkflowReq.getWorkflowId());
        return ResponseVo.createSuccess();
    }

    @GetMapping("getRunLog")
    @ApiOperation(value = "获取运行日志", notes = "获取运行日志")
    public ResponseVo<List<TaskEventVo>> getRunLog(@Validated TerminateWorkflowReq terminateWorkflowReq) {
        return ResponseVo.createSuccess();
    }

    @GetMapping("getWorkflowStatus")
    @ApiOperation(value = "获取工作流状态", notes = "获取工作流状态")
    public ResponseVo<GetStatusVo> getWorkflowStatus(@Validated GetStatusReq getStatusReq) {

        return ResponseVo.createSuccess();
    }

    @GetMapping(value = "getWorkflowNodeResult")
    @ApiOperation(value = "查看工作流节点结果文件", notes = "查看工作流节点结果文件")
    public ResponseVo<List<NodeTaskResultVo>> getWorkflowNodeResult(@Validated TerminateWorkflowReq req) {
        return ResponseVo.createSuccess(BeanUtil.copyToList(taskResultList, NodeTaskResultVo.class));
    }

    @GetMapping(value = {"queryNodeDetailsList/{id}", "queryNodeDetailsList/{id}/{runningRecordId}"})
    @ApiOperation(value = "查询工作流节点详情列表", notes = "查询工作流节点详情列表")
    public ResponseVo<NodeDetailsListVo> queryNodeDetailsList1(@ApiParam(value = "工作流表主键ID", required = true) @PathVariable Long id, @ApiParam(value = "运行记录id") @PathVariable(required = false) Long runningRecordId, HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        Workflow workflow = workflowService.queryWorkflowDetailAndStatus(id, runningRecordId, language);
        NodeDetailsListVo nodeDetailsListVo = BeanUtil.toBean(workflow, NodeDetailsListVo.class);
        return ResponseVo.createSuccess(nodeDetailsListVo);
    }


    @GetMapping(value = {"queryNodeDetailsList/{id}", "queryNodeDetailsList/{id}/{runningRecordId}"})
    @ApiOperation(value = "查询工作流节点详情列表", notes = "查询工作流节点详情列表")
    public ResponseVo<NodeDetailsListVo> queryNodeDetailsList2(@ApiParam(value = "工作流表主键ID", required = true) @PathVariable Long id, @ApiParam(value = "运行记录id") @PathVariable(required = false) Long runningRecordId, HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        Workflow workflow = workflowService.queryWorkflowDetailAndStatus(id, runningRecordId, language);
        NodeDetailsListVo nodeDetailsListVo = BeanUtil.toBean(workflow, NodeDetailsListVo.class);
        return ResponseVo.createSuccess(nodeDetailsListVo);
    }
}
