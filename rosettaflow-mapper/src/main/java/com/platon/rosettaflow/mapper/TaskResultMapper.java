package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.TaskResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface TaskResultMapper extends BaseMapper<TaskResult> {

    int batchInsert(@Param("taskResultList") List<TaskResult> taskResultList);
}