package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.OrgVcManager;
import com.datum.platform.mapper.OrgVcMapper;
import com.datum.platform.mapper.domain.OrgVc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 组织的VC 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Service
public class OrgVcManagerImpl extends ServiceImpl<OrgVcMapper, OrgVc> implements OrgVcManager {

    @Override
    public List<String> listId() {
        LambdaQueryWrapper<OrgVc> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.select(OrgVc::getIdentityId);
        return listObjs(queryWrapper, item -> item.toString());
    }

    @Override
    public List<OrgVc> listLatest(Integer size) {
        return baseMapper.listLatest(size);
    }

    @Override
    public IPage<OrgVc> list(Page<OrgVc> page) {
        return baseMapper.list(page);
    }

    @Override
    public void saveBatchIfAbsent(List<OrgVc> orgVcList) {
        List<String> db = listId();
        List<OrgVc> save = orgVcList.stream()
                .filter(orgVc -> !db.contains(orgVc.getIdentityId()))
                .collect(Collectors.toList());
        if(save.size() > 0){
            saveBatch(save);
        }
    }
}
