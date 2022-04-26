package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.req.KeyWorkReq;
import com.moirae.rosettaflow.req.home.GetLatestModelListReq;
import com.moirae.rosettaflow.req.home.GetOrgComputingTopReq;
import com.moirae.rosettaflow.req.home.GetTaskTrendReq;
import com.moirae.rosettaflow.service.DataService;
import com.moirae.rosettaflow.service.StatisticsService;
import com.moirae.rosettaflow.service.dto.statistics.NavigationDto;
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
import java.util.List;

@Slf4j
@RestController
@Api(tags = "首页相关接口")
@RequestMapping(value = "home", produces = MediaType.APPLICATION_JSON_VALUE)
public class HomeController {
    @Resource
    private StatisticsService statisticsService;
    @Resource
    private DataService dataService;

    @GetMapping("queryNavigation")
    @ApiOperation(value = "查询导航", notes = "查询导航")
    public ResponseVo<NavigationVo> queryNavigation(@Valid KeyWorkReq req) {
        NavigationDto navigationDto = statisticsService.queryNavigation(req.getKeyword());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(navigationDto, NavigationVo.class));
    }

    @GetMapping("getLatestModelList")
    @ApiOperation(value = "获得最新的模型列表", notes = "获得最新的模型列表")
    public ResponseVo<List<LatestModelVo>> getLatestModel(@Valid GetLatestModelListReq req) {
        List<Model> modelList = dataService.listModelOfLatest(req.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(modelList, LatestModelVo.class));
    }

    @GetMapping("getGlobalStats")
    @ApiOperation(value = "获得全网统计数据", notes = "获得全网统计数据")
    public ResponseVo<StatsGlobalVo> globalStats() {
        StatsGlobal statsGlobal = statisticsService.globalStats();
        return ResponseVo.createSuccess(BeanUtil.copyProperties(statsGlobal, StatsGlobalVo.class));
    }

    @GetMapping("getTaskTrend")
    @ApiOperation(value = "获得15天隐私计算走势", notes = "获得15天隐私计算走势")
    public ResponseVo<List<TrendVo>> getTaskTrend(@Valid GetTaskTrendReq req) {
        List<StatsDay> statsDayList = statisticsService.getTaskTrend(req.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(statsDayList, TrendVo.class));
    }

    @GetMapping("getOrgPowerTop")
    @ApiOperation(value = "获得组织算力排行", notes = "获得组织算力排行")
    public ResponseVo<List<OrgPowerVo>> getOrgComputingTop(@Valid GetOrgComputingTopReq req) {
        List<StatsOrg> statsOrgList = statisticsService.getOrgComputingTop(req.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(statsOrgList, OrgPowerVo.class));
    }

    @GetMapping("getDataTokenUsedTop")
    @ApiOperation(value = "获得数据凭证使用量排行", notes = "获得数据凭证使用量排行")
    public ResponseVo<List<DataTokenUsedVo>> getDataTokenUsedTop(@Valid GetOrgComputingTopReq req) {
        List<StatsToken> statsTokenList = statisticsService.getDataTokenUsedTop(req.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(statsTokenList, DataTokenUsedVo.class));
    }
}
