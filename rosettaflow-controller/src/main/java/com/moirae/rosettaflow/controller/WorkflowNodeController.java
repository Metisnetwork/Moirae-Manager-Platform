package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.mapper.domain.WorkflowRunTaskStatus;
import com.moirae.rosettaflow.req.workflow.node.ClearWorkflowNodeReq;
import com.moirae.rosettaflow.req.workflow.node.WorkflowAllNodeReq;
import com.moirae.rosettaflow.service.IWorkflowNodeService;
import com.moirae.rosettaflow.service.IWorkflowRunStatusService;
import com.moirae.rosettaflow.service.IWorkflowService;
import com.moirae.rosettaflow.vo.ResponseVo;
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
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 工作流节点管理关接口
 *
 * @author hudenian
 * @date 2021/8/31
 */
@Slf4j
@RestController
@Api(tags = "工作流节点管理关接口")
@RequestMapping(value = "workflowNode", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowNodeController {

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private IWorkflowRunStatusService workflowRunStatusService;

    @GetMapping(value = "queryNodeDetailsList/{id}")
    @ApiOperation(value = "查询工作流节点详情列表", notes = "查询工作流节点详情列表")
    public ResponseVo<NodeDetailsListVo> queryNodeDetailsList(@ApiParam(value = "工作流表主键ID", required = true) @PathVariable Long id, HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        Workflow workflow = workflowService.queryWorkflowDetailAndStatus(id, language);
        NodeDetailsListVo nodeDetailsListVo = BeanUtil.toBean(workflow, NodeDetailsListVo.class);
        return ResponseVo.createSuccess(nodeDetailsListVo);
    }

    @PostMapping("save")
    @ApiOperation(value = "保存工作流所有节点数据", notes = "保存工作流所有节点数据")
    public ResponseVo<?> save(@RequestBody @Validated WorkflowAllNodeReq workflowAllNodeReq) {
        Workflow workflow = BeanUtil.toBean(workflowAllNodeReq, Workflow.class);
        workflowService.saveWorkflowDetail(workflow);
        return ResponseVo.createSuccess();
    }

    @PostMapping("clear")
    @ApiOperation(value = "清空工作流节点", notes = "清空工作流节点")
    public ResponseVo<?> clear(@RequestBody @Validated ClearWorkflowNodeReq clearNodeReq) {
        workflowService.clearWorkflowNode(clearNodeReq.getWorkflowId());
        return ResponseVo.createSuccess();
    }

    @GetMapping(value = "getTaskResult/{taskId}")
    @ApiOperation(value = "查看运行结果", notes = "查看运行结果")
    public ResponseVo<List<NodeTaskResultVo>> queryTaskResultByTaskId(@ApiParam(value = "任务id", required = true) @PathVariable String taskId) {
        List<WorkflowRunTaskStatus> taskResultList = workflowRunStatusService.queryWorkflowRunTaskStatusByTaskId(taskId);
        return ResponseVo.createSuccess(BeanUtil.copyToList(taskResultList, NodeTaskResultVo.class));
    }

}
