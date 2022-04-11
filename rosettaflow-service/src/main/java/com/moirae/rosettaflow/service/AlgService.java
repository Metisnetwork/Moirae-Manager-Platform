package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.Algorithm;
import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;
import com.moirae.rosettaflow.service.dto.alg.AlgTreeDto;

public interface AlgService{

    AlgTreeDto getAlgTreeDto(boolean isNeedDetails);

    AlgorithmClassify getAlgTree(boolean isNeedDetails, Long rootId);

    Algorithm getAlg(Long algorithmId);
}
