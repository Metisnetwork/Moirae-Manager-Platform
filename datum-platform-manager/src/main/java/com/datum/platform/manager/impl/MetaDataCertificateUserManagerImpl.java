package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.MetaDataCertificateUserManager;
import com.datum.platform.mapper.MetaDataCertificateUserMapper;
import com.datum.platform.mapper.domain.MetaDataCertificateUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户持有的元数据凭证 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Service
public class MetaDataCertificateUserManagerImpl extends ServiceImpl<MetaDataCertificateUserMapper, MetaDataCertificateUser> implements MetaDataCertificateUserManager {

    @Override
    public MetaDataCertificateUser getById(Long metaDataCertificateId, String address) {
        LambdaQueryWrapper<MetaDataCertificateUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataCertificateUser::getMetaDataCertificateId, metaDataCertificateId);
        queryWrapper.eq(MetaDataCertificateUser::getAddress, address);
        return getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateBatchMetaDataCertificateUser(String address, List<MetaDataCertificateUser> metaDataCertificateUserList) {
        // 查询用户的账户信息
        Set<Long> metaDataCertificateIdSet = listMetaDataCertificateIdByAddress(address).stream().collect(Collectors.toSet());

        List<MetaDataCertificateUser> insertList = metaDataCertificateUserList.stream().filter(item -> !metaDataCertificateIdSet.contains(item.getMetaDataCertificateId())).collect(Collectors.toList());
        List<MetaDataCertificateUser> updateList = metaDataCertificateUserList.stream().filter(item -> metaDataCertificateIdSet.contains(item.getMetaDataCertificateId())).collect(Collectors.toList());

        if(insertList.size()>0){
            saveBatch(insertList);
        }

        if(updateList.size()>0){
            baseMapper.updateBatch(updateList);
        }
        return true;
    }

    @Override
    public List<Long> listMetaDataCertificateIdByAddress(String address) {
        LambdaQueryWrapper<MetaDataCertificateUser> wrapper = Wrappers.lambdaQuery();
        wrapper.select(MetaDataCertificateUser::getMetaDataCertificateId);
        wrapper.eq(MetaDataCertificateUser::getAddress, address);
        return listObjs(wrapper, item -> (long)item);
    }

    @Override
    public MetaDataCertificateUser countByMetaDataIdAndUser(String address, String metaDataId) {
        return baseMapper.countByMetaDataIdAndUser(address, metaDataId);
    }
}
