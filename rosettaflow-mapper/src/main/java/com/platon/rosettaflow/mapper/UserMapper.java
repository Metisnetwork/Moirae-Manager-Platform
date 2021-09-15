package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.User;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询所有用户昵称（不包含自己）
     * @param curUserId
     * @return
     */
    List<User> queryAllUserNickname(Long curUserId);
}