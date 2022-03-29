package com.moirae.rosettaflow.manager;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Model;

import java.util.List;

public interface ModelManager extends IService<Model> {

    List<Model> queryAvailableModel(String address, Long algorithmId, String identityId);

    List<Model> getLatestModel(Integer size);
}
