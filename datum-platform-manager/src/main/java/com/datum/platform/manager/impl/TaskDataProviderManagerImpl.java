package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TaskDataProviderManager;
import com.datum.platform.mapper.TaskDataProviderMapper;
import com.datum.platform.mapper.domain.TaskDataProvider;
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
    public Long countOfTokenUsed(List<String> metaDataIdList) {
        LambdaQueryWrapper<TaskDataProvider> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TaskDataProvider::getMetaDataId, metaDataIdList);
        return Long.valueOf(count(wrapper));
    }
}
