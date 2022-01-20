package com.moirae.rosettaflow.service;

import cn.hutool.core.date.DateUtil;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.service.utils.UserContext;
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
    static final String DATE_TIME_FMT = "yyyyMMddHHmmssSSS";
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
            log.error(ErrorMsg.UN_LOGIN.getMsg());
            throw new BusinessException(RespCodeEnum.UN_LOGIN, ErrorMsg.UN_LOGIN.getMsg());
        }
        return currentUser;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.format(new Date(), DATE_TIME_FMT));
    }
}
