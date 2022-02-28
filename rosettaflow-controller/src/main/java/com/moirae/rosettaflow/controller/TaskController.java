package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.mapper.domain.Task;
import com.moirae.rosettaflow.mapper.domain.TaskEvent;
import com.moirae.rosettaflow.req.task.GetOrgTaskListByIdentityIdReq;
import com.moirae.rosettaflow.req.task.GetTaskDetailsReq;
import com.moirae.rosettaflow.req.task.GetTaskListByMetaDataIdReq;
import com.moirae.rosettaflow.service.TaskService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.task.OrgTaskVo;
import com.moirae.rosettaflow.vo.task.TaskDetailsVo;
import com.moirae.rosettaflow.vo.task.TaskEventVo;
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

    @GetMapping("getTaskEventList")
    @ApiOperation(value = "查询任务事件列表", notes = "查询任务事件列表")
    public ResponseVo<List<TaskEventVo>> getTaskEventList(@Valid GetTaskDetailsReq req) {
        List<TaskEvent> taskEventList = taskService.getTaskEventList(req.getTaskId());
        return ResponseVo.createSuccess(BeanUtil.copyToList(taskEventList, TaskEventVo.class));
    }

    @GetMapping("getOrgTaskListByIdentityId")
    @ApiOperation(value = "查询组织的任务列表-通过组织id", notes = "查询组织的任务列表-通过组织id")
    public ResponseVo<PageVo<OrgTaskVo>> getOrgTaskListByIdentityId(@Valid GetOrgTaskListByIdentityIdReq req) {
        IPage<Task> page = taskService.getOrgTaskListByIdentityId(req.getCurrent(), req.getSize(), req.getIdentityId());
        List<OrgTaskVo> orgTaskVoList = BeanUtil.copyToList(page.getRecords(), OrgTaskVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, orgTaskVoList));
    }

    @GetMapping("getTaskByMetaDataId")
    @ApiOperation(value = "查询任务列表-通过元数据", notes = "查询组织的任务列表-通过组织id")
    public ResponseVo<PageVo<TaskVo>> getTaskListByMetaDataId(@Valid GetTaskListByMetaDataIdReq req) {
        IPage<Task> page = taskService.getTaskListByMetaDataId(req.getCurrent(), req.getSize(), req.getMetaDataId());
        List<TaskVo> orgTaskVoList = BeanUtil.copyToList(page.getRecords(), TaskVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, orgTaskVoList));
    }

    @GetMapping("getTaskDetails")
    @ApiOperation(value = "查询任务详情", notes = "查询任务详情")
    public ResponseVo<TaskDetailsVo> getTaskDetails(@Valid GetTaskDetailsReq req) {
        Task task = taskService.getTaskDetails(req.getTaskId());
        return ResponseVo.createSuccess(BeanUtil.toBean(task, TaskDetailsVo.class));
    }
}
