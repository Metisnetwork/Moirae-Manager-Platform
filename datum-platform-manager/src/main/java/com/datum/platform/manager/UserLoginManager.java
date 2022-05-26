package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.UserLogin;

public interface UserLoginManager extends IService<UserLogin> {

    void successRecord(String hexAddress);

    void failRecord(String hexAddress);

    int countOfActiveAddress();
}
