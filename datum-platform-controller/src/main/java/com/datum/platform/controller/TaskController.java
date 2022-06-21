package com.datum.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.Task;
import com.datum.platform.req.data.DataIdPageReq;
import com.datum.platform.req.org.OrgIdPageReq;
import com.datum.platform.req.task.GetTaskDetailsReq;
import com.datum.platform.req.task.GetTaskListReq;
import com.datum.platform.service.TaskService;
import com.datum.platform.utils.ConvertUtils;
import com.datum.platform.vo.PageVo;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.task.TaskDetailsVo;
import com.datum.platform.vo.task.TaskOrgVo;
import com.datum.platform.vo.task.TaskStatsVo;
import com.datum.platform.vo.task.TaskVo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
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
@ApiSupport(order = 600)
@RequestMapping(value = "task", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {
    @Resource
    private TaskService taskService;

    @GetMapping("getTaskStats")
    @ApiOperation(value = "查询任务统计", notes = "查询任务统计")
    @ApiOperationSupport(order = 1)
    public ResponseVo<TaskStatsVo> getTaskStats() {
        int taskCount = taskService.countOfTask();
        TaskStatsVo taskStatsVo = new TaskStatsVo();
        taskStatsVo.setTaskCount(taskCount);
        return ResponseVo.createSuccess(taskStatsVo);
    }

    @GetMapping("getTaskListByOrg")
    @ApiOperation(value = "查询任务列表通过组织id", notes = "查询任务列表通过组织id")
    @ApiOperationSupport(order = 2)
    public ResponseVo<PageVo<TaskOrgVo>> getTaskListByOrg(@Valid OrgIdPageReq req) {
        IPage<Task> page = taskService.getTaskListByOrg(req.getCurrent(), req.getSize(), req.getIdentityId());
        List<TaskOrgVo> itemList = BeanUtil.copyToList(page.getRecords(), TaskOrgVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getTaskListByData")
    @ApiOperation(value = "查询任务列表通过元数据", notes = "查询任务列表通过元数据")
    @ApiOperationSupport(order = 3)
    public ResponseVo<PageVo<TaskVo>> getTaskListByData(@Valid DataIdPageReq req) {
        IPage<Task> page = taskService.getTaskListByData(req.getCurrent(), req.getSize(), req.getMetaDataId());
        List<TaskVo> itemList = BeanUtil.copyToList(page.getRecords(), TaskVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getTaskList")
    @ApiOperation(value = "查询任务列表", notes = "查询任务列表")
    @ApiOperationSupport(order = 4)
    public ResponseVo<PageVo<TaskVo>> getTaskList(@Valid GetTaskListReq req) {
        IPage<Task> page = taskService.getTaskList(req.getCurrent(), req.getSize(), req.getKeyword(), req.getBegin(), req.getEnd(), req.getTaskStatus());
        List<TaskVo> itemList = BeanUtil.copyToList(page.getRecords(), TaskVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, itemList));
    }

    @GetMapping("getTaskDetails")
    @ApiOperation(value = "查询任务详情", notes = "查询任务详情")
    @ApiOperationSupport(order = 5)
    public ResponseVo<TaskDetailsVo> getTaskDetails(@Valid GetTaskDetailsReq req) {
        Task task = taskService.getTaskDetails(req.getTaskId());
        return ResponseVo.createSuccess(BeanUtil.toBean(task, TaskDetailsVo.class));
    }
}
