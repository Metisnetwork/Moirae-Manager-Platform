package com.platon.rosettaflow.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.mapper.domain.Algorithm;

import java.util.List;

/**
 * @author houz
 */
public interface IAlgorithmService extends IService<Algorithm> {

    /**
     * 新增算法
     * @param algorithmDto
     */
    void saveAlgorithm(AlgorithmDto algorithmDto);

    /**
     * 查询算法列表
     * @param userId
     * @return
     */
    List<AlgorithmDto> queryAlgorithmList(Long userId);

    /**
     * 查询算法详情
     * @param userId
     * @return
     */
    AlgorithmDto queryAlgorithmDetails(Long userId);


}