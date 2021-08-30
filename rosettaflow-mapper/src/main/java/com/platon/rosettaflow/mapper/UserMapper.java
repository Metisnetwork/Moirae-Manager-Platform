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
     * 查询所有用户昵称
     * @return
     */
    List<Map<String, Object>> queryAllUserNickname();
}