package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TaskAlgoProviderManager;
import com.datum.platform.mapper.TaskAlgoProviderMapper;
import com.datum.platform.mapper.domain.TaskAlgoProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskAlgoProviderManagerImpl extends ServiceImpl<TaskAlgoProviderMapper, TaskAlgoProvider> implements TaskAlgoProviderManager {

}
