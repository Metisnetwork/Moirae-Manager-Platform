package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.Task;
import com.moirae.rosettaflow.req.data.DataIdPageReq;
import com.moirae.rosettaflow.req.org.OrgIdPageReq;
import com.moirae.rosettaflow.req.task.GetTaskDetailsReq;
import com.moirae.rosettaflow.req.task.GetTaskListReq;
import com.moirae.rosettaflow.service.TaskService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.task.TaskDetailsVo;
import com.moirae.rosettaflow.vo.task.TaskOrgVo;
import com.moirae.rosettaflow.vo.task.TaskStatsVo;
import com.moirae.rosettaflow.vo.task.TaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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

    @GetMapping("getTaskStats")
    @ApiOperation(value = "查询任务统计", notes = "查询任务统计")
    public ResponseVo<TaskStatsVo> getTaskStats() {
        int taskCount = taskService.getTaskStats();
        TaskStatsVo taskStatsVo = new TaskStatsVo();
        taskStatsVo.setTaskCount(taskCount);
        return ResponseVo.createSuccess(taskStatsVo);
    }

    @GetMapping("getTaskListByOrg")
    @ApiOperation(value = "查询任务列表通过组织id", notes = "查询任务列表通过组织id")
    public ResponseVo<PageVo<TaskOrgVo>> getTaskListByOrg(@Valid OrgIdPageReq req) {
        IPage<Task> page = taskService.getTaskListByOrg(req.getCurrent(), req.getSize(), req.getIdentityId());
        List<TaskOrgVo> itemList = BeanUtil.copyToList(page.getRecords(), TaskOrgVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getTaskListByData")
    @ApiOperation(value = "查询任务列表通过元数据", notes = "查询任务列表通过元数据")
    public ResponseVo<PageVo<TaskVo>> getTaskListByData(@Valid DataIdPageReq req) {
        IPage<Task> page = taskService.getTaskListByData(req.getCurrent(), req.getSize(), req.getMetaDataId());
        List<TaskVo> itemList = BeanUtil.copyToList(page.getRecords(), TaskVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getTaskList")
    @ApiOperation(value = "查询任务列表", notes = "查询任务列表")
    public ResponseVo<PageVo<TaskVo>> getTaskList(@Valid GetTaskListReq req) {
        IPage<Task> page = taskService.getTaskList(req.getCurrent(), req.getSize(), req.getKeyword(), req.getBegin(), req.getEnd(), req.getTaskStatus());
        List<TaskVo> itemList = BeanUtil.copyToList(page.getRecords(), TaskVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getTaskDetails")
    @ApiOperation(value = "（开发中）查询任务详情", notes = "查询任务详情")
    public ResponseVo<TaskDetailsVo> getTaskDetails(@Valid GetTaskDetailsReq req) {
        Task task = taskService.getTaskDetails(req.getTaskId());
        return ResponseVo.createSuccess(BeanUtil.toBean(task, TaskDetailsVo.class));
    }
}
