package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;
import com.moirae.rosettaflow.service.AlgService;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.alg.AlgTreeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@Api(tags = "算法相关接口")
@RequestMapping(value = "alg", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgController {

    @Resource
    private AlgService algService;

    @GetMapping("getAlgTreeList")
    @ApiOperation(value = "查询算法树列表", notes = "查询算法树列表")
    public ResponseVo<AlgTreeVo> queryAlgTreeList() {
        AlgorithmClassify algorithmClassify = algService.queryAlgTreeList();
        return ResponseVo.createSuccess(BeanUtil.copyProperties(algorithmClassify, AlgTreeVo.class));
    }
}
