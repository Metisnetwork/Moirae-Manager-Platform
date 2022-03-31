package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowTempMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowTemp;
import com.moirae.rosettaflow.service.ZOldIWorkflowTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 工作流模板服务实现类
 * @author hudenian
 * @date 2021/9/7
 */
@Slf4j
@Service
public class ZOldWorkflowTempServiceImpl extends ServiceImpl<ZOldWorkflowTempMapper, ZOldWorkflowTemp> implements ZOldIWorkflowTempService {

    @Override
    public ZOldWorkflowTemp getWorkflowTemplate(long projectTempId) {
        LambdaQueryWrapper<ZOldWorkflowTemp> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ZOldWorkflowTemp::getProjectTempId, projectTempId);
        return this.getOne(queryWrapper);
    }
}
