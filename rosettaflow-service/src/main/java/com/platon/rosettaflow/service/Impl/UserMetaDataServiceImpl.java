package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.UserMetaDataMapper;
import com.platon.rosettaflow.mapper.domain.UserMetaData;
import com.platon.rosettaflow.service.IUserMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 功能描述
 */
@Slf4j
@Service
public class UserMetaDataServiceImpl extends ServiceImpl<UserMetaDataMapper, UserMetaData> implements IUserMetaDataService {
}
