package com.platon.rosettaflow.service;

import cn.hutool.core.date.DateUtil;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 * @date 2021/8/2
 */
@Service
@Slf4j
public class CommonService {
    static final String CODE_DATE_FMT = "yyMMdd";
    static final String DATE_TIME_FMT = "yyyyMMddHHmmss";
    static final long REDIS_CODE_KEY_EXPIRE_TIME = 24 * 60 * 60;
    static final String JOB_NO_PRE = "task_";
    static final String TASK_NAME_PRE = "task_";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成任务编号
     *
     * @return 任务编号
     */
    public String generateJobNo() {
        String date = DateUtil.format(new Date(), CODE_DATE_FMT);
        String key = JOB_NO_PRE + date;
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

    /**
     * 根据工作流节点id生成任务名称
     *
     * @param workflowNodeId 工作流节点id
     * @return 任务名称
     */
    public String generateTaskName(Long workflowNodeId) {
        return TASK_NAME_PRE + workflowNodeId + "_" + DateUtil.format(new Date(), DATE_TIME_FMT);
    }

    /**
     * 生成随机uuid
     *
     * @return 返回uuid
     */
    public String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
