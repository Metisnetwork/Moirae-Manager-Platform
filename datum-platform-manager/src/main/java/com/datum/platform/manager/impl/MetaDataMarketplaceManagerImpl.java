package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.MetaDataMarketplaceManager;
import com.datum.platform.mapper.MetaDataMarketplaceMapper;
import com.datum.platform.mapper.domain.MetaDataMarketplace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据市场可见的元数据 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Service
public class MetaDataMarketplaceManagerImpl extends ServiceImpl<MetaDataMarketplaceMapper, MetaDataMarketplace> implements MetaDataMarketplaceManager {

    @Override
    @Transactional
    public boolean batchReplace(List<MetaDataMarketplace> metaDataMarketplaceList) {
        Set<String> dbMetaDataIdSet = list().stream().map(MetaDataMarketplace::getMetaDataId).collect(Collectors.toSet());
        List<MetaDataMarketplace> addList = metaDataMarketplaceList.stream()
                .filter(metaDataMarketplace -> !dbMetaDataIdSet.contains(metaDataMarketplace.getMetaDataId()))
                .collect(Collectors.toList());

        Set<String> setList = metaDataMarketplaceList.stream().map(MetaDataMarketplace::getMetaDataId).collect(Collectors.toSet());
        Set<String> delList = dbMetaDataIdSet.stream().filter(metaDataId -> !setList.contains(metaDataId)).collect(Collectors.toSet());

        if(addList.size() > 0){
            saveBatch(addList);
        }
        if(delList.size() > 0){
            removeByIds(delList);
        }
        return false;
    }
}
