package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.TaskResultMapper;
import com.platon.rosettaflow.mapper.domain.TaskResult;
import com.platon.rosettaflow.service.ITaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/10/14
 */
@Slf4j
@Service
public class TaskResultServiceImpl extends ServiceImpl<TaskResultMapper, TaskResult> implements ITaskResultService {
}
