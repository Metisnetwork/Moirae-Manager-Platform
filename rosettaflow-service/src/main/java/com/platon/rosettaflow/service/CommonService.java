package com.platon.rosettaflow.service;

import cn.hutool.core.date.DateUtil;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 * @date 2021/8/2
 */
@Service
@Slf4j
public class CommonService {
    static final String CODE_DATE_FMT = "yyMMdd";
    static final long REDIS_CODE_KEY_EXPIRE_TIME = 24 * 60 * 60;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    String generateJobNo(String prefixKey) {
        String date = DateUtil.format(new Date(), CODE_DATE_FMT);
        String key = prefixKey + date;
        Long seq = redisTemplate.opsForValue().increment(key);
        if (null == seq) {
            log.error("Failed to call the increment method of redis.");
            throw new BusinessException(RespCodeEnum.EXCEPTION);
        }
        if (seq == 1L) {
            redisTemplate.expire(key, REDIS_CODE_KEY_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        return date + String.format("%06d", seq);
    }
}
