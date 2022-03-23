package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.Model;

import java.util.List;

public interface IModelService extends IService<Model> {

    List<Model> queryAvailableModel(Long algorithmId, String identityId,String language);

    Model queryById(Long id);
}
