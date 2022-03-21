package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.Task;
import com.moirae.rosettaflow.req.task.GetTaskDetailsReq;
import com.moirae.rosettaflow.req.task.GetTaskListReq;
import com.moirae.rosettaflow.service.TaskService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.task.TaskDetailsVo;
import com.moirae.rosettaflow.vo.task.TaskStatsVo;
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
import java.util.ArrayList;
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
        return ResponseVo.createSuccess(new TaskStatsVo());
    }

    @GetMapping("getTaskList")
    @ApiOperation(value = "查询任务列表", notes = "查询任务列表")
    public ResponseVo<PageVo<TaskVo>> getTaskList(@Valid GetTaskListReq req) {
        List<TaskVo> orgTaskVoList = new ArrayList<>();
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(null, orgTaskVoList));
    }

    @GetMapping("getTaskDetails")
    @ApiOperation(value = "查询任务详情", notes = "查询任务详情")
    public ResponseVo<TaskDetailsVo> getTaskDetails(@Valid GetTaskDetailsReq req) {
        Task task = taskService.getTaskDetails(req.getTaskId());
        return ResponseVo.createSuccess(BeanUtil.toBean(task, TaskDetailsVo.class));
    }


}
