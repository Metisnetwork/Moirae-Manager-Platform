package com.platon.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.common.enums.SubJobStatusEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.SubJobDto;
import com.platon.rosettaflow.mapper.SubJobMapper;
import com.platon.rosettaflow.mapper.domain.SubJob;
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
        //停止子作业
        LambdaUpdateWrapper<SubJob> subJobUpdateWrapper = Wrappers.lambdaUpdate();
        subJobUpdateWrapper.set(SubJob::getSubJobStatus,SubJobStatusEnum.UN_RUN);
        subJobUpdateWrapper.set(SubJob::getUpdateTime,new Date(System.currentTimeMillis()));
        subJobUpdateWrapper.eq(SubJob::getId,id);
        this.update(subJobUpdateWrapper);
        //停止工作流
        workflowService.terminate(subJob.getWorkflowId());
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
        //todo 启动工作流

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
