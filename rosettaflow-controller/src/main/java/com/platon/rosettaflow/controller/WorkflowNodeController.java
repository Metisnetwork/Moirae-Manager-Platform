package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.dto.WorkflowNodeDto;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;
import com.platon.rosettaflow.req.workflownode.*;
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

    @PostMapping("save")
    @ApiOperation(value = "保存工作流节点", notes = "保存工作流节点")
    public ResponseVo<?> save(@RequestBody @Validated SaveWorkflowNodeReq saveNodeReq) {
        List<WorkflowNode> workflowNodeList = BeanUtil.copyToList(saveNodeReq.getWorkflowNodeReqList(), WorkflowNode.class);
        workflowNodeService.saveWorkflowNode(saveNodeReq.getWorkflowId(), workflowNodeList);
        return ResponseVo.createSuccess();
    }

    @PostMapping("add")
    @ApiOperation(value = "添加工作流节点", notes = "添加工作流节点")
    public ResponseVo<?> add(@RequestBody @Validated AddWorkflowNodeReq addNodeReq) {
        WorkflowNode workflowNode = BeanUtil.toBean(addNodeReq, WorkflowNode.class);
        workflowNodeService.addWorkflowNode(workflowNode);
        return ResponseVo.createSuccess();
    }

    @PostMapping("rename")
    @ApiOperation(value = "工作流节点重命名", notes = "工作流节点重命名")
    public ResponseVo<?> rename(@RequestBody @Validated WorkflowNodeRenameReq renameReq) {
        workflowNodeService.renameWorkflowNode(renameReq.getNodeId(), renameReq.getNodeName());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delete/{id}")
    @ApiOperation(value = "删除工作流中的节点", notes = "删除工作流中的节点")
    public ResponseVo<?> delete(@ApiParam(value = "工作流表ID", required = true) @PathVariable Long id) {
        workflowNodeService.deleteWorkflowNode(id);
        return ResponseVo.createSuccess();
    }

    @PostMapping("addNodeCode")
    @ApiOperation(value = "添加工作流节点代码", notes = "添加工作流节点代码")
    public ResponseVo<?> addNodeCode(@RequestBody @Validated AddNodeCodeReq addCodeReq) {
        WorkflowNodeCode workflowNodeCode = BeanUtil.toBean(addCodeReq, WorkflowNodeCode.class);
        workflowNodeService.addWorkflowNodeCode(workflowNodeCode);
        return ResponseVo.createSuccess();
    }

    @PostMapping("addNodeResource")
    @ApiOperation(value = "添加工作流节点依赖资源", notes = "添加工作流节点依赖资源")
    public ResponseVo<?> addNodeResource(@RequestBody @Validated AddNodeResourceReq addResourceReq) {
        WorkflowNodeResource workflowNodeResource = BeanUtil.toBean(addResourceReq, WorkflowNodeResource.class);
        workflowNodeService.addWorkflowNodeResource(workflowNodeResource);
        return ResponseVo.createSuccess();
    }

}
