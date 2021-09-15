package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.common.enums.AlgorithmTypeEnum;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.req.algorithm.AlgDetailsReq;
import com.platon.rosettaflow.req.algorithm.AlgListReq;
import com.platon.rosettaflow.req.algorithm.AlgorithmReq;
import com.platon.rosettaflow.service.IAlgorithmService;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.algorithm.AlgDetailsVo;
import com.platon.rosettaflow.vo.algorithm.AlgorithmListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 算法相关接口
 *
 * @author admin
 * @date 2021/8/17
 */
@Slf4j
@RestController
@Api(tags = "算法相关接口")
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmController {

    @Resource
    private IAlgorithmService algorithmService;

    @PostMapping("addAlgorithm")
    @ApiOperation(value = "新增算法", notes = "新增算法")
    public ResponseVo<?> addAlgorithm(@RequestBody @Valid AlgorithmReq algorithmReq) {
        algorithmService.addAlgorithm(BeanUtil.copyProperties(algorithmReq, AlgorithmDto.class));
        return ResponseVo.createSuccess();

    }

    @PostMapping("updateAlgorithm")
    @ApiOperation(value = "修改算法", notes = "修改算法")
    public ResponseVo<?> updateAlgorithm(@RequestBody @Valid AlgorithmReq algorithmReq) {
        algorithmService.updateAlgorithm(BeanUtil.copyProperties(algorithmReq, AlgorithmDto.class));
        return ResponseVo.createSuccess();
    }

    @GetMapping("list")
    @ApiOperation(value = "查询算法列表", notes = "查询算法列表")
    public ResponseVo<List<AlgorithmListVo>> list(@Valid AlgListReq algListReq) {
        List<AlgorithmDto> listDto = algorithmService.queryAlgorithmList(algListReq.getAlgorithmName());
        return ResponseVo.createSuccess(BeanUtil.copyToList(listDto, AlgorithmListVo.class));
    }

    @GetMapping("details/{id}")
    @ApiOperation(value = "查询算法详情", notes = "查询算法详情")
    public ResponseVo<AlgDetailsVo> detail(@ApiParam(value = "算法表ID", required = true) @PathVariable Long id) {
        AlgorithmDto algorithmDto = algorithmService.queryAlgorithmDetails(id);
        AlgDetailsVo algDetailsVo = BeanUtil.copyProperties(algorithmDto, AlgDetailsVo.class);
        algDetailsVo.setAlgorithmTypeDesc(AlgorithmTypeEnum.getDesc(algorithmDto.getAlgorithmType()));
        return ResponseVo.createSuccess(algDetailsVo);
    }

    @GetMapping("queryAlgorithmTreeList")
    @ApiOperation(value = "查询算法树列表", notes = "查询算法树列表")
    public ResponseVo<List<Map<String, Object>>> queryAlgorithmTreeList() {
        List<Map<String, Object>> listVo = algorithmService.queryAlgorithmTreeList();
        return ResponseVo.createSuccess(listVo);

    }

}
