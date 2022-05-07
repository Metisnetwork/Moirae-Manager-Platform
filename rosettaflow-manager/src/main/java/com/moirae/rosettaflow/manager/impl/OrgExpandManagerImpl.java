package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.OrgExpandManager;
import com.moirae.rosettaflow.mapper.OrgExpandMapper;
import com.moirae.rosettaflow.mapper.domain.OrgExpand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class OrgExpandManagerImpl extends ServiceImpl<OrgExpandMapper, OrgExpand> implements OrgExpandManager {

    @Resource
    private OrgExpandMapper orgExpandMapper;

    @Override
    public List<OrgExpand> getOrgExpandList() {
        LambdaQueryWrapper<OrgExpand> wrapper = Wrappers.lambdaQuery();
        wrapper.select(OrgExpand::getIdentityId, OrgExpand::getObserverProxyWalletAddress);
        wrapper.isNull(OrgExpand::getObserverProxyWalletAddress);
        return list(wrapper);
    }
}
