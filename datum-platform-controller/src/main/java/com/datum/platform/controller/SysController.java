package com.datum.platform.controller;

import com.datum.platform.service.SysService;
import com.datum.platform.service.dto.sys.PlatONPropertiesDto;
import com.datum.platform.vo.ResponseVo;
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
@Api(tags = "系统相关接口")
@RequestMapping(value = "sys", produces = MediaType.APPLICATION_JSON_VALUE)
public class SysController {

    @Resource
    private SysService service;

    @GetMapping("getPlatONChainConfig")
    @ApiOperation(value = "获得链配置", notes = "查询算法树")
    public ResponseVo<PlatONPropertiesDto> getPlatONChainConfig() {
        return ResponseVo.createSuccess(service.getPlatONProperties());
    }
}
