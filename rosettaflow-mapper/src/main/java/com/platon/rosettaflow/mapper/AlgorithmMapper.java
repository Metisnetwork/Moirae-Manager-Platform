package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.mapper.domain.Algorithm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author houz
 */
public interface AlgorithmMapper extends BaseMapper<Algorithm> {

    /**
     * 查询算法列表
     * @param userId
     * @param algorithmName
     * @return
     */
    List<AlgorithmDto> queryAlgorithmList(@Param(value = "userId")Long userId, @Param(value = "algorithmName")String algorithmName);

    /**
     * 查询算法详情
     * @param algorithmId 算法id
     * @return
     */
    AlgorithmDto queryAlgorithmDetails(Long algorithmId);


}