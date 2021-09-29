package com.platon.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.*;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.SubJobDto;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.task.req.dto.TerminateTaskRequestDto;
import com.platon.rosettaflow.grpc.task.req.dto.TerminateTaskRespDto;
import com.platon.rosettaflow.mapper.SubJobMapper;
import com.platon.rosettaflow.mapper.domain.SubJob;
import com.platon.rosettaflow.mapper.domain.SubJobNode;
import com.platon.rosettaflow.mapper.domain.Workflow;
import com.platon.rosettaflow.service.ISubJobNodeService;
import com.platon.rosettaflow.service.ISubJobService;
import com.platon.rosettaflow.service.IWorkflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    public void pause(Long id) {
        SubJob subJob = this.getById(id);
        if(Objects.isNull(subJob)){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        if(subJob.getSubJobStatus() != SubJobStatusEnum.RUNNING.getValue()){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_NOT_RUNNING.getMsg());
        }
        //暂停子作业
        TerminateTaskRequestDto terminateTaskRequestDto = assemblyTerminateTaskRequestDto(subJob);
        TerminateTaskRespDto terminateTaskRespDto = grpcTaskService.terminateTask(terminateTaskRequestDto);
        if (terminateTaskRespDto.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
            this.updateJobStatus(subJob.getId(), SubJobStatusEnum.UN_RUN.getValue());
            subJobNodeService.updateRunStatusByJobId(subJob.getId(), SubJobNodeStatusEnum.UN_RUN.getValue());
        } else {
            log.error("Terminate subJob with id:{} fail", subJob.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_TERMINATE_NET_PROCESS_ERROR.getMsg());
        }
    }

    @Override
    public void reStart(Long id) {
        SubJob subJob = this.getById(id);
        if(Objects.isNull(subJob)){
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.JOB_NOT_EXIST.getMsg());
        }
        if(subJob.getSubJobStatus() == SubJobStatusEnum.RUNNING.getValue() || subJob.getSubJobStatus() == SubJobStatusEnum.RUN_SUCCESS.getValue()){
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
        startWorkflowDto.setJobId(subJob.getId());
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


    /**
     *  组装停止子作业请求参数
     * @param subJob 子作业
     */
    public TerminateTaskRequestDto assemblyTerminateTaskRequestDto(SubJob subJob){
        Workflow workflow = workflowService.queryWorkflowDetail(subJob.getWorkflowId());
        SubJobNode subJobNode = subJobNodeService.querySubJobNodeByJobId(subJob.getId());

        TerminateTaskRequestDto terminateTaskRequestDto = new TerminateTaskRequestDto();
        terminateTaskRequestDto.setUser(workflow.getAddress());
        terminateTaskRequestDto.setUserType(UserTypeEnum.checkUserType(workflow.getAddress()));
        terminateTaskRequestDto.setSign(workflow.getSign());
        terminateTaskRequestDto.setTaskId(subJobNode.getTaskId());
        return terminateTaskRequestDto;
    }

    public IPage<SubJobDto> convertToPageDto(Page<SubJob> page){
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
