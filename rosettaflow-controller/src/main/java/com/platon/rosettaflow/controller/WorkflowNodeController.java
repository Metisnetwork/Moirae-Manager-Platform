package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.dto.WorkflowNodeDto;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.req.workflow.node.*;
import com.platon.rosettaflow.service.IWorkflowNodeService;
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

    /** 转换响应参数 */
    private NodeDetailsListVo convertToWorkflowVo( List<WorkflowNodeDto> workflowNodeDtoList) {
        NodeDetailsListVo nodeDetailsListVo = new NodeDetailsListVo();
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
    @ApiOperation(value = "保存工作流节点", notes = "保存工作流节点")
    public ResponseVo<?> save(@RequestBody @Validated SaveWorkflowNodeReq saveNodeReq) {
        List<WorkflowNode> workflowNodeList = BeanUtil.copyToList(saveNodeReq.getWorkflowNodeReqList(), WorkflowNode.class);
        workflowNodeService.saveWorkflowNode(saveNodeReq.getWorkflowId(), workflowNodeList);
        return ResponseVo.createSuccess();
    }

    @PostMapping("clear")
    @ApiOperation(value = "清空工作流节点", notes = "清空工作流节点")
    public ResponseVo<?> clear(@RequestBody @Validated ClearWorkflowNodeReq clearNodeReq) {
        workflowNodeService.clearWorkflowNode(clearNodeReq.getWorkflowId());
        return ResponseVo.createSuccess();
    }

    @PostMapping("add")
    @ApiOperation(value = "添加工作流节点", notes = "添加工作流节点")
    public ResponseVo<AddWorkflowNodeVo> add(@RequestBody @Validated AddWorkflowNodeReq addNodeReq) {
        WorkflowNode workflowNode = BeanUtil.toBean(addNodeReq, WorkflowNode.class);
        Long workflowNodeId = workflowNodeService.addWorkflowNode(workflowNode);
        AddWorkflowNodeVo addWorkflowNodeVo = new AddWorkflowNodeVo();
        addWorkflowNodeVo.setWorkflowNodeId(workflowNodeId);
        return ResponseVo.createSuccess(addWorkflowNodeVo);
    }

    @PostMapping("rename")
    @ApiOperation(value = "工作流节点重命名", notes = "工作流节点重命名")
    public ResponseVo<?> rename(@RequestBody @Validated WorkflowNodeRenameReq renameReq) {
        workflowNodeService.renameWorkflowNode(renameReq.getWorkflowNodeId(), renameReq.getNodeName());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delete/{id}")
    @ApiOperation(value = "删除工作流中的节点", notes = "删除工作流中的节点")
    public ResponseVo<?> delete(@ApiParam(value = "工作流表ID", required = true) @PathVariable Long id) {
        workflowNodeService.deleteWorkflowNode(id);
        return ResponseVo.createSuccess();
    }

    @PostMapping("addNodeInput")
    @ApiOperation(value = "保存工作流节点输入", notes = "保存工作流节点输入")
    public ResponseVo<?> addNodeInput(@RequestBody @Validated SaveNodeInputListReq saveInputReq) {
        List<WorkflowNodeInput> workflowNodeInputList =
                BeanUtil.copyToList(saveInputReq.getSaveNodeInputReqList(), WorkflowNodeInput.class);
        workflowNodeService.saveWorkflowNodeInput(saveInputReq.getWorkflowNodeId(), workflowNodeInputList);
        return ResponseVo.createSuccess();
    }

    @PostMapping("addNodeOutput")
    @ApiOperation(value = "添加工作流节点输出", notes = "添加工作流节点输出")
    public ResponseVo<?> addNodeOutput(@RequestBody @Validated SaveNodeOutputListReq outputListReq) {
        List<WorkflowNodeOutput> workflowNodeOutputList =
                BeanUtil.copyToList(outputListReq.getSaveNodeOutputReqList(), WorkflowNodeOutput.class);
        workflowNodeService.saveWorkflowNodeOutput(outputListReq.getWorkflowNodeId(), workflowNodeOutputList);
        return ResponseVo.createSuccess();
    }

    @PostMapping("saveNodeCode")
    @ApiOperation(value = "保存工作流节点代码", notes = "保存工作流节点代码")
    public ResponseVo<?> addNodeCode(@RequestBody @Validated SaveNodeCodeReq saveCodeReq) {
        WorkflowNodeCode workflowNodeCode = BeanUtil.toBean(saveCodeReq, WorkflowNodeCode.class);
        workflowNodeService.saveWorkflowNodeCode(workflowNodeCode);
        return ResponseVo.createSuccess();
    }

    @PostMapping("saveNodeResource")
    @ApiOperation(value = "保存工作流节点依赖资源环境", notes = "保存工作流节点依赖资源环境")
    public ResponseVo<?> addNodeResource(@RequestBody @Validated SaveNodeResourceReq saveResourceReq) {
        WorkflowNodeResource workflowNodeResource = BeanUtil.toBean(saveResourceReq, WorkflowNodeResource.class);
        workflowNodeService.saveWorkflowNodeResource(workflowNodeResource);
        return ResponseVo.createSuccess();
    }

}
