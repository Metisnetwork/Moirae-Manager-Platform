package com.moirae.rosettaflow.controller;

import cn.hutool.core.bean.BeanUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.moirae.rosettaflow.mapper.domain.GlobalStats;
import com.moirae.rosettaflow.mapper.domain.StatisticsDataTrend;
import com.moirae.rosettaflow.service.StatisticsService;
import com.moirae.rosettaflow.vo.ResponseVo;
import com.moirae.rosettaflow.vo.home.DataTrendVo;
import com.moirae.rosettaflow.vo.home.GlobalStatsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "首页相关接口")
@RequestMapping(value = "home", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiSupport(order = 1)
public class HomeController {
    @Resource
    private StatisticsService statisticsService;

    @GetMapping("globalStats")
    @ApiOperation(value = "查询全网资源概况", notes = "查询全网资源概况")
    @ApiOperationSupport(order = 2)
    public ResponseVo<GlobalStatsVo> globalStats() {
        GlobalStats globalStats = statisticsService.globalStats();
        return ResponseVo.createSuccess(BeanUtil.toBean(globalStats, GlobalStatsVo.class));
    }

    @GetMapping("dataTrend")
    @ApiOperation(value = "查询全网数据的月走势", notes = "查询全网数据的月走势")
    @ApiOperationSupport(order = 1)
    public ResponseVo<List<DataTrendVo>> dataTrend() {
        List<StatisticsDataTrend> statisticsDataTrendList = statisticsService.listGlobalDataFileStatsTrendMonthly();
        return ResponseVo.createSuccess(BeanUtil.copyToList(statisticsDataTrendList, DataTrendVo.class));
    }
}
