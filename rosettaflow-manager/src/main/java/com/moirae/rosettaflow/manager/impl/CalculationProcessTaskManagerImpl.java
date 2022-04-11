package com.moirae.rosettaflow.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.manager.CalculationProcessTaskManager;
import com.moirae.rosettaflow.mapper.CalculationProcessTaskMapper;
import com.moirae.rosettaflow.mapper.domain.CalculationProcessTask;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 计算流程任务步骤表 服务实现类
 * </p>
 *
 * @author chendai
 * @since 2022-04-11
 */
@Service
public class CalculationProcessTaskManagerImpl extends ServiceImpl<CalculationProcessTaskMapper, CalculationProcessTask> implements CalculationProcessTaskManager {

    @Override
    public List<CalculationProcessTask> getList(Long calculationProcessId) {
        LambdaQueryWrapper<CalculationProcessTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CalculationProcessTask::getCalculationProcessId, calculationProcessId);
        return list(wrapper);
    }
}
