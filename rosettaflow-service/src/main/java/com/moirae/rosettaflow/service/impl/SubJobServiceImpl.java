package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.BeanCopierUtils;
import com.moirae.rosettaflow.dto.SubJobDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.TerminateTaskRequestDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TerminateTaskRespDto;
import com.moirae.rosettaflow.mapper.SubJobMapper;
import com.moirae.rosettaflow.mapper.domain.SubJob;
import com.moirae.rosettaflow.mapper.domain.SubJobNode;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.service.ISubJobNodeService;
import com.moirae.rosettaflow.service.ISubJobService;
import com.moirae.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static cn.hutool.core.date.DateTime.now;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 子作业管理服务实现类
 */
@Slf4j
@Service
public class SubJobServiceImpl extends ServiceImpl<SubJobMapper, SubJob> implements ISubJobService {

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private GrpcTaskService grpcTaskService;

    @Resource
    private ISubJobNodeService subJobNodeService;


    @Override
    public IPage<SubJobDto> sublist(Long current, Long size, String subJobId, Long jobId) {
        Page<SubJob> page = new Page<>(current, size);
        LambdaQueryWrapper<SubJob> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SubJob::getJobId, jobId);
        wrapper.eq(SubJob::getStatus, StatusEnum.VALID.getValue());
        if (StrUtil.isNotBlank(subJobId)) {
            wrapper.like(SubJob::getId, subJobId);
        }
        this.page(page, wrapper);
        return convertToPageDto(page);
    }

    @Override
    public List<SubJob> queryBatchSubJobListByJobId(List<Long> jobIds) {
        LambdaQueryWrapper<SubJob> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SubJob::getJobId, jobIds);
        return this.list(queryWrapper);
    }

    @Override
    public void pause(Long id) {
        SubJob subJob = this.getById(id);
        if (Objects.isNull(subJob)) {
            log.error(ErrorMsg.JOB_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        if (subJob.getSubJobStatus() != SubJobStatusEnum.RUNNING.getValue()) {
            log.error(ErrorMsg.SUB_JOB_NOT_RUNNING.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_RUNNING.getMsg());
        }
        //停止运行中子作业节点(按节点顺序触发停止)
        List<TerminateTaskRequestDto> terminateTaskRequestDtoList = assemblyTerminateTaskRequestDto(subJob);
        terminateTaskRequestDtoList.stream().sorted(Comparator.comparing(TerminateTaskRequestDto::getNodeStep)).forEach(terminateTaskRequestDto -> {
            byte nodeRunStatus = terminateTaskRequestDto.getNodeRunStatus();
            if (nodeRunStatus == SubJobNodeStatusEnum.RUNNING.getValue()) {
                if (!terminateTask(terminateTaskRequestDto)) {
                    log.error(ErrorMsg.SUB_JOB_NODE_UPDATE_FAIL.getMsg());
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NODE_UPDATE_FAIL.getMsg());
                }
            }
        });
        //停止子作业
        this.updateJobStatus(subJob.getId(), SubJobStatusEnum.UN_RUN.getValue());
    }


    @Override
    public void reStart(Long id) {
        SubJob subJob = this.getById(id);
        if (Objects.isNull(subJob)) {
            log.error(ErrorMsg.JOB_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        if (subJob.getSubJobStatus() == SubJobStatusEnum.RUNNING.getValue() || subJob.getSubJobStatus() == SubJobStatusEnum.RUN_SUCCESS.getValue()) {
            log.error(ErrorMsg.SUB_JOB_NOT_STOP.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_STOP.getMsg());
        }

        Workflow workFlow = workflowService.getById(subJob.getWorkflowId());
        if (null == workFlow) {
            log.error("workFlow can not find by workflow id:{}", subJob.getWorkflowId());
            return;
        }

        //重启子作业
        WorkflowDto startWorkflowDto = new WorkflowDto();
        BeanCopierUtils.copy(workFlow, startWorkflowDto);
        startWorkflowDto.setStartNode(1);
        startWorkflowDto.setEndNode(workFlow.getNodeNumber());
        startWorkflowDto.setJobFlg(true);
        startWorkflowDto.setSubJobId(subJob.getId());
        workflowService.start(startWorkflowDto);
    }

    @Override
    public void updateJobStatus(Long subJobId, Byte subJobStatus) {
        LambdaUpdateWrapper<SubJob> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SubJob::getSubJobStatus, subJobStatus);
        updateWrapper.set(SubJob::getUpdateTime, new Date(System.currentTimeMillis()));
        updateWrapper.eq(SubJob::getId, subJobId);
        this.update(updateWrapper);
    }

    @Override
    public void updateBatchRunStatus(List<SubJob> subJobList) {
        this.updateBatchById(subJobList);
    }


    @Override
    public void updateBatchStatus(Object[] ids, Byte status) {
        LambdaUpdateWrapper<SubJob> updateSubJobWrapper = Wrappers.lambdaUpdate();
        updateSubJobWrapper.set(SubJob::getStatus, status);
        updateSubJobWrapper.set(SubJob::getUpdateTime, now());
        updateSubJobWrapper.in(SubJob::getId, ids);
        this.update(updateSubJobWrapper);
    }

    @Override
    public void deleteSubJobById(Long id) {
        SubJob subJob = this.getById(id);
        if (Objects.isNull(subJob)) {
            log.error(ErrorMsg.SUB_JOB_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_EXIST.getMsg());
        }
        if (subJob.getSubJobStatus() == SubJobStatusEnum.RUNNING.getValue()) {
            log.error(ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
        }
        //子作业节点是否存在运行中节点
        List<SubJobNode> subJobNodeList = subJobNodeService.querySubJobNodeListBySubJobId(subJob.getId());
        boolean isRunningNode = subJobNodeList.stream().anyMatch(subJobNode -> subJobNode.getRunStatus() == SubJobStatusEnum.RUNNING.getValue());
        if (isRunningNode) {
            log.error(ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
        }
        //更新子作业状态
        subJob.setStatus(StatusEnum.UN_VALID.getValue());
        // 数据库默认自动更新updateTime
        this.updateById(subJob);
        //更新子作业节点状态
        Object[] subJobNodeArrayIds = subJobNodeList.stream().map(SubJobNode::getId).toArray();
        subJobNodeService.updateBatchStatus(subJobNodeArrayIds, StatusEnum.UN_VALID.getValue());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteBatchSubJob(List<Long> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            log.error(ErrorMsg.JOB_ID_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_ID_NOT_EXIST.getMsg());
        }
        List<SubJob> subJobs = this.listByIds(ids);
        if (subJobs.isEmpty() || ids.size() != subJobs.size()) {
            log.error(ErrorMsg.SUB_JOB_ID_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_ID_NOT_EXIST.getMsg());
        }
        //子作业运行中
        boolean isExistRunning = subJobs.stream().anyMatch(subJob -> subJob.getSubJobStatus() == SubJobStatusEnum.RUNNING.getValue());
        if (isExistRunning) {
            log.error(ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
        }
        //子作业节点是否存在运行中节点
        List<SubJobNode> subJobNodeList = null;
        if (!ids.isEmpty()) {
            subJobNodeList = subJobNodeService.queryBatchSubJobListNodeByJobId(ids.toArray());
            boolean isRunningNode = subJobNodeList.stream().anyMatch(subJobNode -> subJobNode.getRunStatus() == SubJobStatusEnum.RUNNING.getValue());
            if (isRunningNode) {
                log.error(ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_DELETE.getMsg());
            }
        }
        //批量更新子作业状态
        LambdaUpdateWrapper<SubJob> updateSubJobWrapper = Wrappers.lambdaUpdate();
        updateSubJobWrapper.set(SubJob::getStatus, StatusEnum.UN_VALID.getValue());
        updateSubJobWrapper.set(SubJob::getUpdateTime, now());
        updateSubJobWrapper.in(SubJob::getId, ids);
        this.update(updateSubJobWrapper);
        //批量更新子作业节点状态
        if(!Objects.isNull(subJobNodeList) && !subJobNodeList.isEmpty()){
            Object[] subJobNodeArrayIds = subJobNodeList.stream().map(SubJobNode::getId).toArray();
            subJobNodeService.updateBatchStatus(subJobNodeArrayIds, StatusEnum.UN_VALID.getValue());
        }
    }


    /**
     * 停止子作业节点任务
     *
     * @param terminateTaskRequestDto 节点请求信息
     * @return 是否停止
     */
    private boolean terminateTask(TerminateTaskRequestDto terminateTaskRequestDto) {
        TerminateTaskRespDto terminateTaskRespDto = grpcTaskService.terminateTask(terminateTaskRequestDto);
        if (terminateTaskRespDto.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
            return subJobNodeService.updateRunStatus(terminateTaskRequestDto.getNodeId(), SubJobNodeStatusEnum.UN_RUN.getValue());
        } else {
            log.error("Terminate subJobNode with subJobNodeId:{} fail", terminateTaskRequestDto.getNodeId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_TERMINATE_NET_PROCESS_ERROR.getMsg());
        }
    }

    /**
     * 组装停止所有子作业节点请求参数
     *
     * @param subJob 子作业
     * @return List<TerminateTaskRequestDto>  需要停止所有子作业节点请求参数
     */
    public List<TerminateTaskRequestDto> assemblyTerminateTaskRequestDto(SubJob subJob) {
        Workflow workflow = workflowService.queryWorkflowDetail(subJob.getWorkflowId());
        List<SubJobNode> subJobNodeList = subJobNodeService.querySubJobNodeListBySubJobId(subJob.getId());
        List<TerminateTaskRequestDto> terminateTaskRequestDtoList = new ArrayList<>();
        subJobNodeList.forEach(subJobNode -> {
            TerminateTaskRequestDto terminateTaskRequestDto = new TerminateTaskRequestDto();
            terminateTaskRequestDto.setUser(workflow.getAddress());
            terminateTaskRequestDto.setUserType(UserTypeEnum.checkUserType(workflow.getAddress()));
            terminateTaskRequestDto.setSign(workflow.getSign());
            terminateTaskRequestDto.setTaskId(subJobNode.getTaskId());
            terminateTaskRequestDto.setNodeStep(subJobNode.getNodeStep());
            terminateTaskRequestDto.setNodeId(subJobNode.getId());
            terminateTaskRequestDto.setNodeRunStatus(subJobNode.getRunStatus());
            terminateTaskRequestDtoList.add(terminateTaskRequestDto);
        });
        return terminateTaskRequestDtoList;
    }

    public IPage<SubJobDto> convertToPageDto(Page<SubJob> page) {
        List<SubJobDto> subJobDtoList = new ArrayList<>();
        page.getRecords().forEach(subJob -> {
            SubJobDto subJobDto = new SubJobDto();
            BeanCopierUtils.copy(subJob, subJobDto);
            subJobDtoList.add(subJobDto);
        });
        IPage<SubJobDto> pageDto = new Page<>();
        pageDto.setCurrent(page.getCurrent());
        pageDto.setRecords(subJobDtoList);
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotal());
        return pageDto;

    }
}
