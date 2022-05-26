package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.User;

import java.util.List;

/**
 * @author admin
 */
public interface UserMapper extends BaseMapper<User> {

    boolean updateHeartBeat(String address);

    /**
     *  获得在线用户
     *
     * @param loginTimeOut 登录超时时间，单位秒
     * @return
     */
    List<String> getOnlineUserIdList(long loginTimeOut);
}
