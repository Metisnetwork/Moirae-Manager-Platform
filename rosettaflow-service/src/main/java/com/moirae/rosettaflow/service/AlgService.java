package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;

public interface AlgService{

    /**
     * 查询算法树
     *
     * @return
     */
    AlgorithmClassify queryAlgTreeList();
}
