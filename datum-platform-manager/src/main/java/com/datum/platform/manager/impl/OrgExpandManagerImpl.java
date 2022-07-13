package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.OrgExpandManager;
import com.datum.platform.mapper.OrgExpandMapper;
import com.datum.platform.mapper.domain.OrgExpand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrgExpandManagerImpl extends ServiceImpl<OrgExpandMapper, OrgExpand> implements OrgExpandManager {
    @Override
    public boolean saveOfNotExist(List<OrgExpand> orgExpandList) {
        LambdaQueryWrapper<OrgExpand> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(OrgExpand::getIdentityId);
        Set<String> idSet = listObjs(queryWrapper, item -> item.toString()).stream().collect(Collectors.toSet());
        List<OrgExpand> addList = orgExpandList.stream().filter(item -> !idSet.contains(item.getIdentityId())).collect(Collectors.toList());
        if(addList.size() > 0){
            return saveBatch(addList);
        }
        return true;
    }

    @Override
    public List<OrgExpand> listHaveIpPort() {
        LambdaQueryWrapper<OrgExpand> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.isNotNull(OrgExpand::getIdentityIp);
        queryWrapper.isNotNull(OrgExpand::getIdentityPort);
        return list(queryWrapper);
    }

    @Override
    public IPage<OrgExpand> list(Page<OrgExpand> page) {
        return baseMapper.list(page);
    }

    @Override
    public Integer countOfAuthority() {
        return baseMapper.countOfAuthority();
    }
}
