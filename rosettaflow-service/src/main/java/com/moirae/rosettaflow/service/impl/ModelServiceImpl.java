package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.manager.ModelManager;
import com.moirae.rosettaflow.mapper.ModelMapper;
import com.moirae.rosettaflow.mapper.domain.Model;
import com.moirae.rosettaflow.mapper.domain.StatsDay;
import com.moirae.rosettaflow.mapper.domain.User;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ModelServiceImpl implements ModelService {

    @Resource
    CommonService commonService;
    @Resource
    ModelManager modelManager;

    @Override
    public List<Model> queryAvailableModel(Long algorithmId, String identityId) {
        User user = commonService.getCurrentUser();
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
