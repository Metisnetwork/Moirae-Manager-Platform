package com.moirae.rosettaflow.controller;

import com.moirae.rosettaflow.service.IAlgorithmService;
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
import javax.servlet.http.HttpServletRequest;

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
public class AlgController {

    @Resource
    private IAlgorithmService algorithmService;

    @GetMapping("queryAlgorithmTreeList")
    @ApiOperation(value = "查询算法树列表", notes = "查询算法树列表")
    public ResponseVo<AlgTreeVo> queryAlgorithmTreeList(HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");
        AlgTreeVo algTreeListVo = new AlgTreeVo();
        return ResponseVo.createSuccess(algTreeListVo);
    }

}
