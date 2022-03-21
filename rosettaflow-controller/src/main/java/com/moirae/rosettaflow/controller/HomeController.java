package com.moirae.rosettaflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.moirae.rosettaflow.req.KeyWorkReq;
import com.moirae.rosettaflow.req.home.GetLatestModelListReq;
import com.moirae.rosettaflow.service.StatisticsService;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.home.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "首页相关接口")
@RequestMapping(value = "home", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiSupport(order = 1)
public class HomeController {
    @Resource
    private StatisticsService statisticsService;

    @GetMapping("queryNavigation")
    @ApiOperation(value = "查询导航", notes = "查询导航")
    public ResponseVo<NavigationVo> queryNavigation(@Valid KeyWorkReq req) {
        NavigationVo resp = new NavigationVo();
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getLatestModelList")
    @ApiOperation(value = "获得最新的模型列表", notes = "获得最新的模型列表")
    public ResponseVo<List<LatestModelVo>> getLatestModel(@Valid GetLatestModelListReq req) {
        List<LatestModelVo> resp = new ArrayList<>();
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getGlobalStats")
    @ApiOperation(value = "获得全网统计数据", notes = "获得全网统计数据")
    public ResponseVo<GlobalStatsVo> globalStats() {
        GlobalStatsVo resp = new GlobalStatsVo();
        return ResponseVo.createSuccess(resp);
    }


    @GetMapping("getTaskTrend")
    @ApiOperation(value = "获得15天隐私计算走势", notes = "获得15天隐私计算走势")
    public ResponseVo<List<TrendVo>> getTaskTrend() {
        List<TrendVo> resp = new ArrayList<>();
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getOrgPowerTop")
    @ApiOperation(value = "获得组织算力排行", notes = "获得组织算力排行")
    public ResponseVo<List<OrgPowerVo>> getOrgComputingTop() {
        List<OrgPowerVo> resp = new ArrayList<>();
        return ResponseVo.createSuccess(resp);
    }

    @GetMapping("getDataTokenUsedTop")
    @ApiOperation(value = "获得数据凭证使用量排行", notes = "获得数据凭证使用量排行")
    public ResponseVo<List<DataTokenUsedVo>> getDataTokenUsedTop() {
        List<DataTokenUsedVo> resp = new ArrayList<>();
        return ResponseVo.createSuccess(resp);
    }
}
