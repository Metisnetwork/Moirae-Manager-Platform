package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskAlgoProviderManager;
import com.moirae.rosettaflow.manager.TaskManager;
import com.moirae.rosettaflow.mapper.TaskAlgoProviderMapper;
import com.moirae.rosettaflow.mapper.TaskMapper;
import com.moirae.rosettaflow.mapper.domain.Task;
import com.moirae.rosettaflow.mapper.domain.TaskAlgoProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskAlgoProviderManagerImpl extends ServiceImpl<TaskAlgoProviderMapper, TaskAlgoProvider> implements TaskAlgoProviderManager {

}
