package com.datum.platform.manager.impl;

import com.datum.platform.mapper.domain.MetaDataUser;
import com.datum.platform.mapper.MetaDataUserMapper;
import com.datum.platform.manager.MetaDataUserManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户可见的元数据 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Service
public class MetaDataUserManagerImpl extends ServiceImpl<MetaDataUserMapper, MetaDataUser> implements MetaDataUserManager {

}
