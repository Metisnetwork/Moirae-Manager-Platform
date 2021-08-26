package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.UserMetaDataDto;
import com.platon.rosettaflow.mapper.UserMetaDataMapper;
import com.platon.rosettaflow.mapper.domain.UserMetaData;
import com.platon.rosettaflow.service.IUserMetaDataService;
import com.platon.rosettaflow.service.utils.UserContext;
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
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    @Override
    public IPage<UserMetaDataDto> list(Long current, Long size,String dataName) {
        Page<UserMetaData> page = new Page<>(current, size);
        if (null == UserContext.get().getAddress()) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_UN_LOGIN.getMsg());
        }
        return this.baseMapper.listByOwner(page, UserContext.get().getAddress(),dataName);
    }
}
