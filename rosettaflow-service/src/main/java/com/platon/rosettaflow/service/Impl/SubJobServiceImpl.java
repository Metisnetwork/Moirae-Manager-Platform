package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.SubJobDto;
import com.platon.rosettaflow.mapper.SubJobMapper;
import com.platon.rosettaflow.mapper.domain.SubJob;
import com.platon.rosettaflow.service.ISubJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 子作业管理服务实现类
 */
@Slf4j
@Service
public class SubJobServiceImpl extends ServiceImpl<SubJobMapper, SubJob> implements ISubJobService {


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

    }

    @Override
    public void reStart(Long id) {

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
