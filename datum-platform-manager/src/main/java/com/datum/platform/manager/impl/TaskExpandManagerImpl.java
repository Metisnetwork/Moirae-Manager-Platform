package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TaskExpandManager;
import com.datum.platform.mapper.TaskExpandMapper;
import com.datum.platform.mapper.domain.TaskExpand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskExpandManagerImpl extends ServiceImpl<TaskExpandMapper, TaskExpand> implements TaskExpandManager {
}
