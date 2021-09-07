package com.platon.rosettaflow.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.mapper.domain.Algorithm;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;

import java.util.List;

/**
 * @author houz
 */
public interface IAlgorithmService extends IService<Algorithm> {

    /**
     * 新增算法
     * @param algorithmDto 算法请求参数
     */
    void addAlgorithm(AlgorithmDto algorithmDto);

    /**
     * 修改算法
     * @param algorithmDto 算法请求参数
     */
    void updateAlgorithm(AlgorithmDto algorithmDto);

    /**
     * 查询算法列表
     * @param algorithmName 算法名称
     * @return AlgorithmDto
     */
    List<AlgorithmDto> queryAlgorithmList(String algorithmName);

    /**
     * 查询算法详情
     * @param id 算法id
     * @return AlgorithmDto
     */
    AlgorithmDto queryAlgorithmDetails(Long id);

    /**
     * 复制保存算法
     * @param oldNode 源算法信息
     * @return
     */
    Long copySaveAlgorithm(WorkflowNode oldNode);

    /**
     * 清空算法表
     */
    void truncate();
}