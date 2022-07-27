package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.MetaDataCertificate;

import java.util.List;

/**
 * <p>
 * 元数据凭证 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
public interface MetaDataCertificateMapper extends BaseMapper<MetaDataCertificate> {

    MetaDataCertificate getNoAttributeCredentialByMetaDataId(String metaDataId);

    MetaDataCertificate getNoAttributeCredentialByMetaDataIdAndUser(String metaDataId, String address);

    IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataId(Page<MetaDataCertificate> page, String metaDataId);

    IPage<MetaDataCertificate> pageHaveAttributesCertificateByMetaDataIdAndUser(Page<MetaDataCertificate> page, String metaDataId, String address);

    List<MetaDataCertificate> listHaveAttributesCertificateByMetaDataIdAndUser(String metaDataId, String address);

    List<MetaDataCertificate> listCertificateByMetaDataIdListAndUser(List<Long> credentialIdList, String address);

    List<String> listMetaDataIdByIds(List<Long> credentialIdList);
}
