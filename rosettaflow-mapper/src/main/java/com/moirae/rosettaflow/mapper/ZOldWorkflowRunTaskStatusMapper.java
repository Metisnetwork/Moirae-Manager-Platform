package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.ZOldWorkflowRunTaskStatus;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface ZOldWorkflowRunTaskStatusMapper extends BaseMapper<ZOldWorkflowRunTaskStatus> {
    List<ZOldWorkflowRunTaskStatus> queryUnConfirmedWorkflowRunTaskStatus();
}
