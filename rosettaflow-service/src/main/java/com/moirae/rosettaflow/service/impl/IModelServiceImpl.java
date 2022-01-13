package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.mapper.ModelMapper;
import com.moirae.rosettaflow.mapper.domain.Model;
import com.moirae.rosettaflow.mapper.domain.User;
import com.moirae.rosettaflow.service.CommonService;
import com.moirae.rosettaflow.service.IModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class IModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService {

    @Resource
    CommonService commonService;

    @Override
    public List<Model> queryAvailableModel(Long algorithmId, String language) {
        User user = commonService.getCurrentUser();
        return this.baseMapper.queryAvailableModel(user.getAddress(), algorithmId, language);
    }
}
