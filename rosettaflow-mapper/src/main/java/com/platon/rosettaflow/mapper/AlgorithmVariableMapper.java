package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.mapper.domain.AlgorithmVariable;

/**
 * t_algorithm_variable
 *
 * @author admin
 */
public interface AlgorithmVariableMapper extends BaseMapper<AlgorithmVariable> {
    void truncate();
}