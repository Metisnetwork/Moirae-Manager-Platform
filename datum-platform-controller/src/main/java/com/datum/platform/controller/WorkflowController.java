package com.datum.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.CalculationProcess;
import com.datum.platform.mapper.domain.Workflow;
import com.datum.platform.mapper.domain.WorkflowVersion;
import com.datum.platform.req.task.GetTaskDetailsReq;
import com.datum.platform.req.workflow.GetCalculationProcessListReq;
import com.datum.platform.req.workflow.GetWorkflowListReq;
import com.datum.platform.req.workflow.GetWorkflowVersionListReq;
import com.datum.platform.req.workflow.expert.CreateWorkflowOfExpertModeReq;
import com.datum.platform.req.workflow.wizard.CreateWorkflowOfWizardModeReq;
import com.datum.platform.service.WorkflowService;
import com.datum.platform.service.dto.task.TaskEventDto;
import com.datum.platform.service.dto.task.TaskResultDto;
import com.datum.platform.service.dto.workflow.*;
import com.datum.platform.service.dto.workflow.expert.WorkflowDetailsOfExpertModeDto;
import com.datum.platform.service.dto.workflow.expert.WorkflowNodeKeyDto;
import com.datum.platform.service.dto.workflow.expert.WorkflowStatusOfExpertModeDto;
import com.datum.platform.service.dto.workflow.wizard.CalculationProcessDto;
import com.datum.platform.service.dto.workflow.wizard.WorkflowDetailsOfWizardModeDto;
import com.datum.platform.service.dto.workflow.wizard.WorkflowWizardStepDto;
import com.datum.platform.utils.ConvertUtils;
import com.datum.platform.vo.PageVo;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.workflow.WorkflowStatsVo;
import com.datum.platform.vo.workflow.WorkflowVersionVo;
import com.datum.platform.vo.workflow.WorkflowVo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
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
@ApiSupport(order = 800)
@RequestMapping(value = "workflow", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowController {

    @Resource
    private WorkflowService workflowService;

    @GetMapping("getWorkflowStats")
    @ApiOperation(value = "查询工作流统计", notes = "查询工作流统计")
    @ApiOperationSupport(order = 1)
    public ResponseVo<WorkflowStatsVo> getWorkflowStats() {
        int workflowCount = workflowService.getWorkflowCount();
        WorkflowStatsVo workflowStatsVo = new WorkflowStatsVo();
        workflowStatsVo.setWorkflowCount(workflowCount);
        return ResponseVo.createSuccess(workflowStatsVo);
    }

    @GetMapping("getWorkflowList")
    @ApiOperation(value = "查询工作流列表", notes = "查询工作流列表")
    @ApiOperationSupport(order = 2)
    public ResponseVo<PageVo<WorkflowVo>> getWorkflowList(@Valid GetWorkflowListReq req) {
        IPage<Workflow> page = workflowService.getWorkflowList(req.getCurrent(), req.getSize(), req.getKeyword(), req.getAlgorithmId(), req.getBegin(), req.getEnd(), req.getCreateMode());
        List<WorkflowVo> itemList = BeanUtil.copyToList(page.getRecords(), WorkflowVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getWorkflowVersionList")
    @ApiOperation(value = "查询指定工作流的版本列表", notes = "查询指定工作流的版本列表")
    @ApiOperationSupport(order = 3)
    public ResponseVo<PageVo<WorkflowVersionVo>> getWorkflowVersionList(@Valid GetWorkflowVersionListReq req) {
        IPage<WorkflowVersion> page = workflowService.getWorkflowVersionList(req.getCurrent(), req.getSize(), req.getWorkflowId());
        List<WorkflowVersionVo> itemList = BeanUtil.copyToList(page.getRecords(), WorkflowVersionVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("wizard/getCalculationProcessList")
    @ApiOperation(value = "查询计算流程列表", notes = "查询计算流程列表")
    @ApiOperationSupport(order = 4)
    public ResponseVo<List<CalculationProcessDto>> getCalculationProcessList(@Valid GetCalculationProcessListReq req) {
        List<CalculationProcess> itemList = workflowService.getCalculationProcessList(req.getAlgorithmId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(itemList, CalculationProcessDto.class));
    }

    @PostMapping("wizard/createWorkflowOfWizardMode")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "向导模式下创建工作流", notes = "向导模式下创建工作流")
    public ResponseVo<WorkflowVersionKeyDto> createWorkflowOfWizardMode(@RequestBody @Validated CreateWorkflowOfWizardModeReq req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.createWorkflowOfWizardMode(req.getWorkflowName(), req.getWorkflowDesc(), req.getAlgorithmId(), req.getCalculationProcessId());
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @GetMapping("wizard/getWorkflowSettingOfWizardMode")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "向导模式下获取工作流设置", notes = "向导模式下获取工作流设置")
    public ResponseVo<WorkflowDetailsOfWizardModeDto> getWorkflowSettingOfWizardMode(@Validated WorkflowWizardStepDto req) {
        WorkflowDetailsOfWizardModeDto resp = workflowService.getWorkflowSettingOfWizardMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("wizard/settingWorkflowOfWizardMode")
    @ApiOperation(value = "向导模式设置工作流", notes = "向导模式设置工作流")
    @ApiOperationSupport(order = 7)
    public ResponseVo<WorkflowVersionKeyDto> settingWorkflowOfWizardMode(@RequestBody @Validated WorkflowDetailsOfWizardModeDto req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.settingWorkflowOfWizardMode(req);
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @PostMapping("expert/createWorkflowOfExpertMode")
    @ApiOperation(value = "专家模式下创建工作流", notes = "专家模式下创建工作流")
    @ApiOperationSupport(order = 8)
    public ResponseVo<WorkflowVersionKeyDto> createWorkflowOfExpertMode(@RequestBody @Validated CreateWorkflowOfExpertModeReq req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.createWorkflowOfExpertMode(req.getWorkflowName());
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @GetMapping("expert/getWorkflowSettingOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流设置", notes = "专家模式下获取工作流设置")
    @ApiOperationSupport(order = 9)
    public ResponseVo<WorkflowDetailsOfExpertModeDto> getWorkflowSettingOfExpertMode(@Validated WorkflowVersionKeyDto req) {
        WorkflowDetailsOfExpertModeDto resp = workflowService.getWorkflowSettingOfExpertMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("expert/settingWorkflowOfExpertMode")
    @ApiOperation(value = "专家模式设置工作流", notes = "专家模式设置工作流")
    @ApiOperationSupport(order = 10)
    public ResponseVo<WorkflowVersionKeyDto> settingWorkflowOfExpertMode(@RequestBody @Validated WorkflowDetailsOfExpertModeDto req) {
        WorkflowVersionKeyDto workflowKeyDto = workflowService.settingWorkflowOfExpertMode(req);
        return ResponseVo.createSuccess(workflowKeyDto);
    }

    @GetMapping("expert/getWorkflowStatusOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流状态", notes = "获取工作流状态")
    @ApiOperationSupport(order = 11)
    public ResponseVo<WorkflowStatusOfExpertModeDto> getWorkflowStatusOfExpertMode(@Validated WorkflowVersionKeyDto req) {
        WorkflowStatusOfExpertModeDto resp = workflowService.getWorkflowStatusOfExpertMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("expert/getWorkflowLogOfExpertMode")
    @ApiOperation(value = "专家模式下获取工作流日志", notes = "专家模式下获取工作流日志")
    @ApiOperationSupport(order = 12)
    public ResponseVo<List<TaskEventDto>> getWorkflowLogOfExpertMode(@Validated WorkflowVersionKeyDto req) {
        List<TaskEventDto> resp = workflowService.getWorkflowLogOfExpertMode(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping(value = "expert/getWorkflowResultOfExpertMode")
    @ApiOperation(value = "专家模式下查看工作流节点结果文件", notes = "专家模式下查看工作流节点结果文件")
    @ApiOperationSupport(order = 13)
    public ResponseVo<List<TaskResultDto>> getWorkflowNodeResult(@Validated WorkflowNodeKeyDto req) {
        List<TaskResultDto> resp = workflowService.getWorkflowNodeResult(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("copyWorkflow")
    @ApiOperation(value = "复制工作流", notes = "复制工作流，返回工作流id")
    @ApiOperationSupport(order = 14)
    public ResponseVo<WorkflowVersionKeyDto> copyWorkflow(@RequestBody @Validated WorkflowVersionNameDto req) {
        WorkflowVersionKeyDto resp = workflowService.copyWorkflow(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("deleteWorkflow")
    @ApiOperation(value = "删除工作流", notes = "删除工作流, 从未运行过的可以删除")
    @ApiOperationSupport(order = 15)
    public ResponseVo<Boolean> deleteWorkflow(@RequestBody @Validated WorkflowKeyDto req) {
        Boolean resp = workflowService.deleteWorkflow(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("preparationStartCredentialList")
    @ApiOperation(value = "启动工作流查询凭证列表", notes = "启动工作流查询凭证列表")
    @ApiOperationSupport(order = 16)
    public ResponseVo<List<WorkflowStartCredentialDto>> preparationStartCredentialList(@Validated WorkflowVersionKeyDto req) {
        List<WorkflowStartCredentialDto> resp = workflowService.preparationStartCredentialList(req);
        return ResponseVo.createSuccess(resp);
    }


    @GetMapping("preparationStart")
    @ApiOperation(value = "启动工作流查询预估费用", notes = "启动工作流查询预估费用")
    @ApiOperationSupport(order = 17)
    public ResponseVo<WorkflowFeeDto> preparationStart(@Validated WorkflowVersionKeyAndCredentialIdListDto req) {
        WorkflowFeeDto resp = workflowService.preparationStart(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("start")
    @ApiOperation(value = "启动工作流", notes = "启动工作流")
    @ApiOperationSupport(order = 18)
    public ResponseVo<WorkflowRunKeyDto> start(@RequestBody @Validated WorkflowStartSignatureDto req) {
        WorkflowRunKeyDto resp = workflowService.start(req);
        return ResponseVo.createSuccess(resp);
    }

    @PostMapping("terminate")
    @ApiOperation(value = "终止工作流", notes = "终止工作流")
    @ApiOperationSupport(order = 19)
    public ResponseVo<Boolean> terminate(@RequestBody @Validated WorkflowRunKeyDto req) {
        Boolean resp = workflowService.terminate(req);
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getWorkflowRunTaskList")
    @ApiOperation(value = "查询指定工作流的运行任务列表", notes = "查询指定工作流的运行任务列表")
    @ApiOperationSupport(order = 20)
    public ResponseVo<List<WorkflowRunTaskDto>> getWorkflowRunTaskList(@Valid WorkflowRunKeyDto req) {
        List<WorkflowRunTaskDto> itemList = workflowService.getWorkflowRunTaskList(req);
        return ResponseVo.createSuccess(itemList);
    }

    @GetMapping("getWorkflowRunTaskResult")
    @ApiOperation(value = "查询指定工作流的运行任务结果", notes = "查询指定工作流的运行任务结果")
    @ApiOperationSupport(order = 21)
    public ResponseVo<WorkflowRunTaskResultDto> getWorkflowRunTaskResult(@Valid GetTaskDetailsReq req) {
        WorkflowRunTaskResultDto result = workflowService.getWorkflowRunTaskResult(req.getTaskId());
        return ResponseVo.createSuccess(result);
    }
}
