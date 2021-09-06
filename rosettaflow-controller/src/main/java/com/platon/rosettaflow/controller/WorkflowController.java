package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.dto.WorkflowNodeDto;
import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeInput;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;
import com.platon.rosettaflow.req.workflow.*;
import com.platon.rosettaflow.service.IWorkflowService;
import com.platon.rosettaflow.utils.ConvertUtils;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.workflow.*;
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
    private GrpcTaskService grpcTaskService;

    @GetMapping("list")
    @ApiOperation(value = "查询工作流列表", notes = "查询工作流列表")
    public ResponseVo<PageVo<WorkflowVo>> list(@Valid ListWorkflowReq listReq) {
        IPage<WorkflowDto> page = workflowService.queryWorkFlowList(listReq.getProjectId(),
                listReq.getWorkflowName(), listReq.getCurrent(), listReq.getSize());
        List<WorkflowVo> items = BeanUtil.copyToList(page.getRecords(), WorkflowVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, items));
    }

    @GetMapping(value = "detail/{id}")
    @ApiOperation(value = "获取工作流详情", notes = "获取工作流详情")
    public ResponseVo<WorkflowDetailVo> detail(@ApiParam(value = "工作流表主键ID", required = true) @PathVariable Long id) {
        WorkflowDto workflowDto = workflowService.queryWorkflowDetail(id);
        return ResponseVo.createSuccess(convertToWorkflowVo(workflowDto));
    }
    /** 转换响应参数 */
    private WorkflowDetailVo convertToWorkflowVo(WorkflowDto workflowDto) {
        WorkflowDetailVo workflowDetailVo = BeanUtil.toBean(workflowDto, WorkflowDetailVo.class);
        List<WorkflowNodeDto> workflowNodeDtoList = workflowDto.getWorkflowNodeDtoList();
        List<WorkflowNodeVo> workflowNodeVoList = new ArrayList<>();
        if (workflowNodeDtoList != null && workflowNodeDtoList.size() > 0) {
            for(WorkflowNodeDto nodeDto: workflowNodeDtoList){
                WorkflowNodeVo workflowNodeVo = BeanUtil.toBean(nodeDto, WorkflowNodeVo.class);
                // 输入参数转换
                List<WorkflowNodeInput> nodeInputList = nodeDto.getWorkflowNodeInputList();
                workflowNodeVo.setWorkflowNodeInputVoList(BeanUtil.copyToList(nodeInputList, WorkflowNodeInputVo.class));
                // 输出参数转换
                List<WorkflowNodeOutput> nodeOutputList = nodeDto.getWorkflowNodeOutputList();
                workflowNodeVo.setWorkflowNodeOutputVoList(BeanUtil.copyToList(nodeOutputList, WorkflowNodeOutputVo.class));
                // 节点资源转换
                WorkflowNodeResource nodeResource = nodeDto.getWorkflowNodeResource();
                workflowNodeVo.setWorkflowNodeResourceVo(BeanUtil.toBean(nodeResource, WorkflowNodeResourceVo.class));
                workflowNodeVoList.add(workflowNodeVo);
            }
        }
        workflowDetailVo.setWorkflowNodeVoList(workflowNodeVoList);
        return workflowDetailVo;
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

    @PostMapping("copy")
    @ApiOperation(value = "复制工作流", notes = "复制工作流")
    public ResponseVo<?> copy(@RequestBody @Validated CopyWorkflowReq copyReq) {
        workflowService.copyWorkflow(copyReq.getOriginId(), copyReq.getWorkflowName(), copyReq.getWorkflowDesc());
        return ResponseVo.createSuccess();
    }

    @PostMapping("start")
    @ApiOperation(value = "启动工作流", notes = "启动工作流")
    public ResponseVo<?> start(@RequestBody @Validated StartWorkflowReq startWorkflowReq) {
        WorkflowDto workflowDto = new WorkflowDto();

        workflowDto.setId(startWorkflowReq.getWorkflowId());
        workflowDto.setStartNode(startWorkflowReq.getStartNode());
        workflowDto.setEndNode(startWorkflowReq.getEndNode());
        workflowService.start(workflowDto);
        //TODO
        return ResponseVo.createSuccess();
    }

    @PostMapping("getLog/{taskId}")
    @ApiOperation(value = "获取运行日志", notes = "获取运行日志")
    public ResponseVo<List<TaskEventVo>> getLog(@ApiParam(value = "taskId", required = true) @PathVariable String taskId) {
        List<TaskEventDto> taskEventShowDtoList = grpcTaskService.getTaskEventList(taskId);
        List<TaskEventVo> taskEventVoList = new ArrayList<>();
        TaskEventVo vo;
        for (TaskEventDto taskEventDto : taskEventShowDtoList) {
            vo = new TaskEventVo();
            vo.setType(taskEventDto.getType());
            vo.setTaskId(taskEventDto.getTaskId());
            vo.setName(taskEventDto.getOwner().getNodeName());
            vo.setNodeId(taskEventDto.getOwner().getNodeId());
            vo.setIdentityId(taskEventDto.getOwner().getIdentityId());
            vo.setContent(taskEventDto.getContent());
            vo.setCreateAt(DateUtil.date(taskEventDto.getCreateAt()));
            taskEventVoList.add(vo);
        }
        return ResponseVo.createSuccess(taskEventVoList);
    }
}
