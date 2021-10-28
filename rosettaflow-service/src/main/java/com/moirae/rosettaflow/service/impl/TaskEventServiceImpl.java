package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.TaskEventMapper;
import com.moirae.rosettaflow.mapper.domain.TaskEvent;
import com.moirae.rosettaflow.service.ITaskEventService;
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
