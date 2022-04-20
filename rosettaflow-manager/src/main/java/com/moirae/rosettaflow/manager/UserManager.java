package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.User;

import java.util.List;

public interface UserManager extends IService<User> {

    User getValidById(String address);

    User getValidByUserName(String userName);

    boolean updateHeartBeat(String address);

    List<String> getOnlineUserIdList(long loginTimeOut);
}
