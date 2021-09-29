package com.platon.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.SubJobNodeMapper;
import com.platon.rosettaflow.mapper.domain.SubJobNode;
import com.platon.rosettaflow.service.ISubJobNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 功能描述
 */
@Slf4j
@Service
public class SubJobNodeServiceImpl extends ServiceImpl<SubJobNodeMapper, SubJobNode> implements ISubJobNodeService {


    @Override
    public void updateRunStatusByJobId(Long subJobId, Byte runStatus) {
        LambdaUpdateWrapper<SubJobNode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(SubJobNode::getRunStatus, runStatus);
        updateWrapper.set(SubJobNode::getUpdateTime, new Date(System.currentTimeMillis()));
        updateWrapper.eq(SubJobNode::getSubJobId, subJobId);
        this.update(updateWrapper);
    }

    @Override
    public SubJobNode querySubJobNodeByJobId(Long subJobId) {
        LambdaQueryWrapper<SubJobNode> querySubJobNode = Wrappers.lambdaQuery();
        querySubJobNode.eq(SubJobNode::getSubJobId, subJobId);
        return this.getOne(querySubJobNode);
    }

    @Override
    public SubJobNode querySubJobNodeByJobIdAndNodeStep(Long subJobId, Integer nodeStep) {
        LambdaQueryWrapper<SubJobNode> querySubJobNode = Wrappers.lambdaQuery();
        querySubJobNode.eq(SubJobNode::getSubJobId, subJobId);
        querySubJobNode.eq(SubJobNode::getNodeStep, nodeStep);
        return this.getOne(querySubJobNode);
    }
}
