package com.platon.rosettaflow.mapper;

import com.platon.rosettaflow.mapper.domain.AlgorithmUser;

public interface AlgorithmUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AlgorithmUser record);

    int insertSelective(AlgorithmUser record);

    AlgorithmUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlgorithmUser record);

    int updateByPrimaryKey(AlgorithmUser record);
}