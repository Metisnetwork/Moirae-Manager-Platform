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

    public UserDto getCurrentUserOrNull() {
        return UserContext.get();
    }
}
