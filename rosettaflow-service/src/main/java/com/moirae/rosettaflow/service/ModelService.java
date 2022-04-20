package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.Model;

import java.util.List;

public interface ModelService {

    List<Model> getLatestModel(Integer size);
}
