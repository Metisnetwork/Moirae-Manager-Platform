package com.platon.rosettaflow.service;

import cn.hutool.core.date.DateUtil;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author admin
 * @date 2021/8/2
 */
@Service
@Slf4j
public class CommonService {
    static final String DATE_TIME_FMT = "yyyyMMddHHmmss";
    static final String TASK_NAME_PRE = "task_";

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

    public UserDto getCurrentUser() {
        UserDto currentUser = UserContext.get();
        if (null == currentUser) {
            log.error(RespCodeEnum.UN_LOGIN.getMsg());
            throw new BusinessException(RespCodeEnum.UN_LOGIN);
        }
        return currentUser;
    }
}
