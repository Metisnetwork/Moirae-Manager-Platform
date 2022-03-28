package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;

import java.util.List;

public interface AlgorithmVariableManager extends IService<AlgorithmVariable> {
    List<AlgorithmVariable> getByAlgorithmId(Long id);
}
