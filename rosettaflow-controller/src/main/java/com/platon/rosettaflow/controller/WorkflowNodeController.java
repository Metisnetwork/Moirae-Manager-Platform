package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.req.workflownode.AddWorkflowNodeReq;
import com.platon.rosettaflow.req.workflownode.WorkflowNodeRenameReq;
import com.platon.rosettaflow.service.IWorkflowNodeService;
import com.platon.rosettaflow.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/8/31
 * @description 工作流节点管理关接口
 */
@Slf4j
@RestController
@Api(tags = "工作流节点管理关接口")
@RequestMapping(value = "workflowNode", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowNodeController {

    @Resource
    private IWorkflowNodeService workflowNodeService;

    @PostMapping("add")
    @ApiOperation(value = "添加工作流节点", notes = "添加工作流节点")
    public ResponseVo<?> add(@RequestBody @Validated AddWorkflowNodeReq addNodeReq) {
        workflowNodeService.add(addNodeReq.getWorkflowId(), addNodeReq.getAlgorithmId(), addNodeReq.getNodeStep());
        return ResponseVo.createSuccess();
    }

    @PostMapping("rename")
    @ApiOperation(value = "工作流节点重命名", notes = "工作流节点重命名")
    public ResponseVo<?> rename(@RequestBody @Validated WorkflowNodeRenameReq workflowNodeRenameReq) {
        workflowNodeService.rename(workflowNodeRenameReq.getWorkflowId(), workflowNodeRenameReq.getNodeStep(), workflowNodeRenameReq.getNodeName());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delete/{id}")
    @ApiOperation(value = "删除工作流中的节点", notes = "删除工作流中的节点")
    public ResponseVo<?> delete(@ApiParam(value = "工作流表ID", required = true) @PathVariable Long id) {
        workflowNodeService.delete(id);
        return ResponseVo.createSuccess();
    }

}
