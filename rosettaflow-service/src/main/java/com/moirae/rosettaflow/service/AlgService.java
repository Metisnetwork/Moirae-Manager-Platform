package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.service.dto.alg.AlgTreeDto;

public interface AlgService{

    AlgTreeDto getAlgTree(boolean isNeedDetails);
}
