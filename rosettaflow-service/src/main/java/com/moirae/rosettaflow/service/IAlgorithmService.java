package com.moirae.rosettaflow.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.AlgorithmDto;
import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.WorkflowNode;

import java.util.List;
import java.util.Map;

/**
 * @author houz
 */
public interface IAlgorithmService extends IService<Algorithm> {

    /**
     * 查询算法列表
     * @param current 当前页数
     * @param size 每页数量
     * @param algorithmName 算法名称
     * @return AlgorithmDto
     */
    IPage<AlgorithmDto> queryAlgorithmList(Long current, Long size, String algorithmName);

    /**
     * 获取算法，通过算法id
     * @param id 算法id
     * @return Algorithm
     */
    Algorithm getAlgorithmById(Long id);

    /**
     * 查询算法详情
     * @param id 算法id
     * @return AlgorithmDto
     */
    AlgorithmDto queryAlgorithmDetails(Long id);

    /**
     * 查询算法树结构
     * @param language 国际化语言
     * @return list
     */
    List<Map<String, Object>> queryAlgorithmTreeList(String language);

    /**
     * 查询算法详情
     * @param algorithmId 算法步骤id
     * @return Algorithm
     */
    Algorithm queryAlgorithmStepDetails(Long algorithmId);

    void isValid(List<Long> algorithmId);

    Algorithm getAlgorithmByIdCode(String algorithmCode, int inputModel);
}
