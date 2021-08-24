package com.platon.rosettaflow.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.mapper.domain.Algorithm;
import com.platon.rosettaflow.mapper.domain.AlgorithmCode;

import java.util.List;

/**
 * @author houz
 */
public interface IAlgorithmCodeService extends IService<AlgorithmCode> {

    /**
     * 新增算法代码
     * @param algorithmCode
     */
    void addAlgorithmCode(AlgorithmCode algorithmCode);

    /**
     * 根据算法id修改算法
     * @param algorithmCode
     */
    void updateAlgorithmCode(AlgorithmCode algorithmCode);


}