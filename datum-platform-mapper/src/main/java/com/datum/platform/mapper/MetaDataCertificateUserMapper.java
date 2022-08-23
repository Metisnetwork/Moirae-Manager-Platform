package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.MetaDataCertificateUser;

import java.util.List;

/**
 * <p>
 * 用户持有的元数据凭证 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
public interface MetaDataCertificateUserMapper extends BaseMapper<MetaDataCertificateUser> {

    boolean updateBatch(List<MetaDataCertificateUser> updateList);

    MetaDataCertificateUser countByMetaDataIdAndUser(String address, String metaDataId);
}
