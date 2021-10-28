package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.TaskResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 */
public interface TaskResultMapper extends BaseMapper<TaskResult> {
    /**
     * 批量保存任务执行结果
     *
     * @param taskResultList 任务结果列表
     * @return 保存记录数
     */
    int batchInsert(@Param("taskResultList") List<TaskResult> taskResultList);
}