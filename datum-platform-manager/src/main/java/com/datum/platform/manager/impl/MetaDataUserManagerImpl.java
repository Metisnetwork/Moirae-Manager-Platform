package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.MetaDataUserManager;
import com.datum.platform.mapper.MetaDataUserMapper;
import com.datum.platform.mapper.domain.MetaDataUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户可见的元数据 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Service
public class MetaDataUserManagerImpl extends ServiceImpl<MetaDataUserMapper, MetaDataUser> implements MetaDataUserManager {

    @Override
    public boolean saveOrDeleteBatch(String address, List<String> metaDataIdList) {
        LambdaQueryWrapper<MetaDataUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(MetaDataUser::getMetaDataId);
        queryWrapper.eq(MetaDataUser::getAddress, address);
        List<String> dbMetaDataIdList = listObjs(queryWrapper, item -> (String)item);
        List<MetaDataUser> addList =  metaDataIdList.stream()
                .filter(metaDataId -> !dbMetaDataIdList.contains(metaDataId))
                .map(metaDataId -> {
                    MetaDataUser metaDataUser = new MetaDataUser();
                    metaDataUser.setMetaDataId(metaDataId);
                    metaDataUser.setAddress(address);
                    return metaDataUser;
                })
                .collect(Collectors.toList());
        List<String> deleteMetaDataIdList = dbMetaDataIdList.stream()
                .filter(metaDataId -> !metaDataIdList.contains(metaDataId))
                .collect(Collectors.toList());

        if(addList.size() > 0){
            baseMapper.saveBatch(addList);
        }

        if(deleteMetaDataIdList.size() > 0){
            LambdaQueryWrapper<MetaDataUser> removeWrapper = Wrappers.lambdaQuery();
            removeWrapper.eq(MetaDataUser::getAddress, address);
            removeWrapper.in(MetaDataUser::getMetaDataId, deleteMetaDataIdList);
            remove(removeWrapper);
        }
        return true;
    }
}
