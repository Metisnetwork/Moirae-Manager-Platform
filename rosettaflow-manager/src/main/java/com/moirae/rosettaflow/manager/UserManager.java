package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.User;

import java.util.List;

public interface UserManager extends IService<User> {

    User getValidById(String address);

    User getValidByUserName(String userName);

    List<User> getValidList();
}
