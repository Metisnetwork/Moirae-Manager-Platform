package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.WorkflowNodeTempMapper;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeTemp;
import com.platon.rosettaflow.service.IWorkflowNodeTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流节点模板服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeTempServiceImpl extends ServiceImpl<WorkflowNodeTempMapper, WorkflowNodeTemp> implements IWorkflowNodeTempService {
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }
}
