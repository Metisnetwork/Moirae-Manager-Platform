package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.Model;
import com.moirae.rosettaflow.req.model.GetUserModelListReq;
import com.moirae.rosettaflow.service.IModelService;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.model.ModelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "模型管理关接口")
@RequestMapping(value = "model", produces = MediaType.APPLICATION_JSON_VALUE)
public class ModelController {
    @Resource
    private IModelService modelService;

    @GetMapping("getUserModelList")
    @ApiOperation(value = "查询当前用户的算法模型", notes = "查询当前用户的算法模型")
    public ResponseVo<List<ModelVo>> getUserModelList(@Valid GetUserModelListReq req) {
        // 获取语言类型
        String language = "";
        List<Model> list =  modelService.queryAvailableModel(req.getAlgorithmId(), req.getIdentityId(), language);
        return ResponseVo.createSuccess(BeanUtil.copyToList(list, ModelVo.class));
    }
}
