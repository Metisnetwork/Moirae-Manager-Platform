package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.MetaDataCertificate;
import com.datum.platform.mapper.MetaDataCertificateMapper;
import com.datum.platform.manager.MetaDataCertificateManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
}
