package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowNodeCodeMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowNodeCode;
import com.moirae.rosettaflow.service.ZOldIWorkflowNodeCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工作流代码服务实现类
 *
 * @author hudenian
 * @date 2021/8/18
 */
@Slf4j
@Service
public class ZOldWorkflowNodeCodeServiceImpl extends ServiceImpl<ZOldWorkflowNodeCodeMapper, ZOldWorkflowNodeCode> implements ZOldIWorkflowNodeCodeService {

    @Override
    public ZOldWorkflowNodeCode queryByWorkflowNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<ZOldWorkflowNodeCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        return this.getOne(wrapper);
    }

    @Override
    public void batchInsert(List<ZOldWorkflowNodeCode> workflowNodeCodeList) {
        this.baseMapper.batchInsert(workflowNodeCodeList);
    }
}
