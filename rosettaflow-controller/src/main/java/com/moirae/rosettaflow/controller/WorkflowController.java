package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.moirae.rosettaflow.common.enums.MetaDataStatusEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.common.enums.TaskRunningStatusEnum;
import com.moirae.rosettaflow.common.enums.WorkflowRunStatusEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.req.data.MetaDataReq;
import com.moirae.rosettaflow.req.workflow.*;
import com.moirae.rosettaflow.req.workflow.node.WorkflowNodeInputReq;
import com.moirae.rosettaflow.req.workflow.node.WorkflowNodeOutputReq;
import com.moirae.rosettaflow.service.IMetaDataService;
import com.moirae.rosettaflow.service.IOrganizationService;
import com.moirae.rosettaflow.service.IWorkflowService;
import com.moirae.rosettaflow.utils.ConvertUtils;
import com.moirae.rosettaflow.vo.PageVo;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.data.MetaDataVo;
import com.moirae.rosettaflow.vo.workflow.*;
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

import static com.moirae.rosettaflow.common.enums.RespCodeEnum.BIZ_EXCEPTION;

/**
 * 工作流管理
 *
 * @author admin
 * @date 2021/8/17
 */
@Slf4j
@RestController
@Api(tags = "工作流管理关接口")
@RequestMapping(value = "workflow", produces = MediaType.APPLICATION_JSON_VALUE)
public class WorkflowController {

    @Resource
    private IWorkflowService workflowService;
    @Resource
    private IMetaDataService metaDataService;
    @Resource
    private IOrganizationService organizationService;

    @GetMapping("list")
    @ApiOperation(value = "查询工作流列表", notes = "查询工作流列表")
    public ResponseVo<PageVo<WorkflowVo>> list(@Validated ListWorkflowReq listReq) {
        IPage<WorkflowDto> page = workflowService.queryWorkFlowPageList(listReq.getProjectId(), listReq.getWorkflowName(), listReq.getCurrent(), listReq.getSize());
        List<WorkflowVo> items = BeanUtil.copyToList(page.getRecords(), WorkflowVo.class);
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, items));
    }

    @GetMapping(value = "detail/{id}")
    @ApiOperation(value = "获取工作流详情", notes = "获取工作流详情")
    public ResponseVo<WorkflowDetailsVo> detail(@ApiParam(value = "工作流表主键ID", required = true) @PathVariable Long id) {
        Workflow workflow = workflowService.queryWorkflow(id);
        return ResponseVo.createSuccess(BeanUtil.toBean(workflow, WorkflowDetailsVo.class));
    }

    @PostMapping("add")
    @ApiOperation(value = "添加工作流", notes = "添加工作流")
    public ResponseVo<?> add(@RequestBody @Validated AddWorkflowReq addReq) {
        Workflow workflow = BeanUtil.toBean(addReq, Workflow.class);
        workflowService.addWorkflow(workflow);
        return ResponseVo.createSuccess();
    }

    @PostMapping("edit")
    @ApiOperation(value = "编辑工作流", notes = "编辑工作流")
    public ResponseVo<?> edit(@RequestBody @Validated EditWorkflowReq editReq) {
        workflowService.editWorkflow(editReq.getId(), editReq.getWorkflowName(), editReq.getWorkflowDesc());
        return ResponseVo.createSuccess();
    }

    @PostMapping("delete/{id}")
    @ApiOperation(value = "删除工作流", notes = "删除工作流")
    public ResponseVo<?> delete(@ApiParam(value = "工作流表ID", required = true) @PathVariable Long id) {
        workflowService.deleteWorkflow(id);
        return ResponseVo.createSuccess();
    }

    @PostMapping("start")
    @ApiOperation(value = "启动工作流", notes = "启动工作流")
    public ResponseVo<?> start(@RequestBody @Validated StartWorkflowReq startWorkflowReq) {
        //校验工作流相关的数据和组织是否被撤销
        checkWorkflowReq(startWorkflowReq);

        Workflow workflow = BeanUtil.toBean(startWorkflowReq, Workflow.class);
        workflowService.saveWorkflowDetailAndStart(workflow);
        return ResponseVo.createSuccess();
    }

    /**
     * 校验工作流相关的数据和组织是否被撤销
     * @param startWorkflowReq
     */
    private void checkWorkflowReq(StartWorkflowReq startWorkflowReq) {
        startWorkflowReq.getWorkflowNodeReqList().forEach(workflowNodeReq -> {
            List<WorkflowNodeInputReq> workflowNodeInputReqList = workflowNodeReq.getWorkflowNodeInputReqList();
            workflowNodeInputReqList.forEach(workflowNodeInputReq -> {
                String metaDataId = workflowNodeInputReq.getDataTableId();
                String identityId = workflowNodeInputReq.getIdentityId();

                //校验元数据
                LambdaQueryWrapper<MetaData> metaDataLambdaQueryWrapper = Wrappers.lambdaQuery();
                metaDataLambdaQueryWrapper.eq(MetaData::getMetaDataId, metaDataId);
                metaDataLambdaQueryWrapper.eq(MetaData::getStatus, StatusEnum.VALID.getValue());
                MetaData one = metaDataService.getOne(metaDataLambdaQueryWrapper);
                if(one == null){
                    //无效元数据
                    throw new BusinessException(BIZ_EXCEPTION,StrUtil.format("无效元数据，元素据Id：{}",metaDataId));
                } else if(one.getDataStatus() == 3) {
                    //元数据已被撤销
                    throw new BusinessException(BIZ_EXCEPTION,StrUtil.format("元数据已被撤销，元素据Id：{}",metaDataId));
                }

                //校验组织
                Organization organization = organizationService.getByIdentityId(identityId);
                if(organization == null){
                    //无效组织
                    throw new BusinessException(BIZ_EXCEPTION,StrUtil.format("组织状态异常，组织Id：{}",identityId));
                }
            });


            List<WorkflowNodeOutputReq> workflowNodeOutputReqList = workflowNodeReq.getWorkflowNodeOutputReqList();
            workflowNodeOutputReqList.forEach(workflowNodeOutputReq -> {
                String identityId = workflowNodeOutputReq.getIdentityId();
                //校验组织
                Organization organization = organizationService.getByIdentityId(identityId);
                if(organization == null){
                    //无效组织
                    throw new BusinessException(BIZ_EXCEPTION, StrUtil.format("组织状态异常，组织Id：{}",identityId));
                }
            });
        });
    }

    @PostMapping(value = {"getLog/{workflowId}", "getLog/{workflowId}/{runningRecordId}"})
    @ApiOperation(value = "获取运行日志", notes = "获取运行日志")
    public ResponseVo<List<TaskEventVo>> getLog(@ApiParam(value = "workflowId", required = true) @PathVariable Long workflowId, @ApiParam(value = "运行记录id") @PathVariable(required = false) Long runningRecordId) {
        List<TaskEventDto> taskEventShowDtoList = workflowService.getTaskEventList(workflowId, runningRecordId);
        return ResponseVo.createSuccess(BeanUtil.copyToList(taskEventShowDtoList, TaskEventVo.class));
    }

    @PostMapping("terminate")
    @ApiOperation(value = "终止工作流", notes = "终止工作流")
    public ResponseVo<?> terminate(@RequestBody @Validated TerminateWorkflowReq terminateWorkflowReq) {
        workflowService.terminate(terminateWorkflowReq.getWorkflowId());
        return ResponseVo.createSuccess();
    }

    @GetMapping("getWorkflowStatus")
    @ApiOperation(value = "获取工作流状态", notes = "获取工作流状态")
    public ResponseVo<GetStatusVo> getWorkflowStatus(@Validated GetStatusReq getStatusReq) {
        Workflow workflow = workflowService.getWorkflowStatusById(getStatusReq.getId(), getStatusReq.getRunningRecordId());
        GetStatusVo getStatusVo = BeanUtil.toBean(workflow, GetStatusVo.class);
        return ResponseVo.createSuccess(getStatusVo);
    }


    @GetMapping("runningRecordList")
    @ApiOperation(value = "获取工作流运行记录", notes = "获取工作流运行记录")
    public ResponseVo<PageVo<RunningRecordVo>> runningRecordList(@Valid RunningRecordReq runningRecordReq) {
        IPage<WorkflowRunStatus> page = workflowService.runningRecordList(runningRecordReq.getCurrent(), runningRecordReq.getSize(), runningRecordReq.getProjectId(), runningRecordReq.getWorkflowName());
        List<RunningRecordVo> items = BeanUtil.copyToList(page.getRecords(), RunningRecordVo.class);
        items.forEach(item -> {
            if (item.getRunStatus() == WorkflowRunStatusEnum.RUNNING.getValue()){
                item.setEndTime(null);
            }
        });
        return ResponseVo.createSuccess(ConvertUtils.convertPageVo(page, items));
    }
}
