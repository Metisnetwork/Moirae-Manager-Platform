package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.Model;
import com.moirae.rosettaflow.mapper.domain.StatsDay;

import java.util.List;

public interface ModelService {

    List<Model> queryAvailableModel(Long algorithmId, String identityId);

    Model queryById(String metaDataId);

    List<Model> getLatestModel(Integer size);
}
