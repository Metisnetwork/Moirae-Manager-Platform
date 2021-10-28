package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.AlgorithmCode;

/**
 * @author houz
 */
public interface IAlgorithmCodeService extends IService<AlgorithmCode> {

    /**
     * 新增算法代码
     *
     * @param algorithmCode 算法代码对象
     */
    void addAlgorithmCode(AlgorithmCode algorithmCode);

    /**
     * 根据算法id修改算法
     *
     * @param algorithmCode 算法代码对象
     */
    void updateAlgorithmCode(AlgorithmCode algorithmCode);

    /**
     * 根据算法id获取算法代码
     *
     * @param algorithmId 算法id
     * @return 算法代码
     */
    AlgorithmCode getByAlgorithmId(Long algorithmId);

    /**
     * 清空算法表
     */
    void truncate();
}