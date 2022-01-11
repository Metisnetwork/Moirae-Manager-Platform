package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.AlgorithmDto;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author houz
 */
public interface AlgorithmMapper extends BaseMapper<Algorithm> {

    /**
     * 查询算法列表
     *
     * @param page 算法分页
     * @param algorithmName 算法名称
     * @return 算法列表
     */
    IPage<AlgorithmDto> queryAlgorithmList(@Param("page") IPage<AlgorithmDto> page, @Param(value = "algorithmName") String algorithmName);

    /**
     * 查询算法树列表
     *
     * @return 算法列表
     */
    List<AlgorithmDto> queryAlgorithmTreeList();

    /**
     * 查询算法详情
     *
     * @param algorithmId 算法id
     * @return 算法详情
     */
    AlgorithmDto queryAlgorithmDetails(Long algorithmId);
}
