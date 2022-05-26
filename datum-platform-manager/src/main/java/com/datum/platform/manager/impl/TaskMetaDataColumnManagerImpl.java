package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.TaskMetaDataColumnManager;
import com.datum.platform.mapper.TaskMetaDataColumnMapper;
import com.datum.platform.mapper.domain.TaskMetaDataColumn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskMetaDataColumnManagerImpl extends ServiceImpl<TaskMetaDataColumnMapper, TaskMetaDataColumn> implements TaskMetaDataColumnManager {

}
