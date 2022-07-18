package com.datum.platform.manager;

import com.datum.platform.mapper.domain.MetaDataCertificateUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户持有的元数据凭证 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
public interface MetaDataCertificateUserManager extends IService<MetaDataCertificateUser> {

    MetaDataCertificateUser getById(Long metaDataCertificateId, String address);

    boolean saveOrUpdateBatchMetaDataCertificateUser(String address, List<MetaDataCertificateUser> metaDataCertificateUserList);

    List<Long> listMetaDataCertificateIdByAddress(String address);
}
