package com.platon.rosettaflow.controller;

import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.JobDto;
import com.platon.rosettaflow.req.job.AddJobReq;
import com.platon.rosettaflow.req.job.EditJobReq;
import com.platon.rosettaflow.service.IJobService;
import com.platon.rosettaflow.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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



}
