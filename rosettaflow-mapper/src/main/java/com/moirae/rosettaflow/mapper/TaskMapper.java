package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moirae.rosettaflow.dto.OrganizationDto;
import com.moirae.rosettaflow.dto.TaskDto;
import com.moirae.rosettaflow.mapper.domain.Task;

/**
 * t_task_event
 *
 * @author admin
 */
public interface TaskMapper extends BaseMapper<Task> {
    IPage<TaskDto> listTaskByIdentityId(String identityId, Page<OrganizationDto> page);
}
