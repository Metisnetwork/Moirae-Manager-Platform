package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.ProposalLogManager;
import com.datum.platform.mapper.ProposalLogMapper;
import com.datum.platform.mapper.domain.ProposalLog;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 提案日志 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Service
public class ProposalLogManagerImpl extends ServiceImpl<ProposalLogMapper, ProposalLog> implements ProposalLogManager {

    @Override
    public ProposalLog getLatestOne() {
        LambdaQueryWrapper<ProposalLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(ProposalLog::getId);
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }

    @Override
    public boolean contain(BigInteger blockNumber, String transactionHash, BigInteger logIndex) {
        LambdaQueryWrapper<ProposalLog> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ProposalLog::getBlockNumber, blockNumber.toString());
        queryWrapper.eq(ProposalLog::getTxHash, transactionHash);
        queryWrapper.eq(ProposalLog::getLogIndex, logIndex.toString());
        return count(queryWrapper) == 1;
    }

    @Override
    public List<ProposalLog> listByPage(Long latestSynced, Long size) {
        return baseMapper.listByPage(latestSynced, size);
    }
}
