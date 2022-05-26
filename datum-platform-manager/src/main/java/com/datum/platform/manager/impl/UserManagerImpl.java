package com.datum.platform.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datum.platform.manager.UserManager;
import com.datum.platform.mapper.UserMapper;
import com.datum.platform.mapper.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserManagerImpl extends ServiceImpl<UserMapper, User> implements UserManager {

    @Override
    public User getValidById(String address) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAddress, address);
        wrapper.eq(User::getIsValid, true);
        return getOne(wrapper);
    }

    @Override
    public User getValidByUserName(String userName) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUserName, userName);
        wrapper.eq(User::getIsValid, true);
        return this.getOne(wrapper);
    }


    @Override
    public boolean updateHeartBeat(String address) {
        return baseMapper.updateHeartBeat(address);
    }

    @Override
    public List<String> getOnlineUserIdList(long loginTimeOut) {
        return baseMapper.getOnlineUserIdList(loginTimeOut);
    }
}
