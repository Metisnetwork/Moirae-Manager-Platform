package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.MetaDataCertificateManager;
import com.datum.platform.mapper.MetaDataCertificateMapper;
import com.datum.platform.mapper.domain.MetaDataCertificate;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 元数据凭证 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Service
public class MetaDataCertificateManagerImpl extends ServiceImpl<MetaDataCertificateMapper, MetaDataCertificate> implements MetaDataCertificateManager {

    @Override
    public MetaDataCertificate getNoAttributeCredentialByMetaDataId(String metaDataId) {
        return baseMapper.getNoAttributeCredentialByMetaDataId(metaDataId);
    }

    @Override
    public MetaDataCertificate getNoAttributeCredentialByMetaDataIdAndUser(String metaDataId, String address) {
        return baseMapper.getNoAttributeCredentialByMetaDataIdAndUser(metaDataId, address);
    }

    @Override
    public IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataId(Page<MetaDataCertificate> page, String metaDataId) {
        return baseMapper.pageHaveAttributesCertificateByMetaDataId(page, metaDataId);
    }

    @Override
    public IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataIdAndUser(Page<MetaDataCertificate> page, String metaDataId, String address) {
        return baseMapper.pageHaveAttributesCertificateByMetaDataIdAndUser(page, metaDataId, address);
    }

    @Override
    public List<MetaDataCertificate> listHaveAttributesCertificateByMetaDataIdAndUser(String metaDataId, String address) {
        return baseMapper.listHaveAttributesCertificateByMetaDataIdAndUser(metaDataId, address);
    }

    @Override
    public List<MetaDataCertificate> listCertificateByMetaDataIdListAndUser(List<Long> credentialIdList, String address) {
        return baseMapper.listCertificateByMetaDataIdListAndUser(credentialIdList, address);
    }

    @Override
    public boolean saveOrUpdateOrDeleteBatch(String metaDataId, List<MetaDataCertificate> metaDataCertificateList) {
        LambdaQueryWrapper<MetaDataCertificate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataCertificate::getMetaDataId, metaDataId);
        Map<String, MetaDataCertificate> dbMap = list(queryWrapper).stream().collect(Collectors.toMap(MetaDataCertificate::getTokenId, me -> me));

        List<MetaDataCertificate> addList = new ArrayList<>();
        List<MetaDataCertificate> updateList = new ArrayList<>();
        metaDataCertificateList.forEach(item ->{
            if(dbMap.containsKey(item.getTokenId())){
                MetaDataCertificate dbItem = dbMap.get(item);
                if(StringUtils.isBlank(dbItem.getName()) && StringUtils.isNotBlank(item.getName())){
                    dbItem.setName(item.getName());
                    updateList.add(dbItem);
                }
            }else{
                addList.add(item);
            }
        });

        Set<String> tokenIdList = metaDataCertificateList.stream().map(MetaDataCertificate::getTokenId).collect(Collectors.toSet());
        Collection<MetaDataCertificate> dbTokenList = dbMap.values();
        Set<Long> deleteIdList = dbTokenList.stream().filter(item -> !tokenIdList.contains(item.getTokenId())).map(MetaDataCertificate::getId).collect(Collectors.toSet());

        if(addList.size() > 0){
            saveBatch(addList);
        }

        if(updateList.size() > 0){
            updateBatchById(updateList);
        }

        if(deleteIdList.size() > 0){
            removeByIds(deleteIdList);
        }
        return true;
    }

    @Override
    public boolean updateNameByTokenAddress(String address, String name) {
        LambdaUpdateWrapper<MetaDataCertificate> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(MetaDataCertificate::getName, name);
        updateWrapper.eq(MetaDataCertificate::getTokenAddress, address);
        return update(updateWrapper);
    }

    @Override
    public List<MetaDataCertificate> listKey() {
        LambdaQueryWrapper<MetaDataCertificate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(MetaDataCertificate::getId, MetaDataCertificate::getType,MetaDataCertificate::getTokenAddress, MetaDataCertificate::getTokenId);
        return list(queryWrapper);
    }

    @Override
    public boolean existNoAttributes(String metaDataId, String tokenAddress) {
        LambdaQueryWrapper<MetaDataCertificate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataCertificate::getMetaDataId, metaDataId);
        queryWrapper.eq(MetaDataCertificate::getTokenAddress, tokenAddress);
        queryWrapper.eq(MetaDataCertificate::getType, MetaDataCertificateTypeEnum.NO_ATTRIBUTES);
        return count(queryWrapper) > 0;
    }

    @Override
    public String getName(MetaDataCertificateTypeEnum metaDataCertificateTypeEnum, String metaDataId, String consumeTokenAddress, String consumeTokenId) {
        LambdaQueryWrapper<MetaDataCertificate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataCertificate::getMetaDataId, metaDataId);
        queryWrapper.eq(MetaDataCertificate::getTokenAddress, consumeTokenAddress);
        queryWrapper.eq(MetaDataCertificate::getType, metaDataCertificateTypeEnum);
        if(metaDataCertificateTypeEnum == MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES){
            queryWrapper.eq(MetaDataCertificate::getTokenId, consumeTokenId);
        }
        MetaDataCertificate result = getOne(queryWrapper);
        if(result != null){
            return result.getName();
        }
        return null;
    }

    @Override
    public List<MetaDataCertificate> listByMetaDataId(String metaDataId) {
        LambdaQueryWrapper<MetaDataCertificate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MetaDataCertificate::getMetaDataId, metaDataId);
        return list(wrapper);
    }
}
