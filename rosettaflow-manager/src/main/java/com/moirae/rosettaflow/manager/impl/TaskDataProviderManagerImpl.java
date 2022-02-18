package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskDataProviderManager;
import com.moirae.rosettaflow.mapper.TaskDataProviderMapper;
import com.moirae.rosettaflow.mapper.domain.TaskDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskDataProviderManagerImpl extends ServiceImpl<TaskDataProviderMapper, TaskDataProvider> implements TaskDataProviderManager {

}
