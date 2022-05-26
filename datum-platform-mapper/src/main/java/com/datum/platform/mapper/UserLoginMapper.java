package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.UserLogin;

/**
 * @author admin
 */
public interface UserLoginMapper extends BaseMapper<UserLogin> {
    int countOfActiveAddress();
}
