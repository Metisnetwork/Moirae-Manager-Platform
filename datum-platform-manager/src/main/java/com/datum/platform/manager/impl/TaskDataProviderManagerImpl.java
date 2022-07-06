package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TaskDataProviderManager;
import com.datum.platform.mapper.TaskDataProviderMapper;
import com.datum.platform.mapper.domain.TaskDataProvider;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskDataProviderManagerImpl extends ServiceImpl<TaskDataProviderMapper, TaskDataProvider> implements TaskDataProviderManager {

    @Override
    public List<TaskDataProvider> listByTaskId(String taskId) {
        LambdaQueryWrapper<TaskDataProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskDataProvider::getTaskId, taskId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Long countOfMetaDataCertificateUsed(String metaDataId, MetaDataCertificateTypeEnum type, String tokenAddress, String tokenId) {
        LambdaQueryWrapper<TaskDataProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TaskDataProvider::getMetaDataId, metaDataId);
        wrapper.eq(TaskDataProvider::getConsumeType, type == MetaDataCertificateTypeEnum.NO_ATTRIBUTES ? 2 : 3);
        wrapper.eq(TaskDataProvider::getConsumeTokenAddress, tokenAddress);
        if(type == MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES){
            wrapper.eq(TaskDataProvider::getConsumeTokenId, tokenId);
        }
        return Long.valueOf(count(wrapper));
    }
}
