package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.Proposal;
import com.datum.platform.mapper.ProposalMapper;
import com.datum.platform.manager.ProposalManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 提案信息 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Service
public class ProposalManagerImpl extends ServiceImpl<ProposalMapper, Proposal> implements ProposalManager {

    @Override
    public IPage<Proposal> list(Page<Proposal> page) {
        return baseMapper.list(page);
    }

    @Override
    public Proposal getDetailsById(String id) {
        return baseMapper.getDetailsById(id);
    }
}
