package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.UserLogin;

public interface UserLoginManager extends IService<UserLogin> {

    void successRecord(String hexAddress);

    void failRecord(String hexAddress);
}
