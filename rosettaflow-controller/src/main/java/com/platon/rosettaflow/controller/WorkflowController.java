package com.platon.rosettaflow.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.req.workflow.*;
import com.platon.rosettaflow.service.IWorkflowService;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.workflow.WorkflowVo;
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

    @GetMapping("list")
    @ApiOperation(value = "工作流列表列表", notes = "工作流列表列表")
    public ResponseVo<PageVo<WorkflowVo>> list(@Valid ListWorkflowReq listWorkflowReq) {
        WorkflowDto workflowDto = new WorkflowDto();
        BeanCopierUtils.copy(listWorkflowReq, workflowDto);
        IPage<WorkflowDto> servicePage = workflowService.list(workflowDto, listWorkflowReq.getCurrent(), listWorkflowReq.getSize());
        return convertToWorkflowVo(servicePage);
    }

    private ResponseVo<PageVo<WorkflowVo>> convertToWorkflowVo(IPage<WorkflowDto> servicePage) {
        List<WorkflowVo> items = new ArrayList<>();
        servicePage.getRecords().forEach(dto -> {
            WorkflowVo vo = new WorkflowVo();
            BeanCopierUtils.copy(dto, vo);
            items.add(vo);
        });

        PageVo<WorkflowVo> pageVo = new PageVo<>();
        pageVo.setCurrent(servicePage.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(servicePage.getSize());
        pageVo.setTotal(servicePage.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }

    @PostMapping("add")
    @ApiOperation(value = "添加工作流", notes = "添加工作流")
    public ResponseVo<?> add(@RequestBody @Validated AddWorkflowReq addWorkflowReq) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setWorkflowName(addWorkflowReq.getWorkflowName());
        workflowDto.setWorkflowDesc(addWorkflowReq.getWorkflowDesc());
        workflowService.add(workflowDto);
        return ResponseVo.createSuccess();
    }

    @PostMapping("edit")
    @ApiOperation(value = "编辑工作流", notes = "编辑工作流")
    public ResponseVo<?> edit(@RequestBody @Validated EditWorkflowReq editWorkflowReq) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setId(editWorkflowReq.getId());
        workflowDto.setWorkflowName(editWorkflowReq.getWorkflowName());
        workflowDto.setWorkflowDesc(editWorkflowReq.getWorkflowDesc());
        workflowService.edit(workflowDto);
        return ResponseVo.createSuccess();
    }

    @PostMapping("copy")
    @ApiOperation(value = "复制工作流", notes = "复制工作流")
    public ResponseVo<?> copy(@RequestBody @Validated CopyWorkflowReq copyWorkflowReq) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setId(copyWorkflowReq.getOriginId());
        workflowDto.setWorkflowName(copyWorkflowReq.getWorkflowName());
        workflowDto.setWorkflowDesc(copyWorkflowReq.getWorkflowDesc());
        workflowService.copy(workflowDto);
        return ResponseVo.createSuccess();
    }

    @PostMapping("delete/{id}")
    @ApiOperation(value = "删除工作流", notes = "删除工作流")
    public ResponseVo<?> delete(@ApiParam(value = "工作流表ID", required = true) @PathVariable Long id) {
        workflowService.delete(id);
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
}
