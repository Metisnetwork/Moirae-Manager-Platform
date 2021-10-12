package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.dto.WorkflowNodeDto;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeInput;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeOutput;
import com.platon.rosettaflow.req.workflow.node.ClearWorkflowNodeReq;
import com.platon.rosettaflow.req.workflow.node.WorkflowAllNodeReq;
import com.platon.rosettaflow.service.IWorkflowNodeService;
import com.platon.rosettaflow.utils.ConvertUtils;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.workflow.node.*;
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
    private IWorkflowNodeService workflowNodeService;

    @GetMapping(value = "queryNodeDetailsList/{id}")
    @ApiOperation(value = "查询工作流节点详情列表", notes = "查询工作流节点详情列表")
    public ResponseVo<NodeDetailsListVo> queryNodeDetailsList(@ApiParam(value = "工作流表主键ID", required = true) @PathVariable Long id) {
        List<WorkflowNodeDto> workflowNodeDtoList = workflowNodeService.queryNodeDetailsList(id);
        return ResponseVo.createSuccess(convertToWorkflowVo(workflowNodeDtoList));
    }

    /**
     * 转换响应参数
     */
    private NodeDetailsListVo convertToWorkflowVo(List<WorkflowNodeDto> workflowNodeDtoList) {
        NodeDetailsListVo nodeDetailsListVo = new NodeDetailsListVo();
        List<WorkflowNodeVo> workflowNodeVoList = new ArrayList<>();
        if (workflowNodeDtoList != null && workflowNodeDtoList.size() > 0) {
            for (WorkflowNodeDto nodeDto : workflowNodeDtoList) {
                WorkflowNodeVo workflowNodeVo = BeanUtil.toBean(nodeDto, WorkflowNodeVo.class);
                // 输入参数转换
                List<WorkflowNodeInput> nodeInputList = nodeDto.getWorkflowNodeInputList();
                workflowNodeVo.setWorkflowNodeInputVoList(BeanUtil.copyToList(nodeInputList, WorkflowNodeInputVo.class));
                // 输出参数转换
                List<WorkflowNodeOutput> nodeOutputList = nodeDto.getWorkflowNodeOutputList();
                workflowNodeVo.setWorkflowNodeOutputVoList(BeanUtil.copyToList(nodeOutputList, WorkflowNodeOutputVo.class));
                // 节点算法转换
                AlgorithmDto algorithmDto = nodeDto.getAlgorithmDto();
                workflowNodeVo.setNodeAlgorithmVo(BeanUtil.toBean(algorithmDto, NodeAlgorithmVo.class));
                workflowNodeVoList.add(workflowNodeVo);
            }
        }
        nodeDetailsListVo.setWorkflowNodeVoList(workflowNodeVoList);
        return nodeDetailsListVo;
    }

    @PostMapping("save")
    @ApiOperation(value = "保存工作流所有节点数据", notes = "保存工作流所有节点数据")
    public ResponseVo<?> save(@RequestBody @Validated WorkflowAllNodeReq workflowAllNodeReq) {
        List<WorkflowNodeDto> workflowNodeDtoList = ConvertUtils.convertSaveReq(
                workflowAllNodeReq.getWorkflowNodeReqList(), Boolean.TRUE);
        workflowNodeService.saveWorkflowAllNodeData(workflowAllNodeReq.getWorkflowId(), workflowNodeDtoList);
        return ResponseVo.createSuccess();
    }

    @PostMapping("clear")
    @ApiOperation(value = "清空工作流节点", notes = "清空工作流节点")
    public ResponseVo<?> clear(@RequestBody @Validated ClearWorkflowNodeReq clearNodeReq) {
        workflowNodeService.clearWorkflowNode(clearNodeReq.getWorkflowId());
        return ResponseVo.createSuccess();
    }

    @GetMapping(value = "getTaskResult/{taskId}")
    @ApiOperation(value = "查看运行结果", notes = "查看运行结果")
    public ResponseVo<WorkflowNodeResultVo> detail(@ApiParam(value = "任务id", required = true) @PathVariable String taskId) {
        WorkflowNodeResultVo workflowNodeResultVo = new WorkflowNodeResultVo();
        workflowNodeResultVo.setResult(taskId + "运行结果待开发");
        return ResponseVo.createSuccess(workflowNodeResultVo);
    }

}
