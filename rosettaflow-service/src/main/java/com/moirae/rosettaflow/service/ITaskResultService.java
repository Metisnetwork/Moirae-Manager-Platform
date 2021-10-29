package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.TaskResult;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/10/14
 * @description 任务结果服务接口
 */
public interface ITaskResultService extends IService<TaskResult> {

    /**
     * 查询任务结果
     *
     * @param taskId 任务id
     * @return 任务结果
     */
    List<TaskResult> queryTaskResultByTaskId(String taskId);

    /**
     * 批量保存任务执行结果
     *
     * @param taskResultList 任务结果列表
     */
    void batchInsert(List<TaskResult> taskResultList);
}
