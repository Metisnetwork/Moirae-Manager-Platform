package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.TaskManager;
import com.moirae.rosettaflow.manager.TaskMetaDataColumnManager;
import com.moirae.rosettaflow.mapper.TaskMapper;
import com.moirae.rosettaflow.mapper.TaskMetaDataColumnMapper;
import com.moirae.rosettaflow.mapper.domain.Task;
import com.moirae.rosettaflow.mapper.domain.TaskMetaDataColumn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskMetaDataColumnManagerImpl extends ServiceImpl<TaskMetaDataColumnMapper, TaskMetaDataColumn> implements TaskMetaDataColumnManager {

}
