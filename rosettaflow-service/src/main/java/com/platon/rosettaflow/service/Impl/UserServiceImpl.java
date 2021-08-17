package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.mapper.UserMapper;
import com.platon.rosettaflow.mapper.domain.User;
import com.platon.rosettaflow.service.ITokenService;
import com.platon.rosettaflow.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author admin
 * @date 2021/8/16
 * @description 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private ITokenService tokenService;

    @Override
    public User getByAddress(String address) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAddress, address);
        return this.getOne(wrapper);
    }

    @Override
    public UserDto generatorToken(String address) {
        User user = this.getByAddress(address);
        if (user == null) {
            user = new User();
            user.setUserName(address);
            user.setAddress(address);
            user.setStatus(StatusEnum.VALID.getValue());
            this.save(user);
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setToken(tokenService.setToken(userDto));
        return userDto;
    }
}
