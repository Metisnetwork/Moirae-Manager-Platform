package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.UserLoginManager;
import com.datum.platform.mapper.UserLoginMapper;
import com.datum.platform.mapper.domain.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserLoginManagerImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginManager {

    @Override
    public void successRecord(String hexAddress) {
        UserLogin userLogin = new UserLogin();
        userLogin.setAddress(hexAddress);
        userLogin.setIsSuccess(true);
        save(userLogin);
    }

    @Override
    public void failRecord(String hexAddress) {
        UserLogin userLogin = new UserLogin();
        userLogin.setAddress(hexAddress);
        userLogin.setIsSuccess(false);
        save(userLogin);
    }

    @Override
    public int countOfActiveAddress() {
        return this.baseMapper.countOfActiveAddress();
    }
}
