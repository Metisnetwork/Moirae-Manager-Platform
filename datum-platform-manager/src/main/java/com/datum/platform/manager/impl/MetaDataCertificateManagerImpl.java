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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public boolean saveOrUpdateOrDeleteBatch(String metaDataId, List<MetaDataCertificate> insertMetaDataCertificateList, List<MetaDataCertificate> updateMetaDataCertificateList, List<Long> deleteIdList) {
        if(insertMetaDataCertificateList.size() > 0){
            saveBatch(insertMetaDataCertificateList);
        }

        if(updateMetaDataCertificateList.size() > 0){
            updateBatchById(updateMetaDataCertificateList);
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

    @Override
    public List<String> listMetaDataIdByIds(List<Long> metaDataCertificateIdList) {
        return this.baseMapper.listMetaDataIdByIds(metaDataCertificateIdList);
    }

    @Override
    public void saveOrSelectUpdate(MetaDataCertificate metaDataCertificate) {
        LambdaQueryWrapper<MetaDataCertificate> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MetaDataCertificate::getMetaDataId, metaDataCertificate.getMetaDataId());
        queryWrapper.eq(MetaDataCertificate::getTokenAddress, metaDataCertificate.getTokenAddress());
        queryWrapper.eq(MetaDataCertificate::getType, MetaDataCertificateTypeEnum.NO_ATTRIBUTES);

        MetaDataCertificate db = getOne(queryWrapper);
        // 不存在信息
        if(db == null){
            save(metaDataCertificate);
        }

        // 存在的是设置不同则更新
        if(db != null && (!StringUtils.equals(db.getErc20CtAlgConsume(), metaDataCertificate.getErc20CtAlgConsume()) || !StringUtils.equals(db.getErc20PtAlgConsume(), metaDataCertificate.getErc20PtAlgConsume())) ){
            LambdaUpdateWrapper<MetaDataCertificate> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.set(MetaDataCertificate::getErc20CtAlgConsume, metaDataCertificate.getErc20CtAlgConsume());
            updateWrapper.set(MetaDataCertificate::getErc20PtAlgConsume, metaDataCertificate.getErc20PtAlgConsume());
            updateWrapper.eq(MetaDataCertificate::getId, db.getId());
            update(updateWrapper);
        }
    }

    @Override
    public List<MetaDataCertificate> listEffectiveByMetaDataId(String metaDataId) {
        return baseMapper.listEffectiveByMetaDataId(metaDataId);
    }
}
