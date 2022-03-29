package com.moirae.rosettaflow.service.impl;

import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.NavigationTypeEnum;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.manager.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.mapper.enums.StatsDayKeyEnum;
import com.moirae.rosettaflow.service.OrgService;
import com.moirae.rosettaflow.service.StatisticsService;
import com.moirae.rosettaflow.service.TaskService;
import com.moirae.rosettaflow.service.dto.NavigationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private StatisticsManager statisticsManager;
    @Resource
    private StatsGlobalManager statsGlobalManager;
    @Resource
    private StatsDayManager statsDayManager;
    @Resource
    private StatsOrgManager statsOrgManager;
    @Resource
    private StatsDataManager statsDataManager;
    @Resource
    private TaskService taskService;
    @Resource
    private OrgService orgService;

    @Override
    public NavigationDto queryNavigation(String keyword) {
        NavigationDto result = new NavigationDto();
        Task task = taskService.getTask(keyword);
        if(task != null){
            result.setType(NavigationTypeEnum.TASK);
            result.setId(task.getId());
            return result;
        }

        Org org = orgService.findOrgById(keyword);
        if(org != null){
            result.setType(NavigationTypeEnum.TASK);
            result.setId(org.getIdentityId());
            return result;
        }

        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.BIZ_QUERY_NOT_EXIST.getMsg());
    }

    @Override
    public List<StatisticsDataTrend> listGlobalDataFileStatsTrendMonthly() {
        return statisticsManager.listGlobalDataFileStatsTrendMonthly();
    }

    @Override
    public List<StatsDay> getTaskTrend(Integer size) {
        return statsDayManager.getNewestList(StatsDayKeyEnum.TASK_COUNT, size);
    }

    @Override
    public List<StatsOrg> getOrgComputingTop(Integer size) {
        StatsGlobal statsGlobal = globalStats();
        List<StatsOrg> statsOrgList = statsOrgManager.list();
        return statsOrgList;
    }

    @Override
    public List<StatsData> getDataTokenUsedTop(Integer size) {
        return statsDataManager.getDataTokenUsedTop(size);
    }

    @Override
    public StatsGlobal globalStats() {
        return statsGlobalManager.getById(1);
    }
}
