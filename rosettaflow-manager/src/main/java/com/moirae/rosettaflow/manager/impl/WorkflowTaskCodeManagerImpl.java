package com.moirae.rosettaflow.manager.impl;

import com.moirae.rosettaflow.mapper.domain.WorkflowTaskCode;
import com.moirae.rosettaflow.mapper.WorkflowTaskCodeMapper;
import com.moirae.rosettaflow.manager.WorkflowTaskCodeManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工作流任务算法代码表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-03-30
 */
@Service
public class WorkflowTaskCodeManagerImpl extends ServiceImpl<WorkflowTaskCodeMapper, WorkflowTaskCode> implements WorkflowTaskCodeManager {

}
