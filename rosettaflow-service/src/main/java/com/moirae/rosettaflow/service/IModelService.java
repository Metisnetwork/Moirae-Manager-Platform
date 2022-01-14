package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.mapper.domain.Model;
import com.moirae.rosettaflow.mapper.domain.Workflow;

import java.util.List;

public interface IModelService extends IService<Model> {

    List<Model> queryAvailableModel(Long algorithmId, String identityId,String language);

    Model queryById(Long id);
}
