package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.req.workflow.StartWorkflowReq;
import com.platon.rosettaflow.service.IWorkflowService;
import com.platon.rosettaflow.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author admin
 * @date 2021/8/17
 * @description 工作流管理
 */
@Slf4j
@RestController
@Api(tags = "工作流管理关接口")
@RequestMapping(value = "workflow", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowController {

    @Resource
    private IWorkflowService workflowService;

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
}
