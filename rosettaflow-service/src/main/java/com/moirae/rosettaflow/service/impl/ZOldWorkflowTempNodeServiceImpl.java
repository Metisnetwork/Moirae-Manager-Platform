package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ZOldWorkflowTempNodeMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowTempNode;
import com.moirae.rosettaflow.service.ZOldIWorkflowTempNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 工作流节点模板服务实现类
 */
@Slf4j
@Service
public class ZOldWorkflowTempNodeServiceImpl extends ServiceImpl<ZOldWorkflowTempNodeMapper, ZOldWorkflowTempNode> implements ZOldIWorkflowTempNodeService {

    @Override
    public List<ZOldWorkflowTempNode> getByWorkflowTempId(Long workflowTempId) {
        LambdaQueryWrapper<ZOldWorkflowTempNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ZOldWorkflowTempNode::getWorkflowTempId, workflowTempId);
        wrapper.orderByAsc(ZOldWorkflowTempNode::getId);
        return this.list(wrapper);
    }
}
