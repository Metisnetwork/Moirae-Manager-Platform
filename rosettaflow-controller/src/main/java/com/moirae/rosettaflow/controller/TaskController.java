package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.TaskDto;
import com.moirae.rosettaflow.req.task.TaskByIdReq;
import com.moirae.rosettaflow.req.task.TaskByIdentityIdReq;
import com.moirae.rosettaflow.service.TaskService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.task.TaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "任务相关接口")
@RequestMapping(value = "task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    @Resource
    private TaskService taskService;

    @GetMapping("getTaskDetails")
    @ApiOperation(value = "查询任务详情", notes = "查询任务详情")
    public ResponseVo<TaskVo> getTaskDetails(@Valid TaskByIdReq taskByIdReq) {
        TaskDto page = taskService.getTaskDetails(taskByIdReq.getTaskId());
        return ResponseVo.createSuccess(BeanUtil.toBean(page, TaskVo.class));
    }

    @GetMapping("listTaskByIdentityId")
    @ApiOperation(value = "根据元数据ID,查询元数据的列定义", notes = "根据元数据ID,查询元数据的列定义")
    public ResponseVo<PageVo<TaskVo>> listTaskByIdentityId(@Valid TaskByIdentityIdReq taskByIdentityIdReq) {
        IPage<TaskDto> page = taskService.listTaskByIdentityId(taskByIdentityIdReq.getCurrent(), taskByIdentityIdReq.getSize(), taskByIdentityIdReq.getIdentityId());
        List<TaskVo> organizationVoList = BeanUtil.copyToList(page.getRecords(), TaskVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, organizationVoList));
    }

}
