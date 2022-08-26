package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.PublicityManager;
import com.datum.platform.mapper.PublicityMapper;
import com.datum.platform.mapper.domain.Publicity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 公示信息 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Service
public class PublicityManagerImpl extends ServiceImpl<PublicityMapper, Publicity> implements PublicityManager {

    @Override
    public List<Publicity> listNeedSync() {
        return baseMapper.listNeedSync();
    }

    @Override
    public boolean saveBatch(Set<String> publicityIdSet) {
        if(publicityIdSet.isEmpty()){
            return true;
        }
        LambdaQueryWrapper<Publicity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(Publicity::getId);
        queryWrapper.in(Publicity::getId, publicityIdSet);
        List<String> dbIdList = listObjs(queryWrapper, item -> item.toString());
        List<Publicity> saveList = publicityIdSet.stream()
                .filter(publicityId -> ! dbIdList.contains(publicityId))
                .map(publicityId -> {
                    Publicity publicity = new Publicity();
                    publicity.setId(publicityId);
                    return publicity;
                })
                .collect(Collectors.toList());
        if(saveList.size() > 0){
            saveBatch(saveList);
        }
        return true;
    }
}
