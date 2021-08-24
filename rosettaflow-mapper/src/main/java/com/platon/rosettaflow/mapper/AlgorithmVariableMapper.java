package com.platon.rosettaflow.mapper;

import com.platon.rosettaflow.mapper.domain.AlgorithmVariable;

public interface AlgorithmVariableMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AlgorithmVariable record);

    int insertSelective(AlgorithmVariable record);

    AlgorithmVariable selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlgorithmVariable record);

    int updateByPrimaryKey(AlgorithmVariable record);
}