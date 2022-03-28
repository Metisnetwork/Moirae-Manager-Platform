package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.UserLoginManager;
import com.moirae.rosettaflow.mapper.UserLoginMapper;
import com.moirae.rosettaflow.mapper.domain.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserLoginManagerImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginManager {

}
