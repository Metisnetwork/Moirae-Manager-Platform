package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.WorkflowTempMapper;
import com.moirae.rosettaflow.mapper.domain.WorkflowTemp;
import com.moirae.rosettaflow.service.IWorkflowTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 工作流模板服务实现类
 * @author hudenian
 * @date 2021/9/7
 */
@Slf4j
@Service
public class WorkflowTempServiceImpl extends ServiceImpl<WorkflowTempMapper, WorkflowTemp> implements IWorkflowTempService {

    @Override
    public WorkflowTemp getWorkflowTemplate(long projectTempId) {
        LambdaQueryWrapper<WorkflowTemp> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkflowTemp::getProjectTempId, projectTempId);
        return this.getOne(queryWrapper);
    }
}
