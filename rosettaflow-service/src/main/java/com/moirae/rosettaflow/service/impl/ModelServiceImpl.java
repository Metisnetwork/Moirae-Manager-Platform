package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.mapper.ModelMapper;
import com.moirae.rosettaflow.mapper.domain.Model;
import com.moirae.rosettaflow.mapper.domain.User;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.IModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService {

    @Resource
    CommonService commonService;

    @Override
    public List<Model> queryAvailableModel(Long algorithmId, String identityId, String language) {
        User user = commonService.getCurrentUser();
        return this.baseMapper.queryAvailableModel(user.getAddress(), algorithmId, identityId, language);
    }

    @Override
    public Model queryById(Long id) {
        LambdaQueryWrapper<Model> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Model::getId, id);
        queryWrapper.eq(Model::getStatus, StatusEnum.VALID.getValue());
        return getOne(queryWrapper);
    }
}
