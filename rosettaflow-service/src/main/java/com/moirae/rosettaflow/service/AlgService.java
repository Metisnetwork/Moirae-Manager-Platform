package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;
import com.moirae.rosettaflow.mapper.enums.CalculationProcessTaskAlgorithmSelectEnum;

import java.util.List;
import java.util.Set;

public interface AlgService{
    /**
     * 查询算法分类树结构
     *
     * @param isNeedDetails  是否返回算法详情（代码+变量）
     * @return
     */
    AlgorithmClassify getAlgorithmClassifyTree(boolean isNeedDetails);


    /**
     * 查询算法信息
     *
     * @param algorithmId    算法id
     * @param isNeedDetails  是否返回算法详情（代码+变量）
     * @return
     */
    Algorithm getAlgorithm(Long algorithmId, boolean isNeedDetails);

    /**
     * 查询算法相关的预测算法
     *
     * @param algorithmId   算法id
     * @return
     */
    Algorithm getAlgorithmOfRelativelyPrediction(Long algorithmId);

    /**
     * 根据选择类型查询查询算法
     *
     * @param algorithmSelect  选择方式
     * @param rootTree         算法分类树
     * @param selectedTree     算法分类树用户选择的子树
     * @return
     */
    Algorithm findAlgorithm(CalculationProcessTaskAlgorithmSelectEnum algorithmSelect, AlgorithmClassify rootTree, AlgorithmClassify selectedTree);
}
