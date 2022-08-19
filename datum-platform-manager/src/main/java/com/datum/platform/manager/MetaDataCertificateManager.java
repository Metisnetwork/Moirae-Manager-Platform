package com.datum.platform.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.MetaDataCertificate;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;

import java.util.List;

/**
 * <p>
 * 元数据凭证 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
public interface MetaDataCertificateManager extends IService<MetaDataCertificate> {

    MetaDataCertificate getNoAttributeCredentialByMetaDataId(String metaDataId);

    MetaDataCertificate getNoAttributeCredentialByMetaDataIdAndUser(String metaDataId, String address);

    IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataId(Page<MetaDataCertificate> page, String metaDataId);

    IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataIdAndUser(Page<MetaDataCertificate> page, String metaDataId, String address);

    List<MetaDataCertificate> listHaveAttributesCertificateByMetaDataIdAndUser(String metaDataId, String address);

    List<MetaDataCertificate> listCertificateByMetaDataIdListAndUser(List<Long> credentialIdList, String address);

    boolean saveOrUpdateOrDeleteBatch(String metaDataId, List<MetaDataCertificate> insertMetaDataCertificateList, List<MetaDataCertificate> updateMetaDataCertificateList, List<Long> deleteIdList);

    boolean updateNameByTokenAddress(String address, String name);

    List<MetaDataCertificate> listKey();

    boolean existNoAttributes(String metaDataId, String tokenAddress);

    String getName(MetaDataCertificateTypeEnum metaDataCertificateTypeEnum, String metaDataId, String consumeTokenAddress, String consumeTokenId);

    List<MetaDataCertificate> listByMetaDataId(String metaDataId);

    List<String> listMetaDataIdByIds(List<Long> metaDataCertificateIdList);

    void saveOrSelectUpdate(MetaDataCertificate metaDataCertificate);

    List<MetaDataCertificate> listEffectiveByMetaDataId(String metaDataId);
}
