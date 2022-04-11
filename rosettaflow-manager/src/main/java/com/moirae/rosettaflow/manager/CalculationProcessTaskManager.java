package com.moirae.rosettaflow.manager;

import com.moirae.rosettaflow.mapper.domain.CalculationProcessTask;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 计算流程任务步骤表 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-04-11
 */
public interface CalculationProcessTaskManager extends IService<CalculationProcessTask> {

    List<CalculationProcessTask> getList(Long calculationProcessId);
}
