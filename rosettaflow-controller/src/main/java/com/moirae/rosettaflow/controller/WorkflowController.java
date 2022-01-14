package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.req.workflow.*;
import com.moirae.rosettaflow.service.IWorkflowNodeService;
import com.moirae.rosettaflow.service.IWorkflowService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.workflow.GetStatusVo;
import com.moirae.rosettaflow.vo.workflow.TaskEventVo;
import com.moirae.rosettaflow.vo.workflow.WorkflowDetailsVo;
import com.moirae.rosettaflow.vo.workflow.WorkflowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作流管理
 *
 * @author admin
 * @date 2021/8/17
 */
@Slf4j
@RestController
@Api(tags = "工作流管理关接口")
@RequestMapping(value = "workflow", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowController {

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private IWorkflowNodeService workflowNodeService;

    @GetMapping("list")
    @ApiOperation(value = "查询工作流列表", notes = "查询工作流列表")
    public ResponseVo<PageVo<WorkflowVo>> list(@Validated ListWorkflowReq listReq) {
        IPage<WorkflowDto> page = workflowService.queryWorkFlowPageList(listReq.getProjectId(),
                listReq.getWorkflowName(), listReq.getCurrent(), listReq.getSize());
        List<WorkflowVo> items = BeanUtil.copyToList(page.getRecords(), WorkflowVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, items));
    }

    @GetMapping(value = "detail/{id}")
    @ApiOperation(value = "获取工作流详情", notes = "获取工作流详情")
    public ResponseVo<WorkflowDetailsVo> detail(@ApiParam(value = "工作流表主键ID", required = true) @PathVariable Long id) {
        Workflow workflow = workflowService.queryWorkflow(id);
        return ResponseVo.createSuccess(BeanUtil.toBean(workflow, WorkflowDetailsVo.class));
    }

    @PostMapping("add")
    @ApiOperation(value = "添加工作流", notes = "添加工作流")
    public ResponseVo<?> add(@RequestBody @Validated AddWorkflowReq addReq) {
        Workflow workflow = BeanUtil.toBean(addReq, Workflow.class);
        workflowService.addWorkflow(workflow);
        return ResponseVo.createSuccess();
    }

    @PostMapping("edit")
    @ApiOperation(value = "编辑工作流", notes = "编辑工作流")
    public ResponseVo<?> edit(@RequestBody @Validated EditWorkflowReq editReq) {
        workflowService.editWorkflow(editReq.getId(), editReq.getWorkflowName(), editReq.getWorkflowDesc());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delete/{id}")
    @ApiOperation(value = "删除工作流", notes = "删除工作流")
    public ResponseVo<?> delete(@ApiParam(value = "工作流表ID", required = true) @PathVariable Long id) {
        workflowService.deleteWorkflow(id);
        return ResponseVo.createSuccess();
    }

    @PostMapping("start")
    @ApiOperation(value = "启动工作流", notes = "启动工作流")
    public ResponseVo<?> start(@RequestBody @Validated StartWorkflowReq startWorkflowReq) {
        Workflow workflow = BeanUtil.toBean(startWorkflowReq, Workflow.class);
        workflowService.saveWorkflowDetailAndStart(workflow);
        return ResponseVo.createSuccess();
    }

    @PostMapping("getLog/{workflowId}")
    @ApiOperation(value = "获取运行日志", notes = "获取运行日志")
    public ResponseVo<List<TaskEventVo>> getLog(@ApiParam(value = "workflowId", required = true) @PathVariable Long workflowId) {
        List<TaskEventDto> taskEventShowDtoList = workflowService.getTaskEventList(workflowId);
        return ResponseVo.createSuccess(getLogConvertVo(taskEventShowDtoList));
    }

    /** 获取日志接口返回参数转换 */
    private  List<TaskEventVo> getLogConvertVo(List<TaskEventDto> taskEventShowDtoList){
        List<TaskEventVo> taskEventVoList = new ArrayList<>();
        TaskEventVo vo;
        if (taskEventShowDtoList.size() > 0) {
            for (TaskEventDto taskEventDto : taskEventShowDtoList) {
                vo = new TaskEventVo();
                vo.setType(taskEventDto.getType());
                vo.setTaskId(taskEventDto.getTaskId());
                vo.setName(taskEventDto.getOwner().getNodeName());
                vo.setNodeId(taskEventDto.getOwner().getNodeId());
                vo.setIdentityId(taskEventDto.getOwner().getIdentityId());
                vo.setPartyId(taskEventDto.getPartyId());
                vo.setContent(taskEventDto.getContent());
                vo.setCreateAt(DateUtil.date(taskEventDto.getCreateAt()));
                taskEventVoList.add(vo);
            }
        }
        return taskEventVoList;
    }

    @PostMapping("terminate")
    @ApiOperation(value = "终止工作流", notes = "终止工作流")
    public ResponseVo<?> terminate(@RequestBody @Validated TerminateWorkflowReq terminateWorkflowReq) {
        workflowService.terminate(terminateWorkflowReq.getWorkflowId());
        return ResponseVo.createSuccess();
    }

    @GetMapping("getWorkflowStatus")
    @ApiOperation(value = "获取工作流状态", notes = "获取工作流状态")
    public ResponseVo<GetStatusVo> getWorkflowStatus(@Validated GetStatusReq getStatusReq) {
        Workflow workflow = workflowService.getWorkflowStatusById(getStatusReq.getId());
        GetStatusVo getStatusVo = BeanUtil.toBean(workflow, GetStatusVo.class);
        return ResponseVo.createSuccess(getStatusVo);
    }
}
