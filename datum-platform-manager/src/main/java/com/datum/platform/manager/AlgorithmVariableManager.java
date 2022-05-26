package com.datum.platform.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.datum.platform.mapper.domain.AlgorithmVariable;

import java.util.List;

public interface AlgorithmVariableManager extends IService<AlgorithmVariable> {
    List<AlgorithmVariable> getByAlgorithmId(Long id);
}
