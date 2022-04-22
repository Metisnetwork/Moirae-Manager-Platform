package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;
import com.moirae.rosettaflow.mapper.enums.CalculationProcessTaskAlgorithmSelectEnum;
import com.moirae.rosettaflow.service.dto.alg.AlgTreeDto;

import java.util.List;
import java.util.Set;

public interface AlgService{

    AlgTreeDto getAlgTreeDto(boolean isNeedDetails);

    AlgorithmClassify getAlgTree(boolean isNeedDetails);

    AlgorithmClassify getAlgTree(boolean isNeedDetails, Long rootId);

    Algorithm getAlg(Long algorithmId,boolean isNeedDetails);

    Algorithm getAlgOfRelativelyPrediction(Long algorithmId);

    List<AlgorithmClassify> listAlglassifyByIds(Set<Long> collect);

    Algorithm findAlg(CalculationProcessTaskAlgorithmSelectEnum algorithmSelect, AlgorithmClassify rootTree, AlgorithmClassify selectedTree);
}
