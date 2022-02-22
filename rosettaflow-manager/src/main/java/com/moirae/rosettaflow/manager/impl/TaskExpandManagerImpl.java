package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskExpandManager;
import com.moirae.rosettaflow.mapper.TaskExpandMapper;
import com.moirae.rosettaflow.mapper.domain.TaskExpand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskExpandManagerImpl extends ServiceImpl<TaskExpandMapper, TaskExpand> implements TaskExpandManager {
}
