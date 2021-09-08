package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.req.job.AddJobReq;
import com.platon.rosettaflow.req.job.EditJobReq;
import com.platon.rosettaflow.req.job.QueryWorkflowReq;
import com.platon.rosettaflow.req.workflow.ListWorkflowReq;
import com.platon.rosettaflow.service.IJobService;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.job.QueryWorkflowVo;
import com.platon.rosettaflow.vo.workflow.WorkflowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/26
 * @description 作业管理
 */
@Slf4j
@RestController
@Api(tags = "作业管理关接口")
@RequestMapping(value = "job", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobController {

    @Resource
    private IJobService jobService;

    @PostMapping("add")
    @ApiOperation(value = "添加作业", notes = "添加作业")
    public ResponseVo<?> addJob(@RequestBody @Valid AddJobReq addJobReq) {
        JobDto jobDto = new JobDto();
        BeanCopierUtils.copy(addJobReq, jobDto);
        jobService.add(jobDto);
        return ResponseVo.createSuccess();
    }

    @PostMapping("edit")
    @ApiOperation(value = "作业编辑", notes = "作业编辑")
    public ResponseVo<?> edit(@RequestBody @Valid EditJobReq editJobReq) {
        JobDto jobDto = new JobDto();
        BeanCopierUtils.copy(editJobReq, jobDto);
        jobService.edit(jobDto);
        return ResponseVo.createSuccess();
    }

    @GetMapping("queryRelatedWorkflowName")
    @ApiOperation(value = "查询关联工作流名称", notes = "查询关联工作流名称")
    public ResponseVo<List<QueryWorkflowVo>> queryRelatedWorkflowName(@Valid QueryWorkflowReq queryWorkflowReq) {
        List<Workflow> workflowList = jobService.queryRelatedWorkflowName(queryWorkflowReq.getProjectId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(workflowList, QueryWorkflowVo.class));
    }

}
