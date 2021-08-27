package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.TaskEventMapper;
import com.platon.rosettaflow.mapper.domain.TaskEvent;
import com.platon.rosettaflow.service.ITaskEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 任务事件服务实现类
 */
@Slf4j
@Service
public class TaskEventServiceImpl extends ServiceImpl<TaskEventMapper, TaskEvent> implements ITaskEventService {
}
