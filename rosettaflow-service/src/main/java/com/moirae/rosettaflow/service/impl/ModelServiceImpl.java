package com.moirae.rosettaflow.service.impl;

import com.moirae.rosettaflow.manager.ModelManager;
import com.moirae.rosettaflow.mapper.domain.Model;
import com.moirae.rosettaflow.mapper.domain.User;
import com.moirae.rosettaflow.service.ModelService;
import com.moirae.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ModelServiceImpl implements ModelService {

    @Resource
    ModelManager modelManager;

    @Override
    public List<Model> queryAvailableModel(Long algorithmId, String identityId) {
        User user = UserContext.getCurrentUser();
        return this.modelManager.queryAvailableModel(user.getAddress(), algorithmId, identityId);
    }

    @Override
    public Model queryById(String metaDataId) {
        return modelManager.getById(metaDataId);
    }

    @Override
    public List<Model> getLatestModel(Integer size) {
        return modelManager.getLatestModel(size);
    }
}
