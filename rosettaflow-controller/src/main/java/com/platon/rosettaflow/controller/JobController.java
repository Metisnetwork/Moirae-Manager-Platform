package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platon.rosettaflow.common.enums.JobActionStatusEnum;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.req.job.ActionJobReq;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.req.job.AddJobReq;
import com.platon.rosettaflow.req.job.EditJobReq;
import com.platon.rosettaflow.req.job.ListJobReq;
import com.platon.rosettaflow.req.job.QueryWorkflowReq;
import com.platon.rosettaflow.service.IJobService;
import com.platon.rosettaflow.vo.PageVo;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.job.JobVo;
import com.platon.rosettaflow.vo.job.QueryWorkflowVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
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


    @PostMapping("list")
    @ApiOperation(value = "作业分页列表", notes = "作业分页列表")
    public ResponseVo<PageVo<JobVo>> listJob(@RequestBody @Valid ListJobReq listJobReq) {
        IPage<JobDto> jobDtoIpage = jobService.list(listJobReq.getCurrent(), listJobReq.getSize(), listJobReq.getJobName());
        return convertToJobVo(jobDtoIpage);
    }


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
    @PostMapping("action")
    @ApiOperation(value = "操作作业", notes = "操作作业")
    public ResponseVo<?> actionJob(@RequestBody @Valid ActionJobReq actionJobReq) {
        if(actionJobReq.getActionType() == JobActionStatusEnum.PAUSE.getValue()){
            jobService.pause(actionJobReq.getId());
        } else {
            jobService.reStart(actionJobReq.getId());
        }
        return ResponseVo.createSuccess();
    }






    private ResponseVo<PageVo<JobVo>> convertToJobVo(IPage<JobDto> pageDto) {
        List<JobVo> items = new ArrayList<>();
        pageDto.getRecords().forEach(dto -> {
            JobVo vo = new JobVo();
            BeanCopierUtils.copy(dto, vo);
            items.add(vo);
        });
        PageVo<JobVo> pageVo = new PageVo<>();
        pageVo.setCurrent(pageDto.getCurrent());
        pageVo.setItems(items);
        pageVo.setSize(pageDto.getSize());
        pageVo.setTotal(pageDto.getTotal());
        return ResponseVo.createSuccess(pageVo);
    }



}
