package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskPowerProviderManager;
import com.moirae.rosettaflow.mapper.TaskPowerProviderMapper;
import com.moirae.rosettaflow.mapper.domain.TaskPowerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskPowerProviderManagerImpl extends ServiceImpl<TaskPowerProviderMapper, TaskPowerProvider> implements TaskPowerProviderManager {

}
