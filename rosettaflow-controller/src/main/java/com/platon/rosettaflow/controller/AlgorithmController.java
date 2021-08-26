package com.platon.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.req.algorithm.AlgorithmListReq;
import com.platon.rosettaflow.req.algorithm.AlgorithmReq;
import com.platon.rosettaflow.service.IAlgorithmService;
import com.platon.rosettaflow.utils.ConvertUtils;
import com.platon.rosettaflow.vo.ResponseVo;
import com.platon.rosettaflow.vo.algorithm.AlgDetailsVo;
import com.platon.rosettaflow.vo.algorithm.AlgorithmListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
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

    @PostMapping("saveAlgorithm")
    @ApiOperation(value = "保存算法", notes = "保存算法")
    public ResponseVo<AlgDetailsVo> saveAlgorithm(@RequestBody @Valid AlgorithmReq algorithmReq) {
        try {
            AlgorithmDto algorithmDto = BeanUtil.copyProperties(algorithmReq, AlgorithmDto.class);
            algorithmService.saveAlgorithm(algorithmDto);
            return ResponseVo.createSuccess();
        } catch (Exception e) {
            log.error("algorithm--queryAlgorithmDetails--查询算法详情失败, 错误信息:{}", e);
            return ResponseVo.createFail();
        }
    }

    @PostMapping("queryAlgorithmList")
    @ApiOperation(value = "查询算法列表", notes = "查询算法列表")
    public ResponseVo<List<AlgorithmListVo>> queryAlgorithmList(@RequestBody @Valid AlgorithmListReq algListReq) {
        try {
            List listVo = algorithmService.queryAlgorithmList(algListReq.getUserId());
            List<AlgorithmListVo> algVoList = ConvertUtils.convertSerialToList(listVo, AlgorithmListVo.class);
            return ResponseVo.createSuccess(algVoList);
        } catch (Exception e) {
            log.error("algorithm--queryAlgorithmList--查询算法列表失败, 错误信息:{}", e);
            return ResponseVo.createFail();
        }

    }

    @PostMapping("queryAlgorithmDetails")
    @ApiOperation(value = "查询算法详情", notes = "查询算法详情")
    public ResponseVo<AlgDetailsVo> queryAlgorithmDetails(@RequestBody @Valid AlgorithmListReq algListReq) {
        try {
            AlgorithmDto algorithmDto = algorithmService.queryAlgorithmDetails(algListReq.getUserId());
            AlgDetailsVo algDetailsVo = BeanUtil.copyProperties(algorithmDto, AlgDetailsVo.class);
            return ResponseVo.createSuccess(algDetailsVo);
        } catch (Exception e) {
            log.error("algorithm--queryAlgorithmDetails--查询算法详情失败, 错误信息:{}", e);
            return ResponseVo.createFail();
        }
    }


}
