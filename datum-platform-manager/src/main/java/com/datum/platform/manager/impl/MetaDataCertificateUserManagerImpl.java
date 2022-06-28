package com.datum.platform.manager.impl;

import com.datum.platform.mapper.domain.MetaDataCertificateUser;
import com.datum.platform.mapper.MetaDataCertificateUserMapper;
import com.datum.platform.manager.MetaDataCertificateUserManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
