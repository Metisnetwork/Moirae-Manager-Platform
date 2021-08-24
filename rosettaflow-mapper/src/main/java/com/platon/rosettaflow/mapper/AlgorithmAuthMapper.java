package com.platon.rosettaflow.mapper;

import com.platon.rosettaflow.mapper.domain.AlgorithmAuth;

public interface AlgorithmAuthMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AlgorithmAuth record);

    int insertSelective(AlgorithmAuth record);

    AlgorithmAuth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlgorithmAuth record);

    int updateByPrimaryKey(AlgorithmAuth record);
}