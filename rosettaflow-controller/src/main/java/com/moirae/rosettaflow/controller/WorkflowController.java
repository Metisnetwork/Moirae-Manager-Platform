package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.CalculationProcess;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowVersion;
import com.moirae.rosettaflow.req.workflow.*;
import com.moirae.rosettaflow.req.workflow.expert.CreateWorkflowOfExpertModeReq;
import com.moirae.rosettaflow.req.workflow.wizard.CreateWorkflowOfWizardModeReq;
import com.moirae.rosettaflow.service.WorkflowService;
import com.moirae.rosettaflow.service.dto.task.TaskEventDto;
import com.moirae.rosettaflow.service.dto.workflow.*;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowNodeKeyDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowDetailsOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.expert.WorkflowStatusOfExpertModeDto;
import com.moirae.rosettaflow.service.dto.workflow.wizard.CalculationProcessDto;
import com.moirae.rosettaflow.service.dto.workflow.wizard.WorkflowDetailsOfWizardModeDto;
import com.moirae.rosettaflow.service.dto.workflow.wizard.WorkflowWizardStepDto;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.service.dto.task.TaskResultDto;
import com.moirae.rosettaflow.vo.workflow.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
    private WorkflowService workflowService;

    @GetMapping("getWorkflowStats")
    @ApiOperation(value = "查询工作流统计", notes = "查询工作流统计")
    public ResponseVo<WorkflowStatsVo> getWorkflowStats() {
        int workflowCount = workflowService.getWorkflowCount();
        WorkflowStatsVo workflowStatsVo = new WorkflowStatsVo();
        workflowStatsVo.setWorkflowCount(workflowCount);
        return ResponseVo.createSuccess(workflowStatsVo);
    }

    @GetMapping("getWorkflowList")
    @ApiOperation(value = "查询工作流列表", notes = "查询工作流列表")
    public ResponseVo<PageVo<WorkflowVo>> getWorkflowList(@Valid GetWorkflowListReq req) {
        IPage<Workflow> page = workflowService.getWorkflowList(req.getCurrent(), req.getSize(), req.getKeyword(), req.getAlgorithmId(), req.getBegin(), req.getEnd());
        List<WorkflowVo> itemList = BeanUtil.copyToList(page.getRecords(), WorkflowVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getWorkflowVersionList")
    @ApiOperation(value = "（开发中）查询指定工作流的版本列表", notes = "查询指定工作流的版本列表")
    public ResponseVo<PageVo<WorkflowVersionVo>> getWorkflowVersionList(@Valid GetWorkflowVersionListReq req) {
        IPage<WorkflowVersion> page = workflowService.getWorkflowVersionList(req.getCurrent(), req.getSize(), req.getWorkflowId());
        List<WorkflowVersionVo> itemList = BeanUtil.copyToList(page.getRecords(), WorkflowVersionVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("wizard/getCalculationProcessList")
    @ApiOperation(value = "查询计算流程列表", notes = "查询计算流程列表")
    public ResponseVo<List<CalculationProcessDto>> getCalculationProcessList(@Valid GetCalculationProcessListReq req) {
        List<CalculationProcess> itemList = workflowService.getCalculationProcessList(req.getAlgorithmId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(itemList, CalculationProcessDto.class));
    }

    @PostMapping("wizard/createWorkflowOfWizardMode")
    @ApiOperation(value = "向导模式下创建工作流", notes = "向导模式下创建工作流")
    public ResponseVo<WorkflowVersionKeyDto> createWorkflowOfWizardMode(@RequestBody @Validated CreateWorkflowOfWizardModeReq req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.createWorkflowOfWizardMode(req.getWorkflowName(), req.getWorkflowDesc(), req.getAlgorithmId(), req.getCalculationProcessId());
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @GetMapping("wizard/getWorkflowSettingOfWizardMode")
    @ApiOperation(value = "向导模式下获取工作流设置", notes = "向导模式下获取工作流设置")
    public ResponseVo<WorkflowDetailsOfWizardModeDto> getWorkflowSettingOfWizardMode(@Validated WorkflowWizardStepDto req) {
        WorkflowDetailsOfWizardModeDto resp = workflowService.getWorkflowSettingOfWizardMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("wizard/settingWorkflowOfWizardMode")
    @ApiOperation(value = "向导模式设置工作流", notes = "向导模式设置工作流")
    public ResponseVo<WorkflowVersionKeyDto> settingWorkflowOfWizardMode(@RequestBody @Validated WorkflowDetailsOfWizardModeDto req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.settingWorkflowOfWizardMode(req);
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @PostMapping("expert/createWorkflowOfExpertMode")
    @ApiOperation(value = "专家模式下创建工作流", notes = "专家模式下创建工作流")
    public ResponseVo<WorkflowVersionKeyDto> createWorkflowOfExpertMode(@RequestBody @Validated CreateWorkflowOfExpertModeReq req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.createWorkflowOfExpertMode(req.getWorkflowName());
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @GetMapping("expert/getWorkflowSettingOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流设置", notes = "专家模式下获取工作流设置")
    public ResponseVo<WorkflowDetailsOfExpertModeDto> getWorkflowSettingOfExpertMode(@Validated WorkflowVersionKeyDto req) {
        WorkflowDetailsOfExpertModeDto resp = workflowService.getWorkflowSettingOfExpertMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("expert/settingWorkflowOfExpertMode")
    @ApiOperation(value = "专家模式设置工作流", notes = "专家模式设置工作流")
    public ResponseVo<WorkflowVersionKeyDto> settingWorkflowOfExpertMode(@RequestBody @Validated WorkflowDetailsOfExpertModeDto req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.settingWorkflowOfExpertMode(req);
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @GetMapping("expert/getWorkflowStatusOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流状态", notes = "获取工作流状态")
    public ResponseVo<WorkflowStatusOfExpertModeDto> getWorkflowStatusOfExpertMode(@Validated WorkflowVersionKeyDto req) {
        WorkflowStatusOfExpertModeDto resp = workflowService.getWorkflowStatusOfExpertMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("expert/getWorkflowLogOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流日志", notes = "专家模式下获取工作流日志")
    public ResponseVo<List<TaskEventDto>> getWorkflowLogOfExpertMode(@Validated WorkflowVersionKeyDto req) {
        List<TaskEventDto> resp = workflowService.getWorkflowLogOfExpertMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping(value = "expert/getWorkflowResultOfExpertMode")
    @ApiOperation(value = "专家模式下查看工作流节点结果文件", notes = "专家模式下查看工作流节点结果文件")
    public ResponseVo<List<TaskResultDto>> getWorkflowNodeResult(@Validated WorkflowNodeKeyDto req) {
        List<TaskResultDto> resp = workflowService.getWorkflowNodeResult(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("copyWorkflow")
    @ApiOperation(value = "复制工作流", notes = "复制工作流，返回工作流id")
    public ResponseVo<WorkflowVersionKeyDto> copyWorkflow(@RequestBody @Validated WorkflowVersionNameDto req) {
        WorkflowVersionKeyDto resp = workflowService.copyWorkflow(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("deleteWorkflow")
    @ApiOperation(value = "删除工作流", notes = "删除工作流, 从未运行过的可以删除")
    public ResponseVo<Boolean> deleteWorkflow(@RequestBody @Validated WorkflowKeyDto req) {
        Boolean resp = workflowService.deleteWorkflow(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("estimateWorkflowFee")
    @ApiOperation(value = "（开发中）估算工作流手续费", notes = "估算工作流手续费")
    public ResponseVo<List<WorkflowFeeDto>> estimateWorkflowFee(@Validated WorkflowVersionKeyDto req) {
        List<WorkflowFeeDto> resp = workflowService.estimateWorkflowFee(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("start")
    @ApiOperation(value = "（开发中）启动工作流", notes = "启动工作流")
    public ResponseVo<WorkflowRunKeyDto> start(@RequestBody @Validated WorkflowStartSignatureDto req) {
        WorkflowRunKeyDto resp = workflowService.start(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("terminate")
    @ApiOperation(value = "（开发中）终止工作流", notes = "终止工作流")
    public ResponseVo<Boolean> terminate(@RequestBody @Validated WorkflowRunKeyDto req) {
        Boolean resp = workflowService.terminate(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getWorkflowRunTaskList")
    @ApiOperation(value = "（开发中）查询指定工作流的运行任务列表", notes = "查询指定工作流的运行任务列表")
    public ResponseVo<PageVo<WorkflowRunTaskDto>> getWorkflowRunTaskList(@Valid WorkflowRunKeyDto req) {
        IPage<WorkflowRunTaskDto> page = workflowService.getWorkflowRunTaskList(req);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, page.getRecords()));
    }
}
