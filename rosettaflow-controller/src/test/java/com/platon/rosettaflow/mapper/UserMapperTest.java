package com.platon.rosettaflow.mapper;

import com.platon.rosettaflow.mapper.domain.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author admin
 * @date 2021/7/20
 */
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    @Disabled
    public void test() {
        User user = userMapper.selectById(1L);
        System.out.println(user.getUserName());
    }
}
