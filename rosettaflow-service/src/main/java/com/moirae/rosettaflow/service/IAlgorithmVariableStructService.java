package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.AlgorithmVariableStruct;
/**
 * @author hudenian
 * @date 2021/10/13
 * @description 算法变量服务接口
 */
public interface IAlgorithmVariableStructService extends IService<AlgorithmVariableStruct> {
    /**
     * 根据算法id获取算法变量结构模板
     * @param algorithmId 算法id
     * @return  算法变量结构模板
     */
    AlgorithmVariableStruct getByAlgorithmId(Long algorithmId);
}
