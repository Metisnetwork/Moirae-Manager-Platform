package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariable;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/31
 * @description 算法变量服务接口
 */
public interface IAlgorithmVariableService extends IService<AlgorithmVariable> {

    /**
     * 根据算法id获取算法因变量及自变量
     *
     * @param algorithmId 算法id
     * @return 算法变量
     */
    List<AlgorithmVariable> getByAlgorithmId(Long algorithmId);

    /**
     * 复制保存算法变量
     *
     * @param oldAlgorithmId 源算法id
     * @param newAlgorithmId 目的算法id
     */
    void saveAlgorithmVariable(Long oldAlgorithmId, Long newAlgorithmId);

    /**
     * 清空算法变量表
     */
    void truncate();

}
