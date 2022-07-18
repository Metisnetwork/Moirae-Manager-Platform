package com.datum.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.req.KeyWorkReq;
import com.datum.platform.req.home.GetLatestTaskListReq;
import com.datum.platform.req.home.GetOrgComputingTopReq;
import com.datum.platform.req.home.GetTaskTrendReq;
import com.datum.platform.service.StatisticsService;
import com.datum.platform.service.TaskService;
import com.datum.platform.service.dto.statistics.NavigationDto;
import com.datum.platform.vo.ResponseVo;
import com.datum.platform.vo.home.*;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
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
@ApiSupport(order = 200)
@RequestMapping(value = "home", produces = MediaType.APPLICATION_JSON_VALUE)
public class HomeController {
    @Resource
    private StatisticsService statisticsService;
    @Resource
    private TaskService taskService;

    @GetMapping("queryNavigation")
    @ApiOperation(value = "查询导航", notes = "查询导航")
    public ResponseVo<NavigationVo> queryNavigation(@Valid KeyWorkReq req) {
        NavigationDto navigationDto = statisticsService.queryNavigation(req.getKeyword());
        return ResponseVo.createSuccess(BeanUtil.copyProperties(navigationDto, NavigationVo.class));
    }

    @GetMapping("getLatestTaskList")
    @ApiOperation(value = "获得最新的任务列表", notes = "获得最新的任务列表")
    public ResponseVo<List<LatestTaskVo>> getLatestTaskList(@Valid GetLatestTaskListReq req) {
        List<Task> modelList = taskService.listTaskOfLatest(req.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(modelList, LatestTaskVo.class));
    }

    @GetMapping("getGlobalStats")
    @ApiOperation(value = "获得全网统计数据", notes = "获得全网统计数据")
    public ResponseVo<StatsGlobalVo> globalStats() {
        StatsGlobal statsGlobal = statisticsService.globalStats();
        return ResponseVo.createSuccess(BeanUtil.copyProperties(statsGlobal, StatsGlobalVo.class));
    }

    @GetMapping("getTaskTrend")
    @ApiOperation(value = "获得15天计算走势", notes = "获得15天计算走势")
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

    @GetMapping("getDataUsedTop")
    @ApiOperation(value = "获得数据使用排行", notes = "获得数据使用排行")
    public ResponseVo<List<DataUsedVo>> getDataTokenUsedTop(@Valid GetOrgComputingTopReq req) {
        List<StatsMetaData> statsTokenList = statisticsService.getMetaDataUsedTop(req.getSize());
        return ResponseVo.createSuccess(BeanUtil.copyToList(statsTokenList, DataUsedVo.class));
    }
}
