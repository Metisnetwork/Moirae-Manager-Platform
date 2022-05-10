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
import com.moirae.rosettaflow.service.dto.statistics.NavigationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Resource
    private StatsGlobalManager statsGlobalManager;
    @Resource
    private StatsDayManager statsDayManager;
    @Resource
    private StatsOrgManager statsOrgManager;
    @Resource
    private StatsTokenManager statsDataManager;
    @Resource
    private TaskService taskService;
    @Resource
    private OrgService orgService;

    @Override
    public NavigationDto queryNavigation(String keyword) {
        NavigationDto result = new NavigationDto();
        Task task = taskService.getTaskById(keyword);
        if(task != null){
            result.setType(NavigationTypeEnum.TASK);
            result.setId(task.getId());
            return result;
        }

        Org org = orgService.getOrgById(keyword);
        if(org != null){
            result.setType(NavigationTypeEnum.TASK);
            result.setId(org.getIdentityId());
            return result;
        }

        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.BIZ_QUERY_NOT_EXIST.getMsg());
    }

    @Override
    public List<StatsDay> getTaskTrend(Integer size) {
        return statsDayManager.getNewestList(StatsDayKeyEnum.TASK_COUNT, size);
    }

    @Override
    public List<StatsOrg> getOrgComputingTop(Integer size) {
        List<StatsOrg> statsOrgList = statsOrgManager.listByComputingPowerRatioDesc(size);
        Map<String, Org> identityId2OrgMap = orgService.getIdentityId2OrgMap();
        statsOrgList.forEach(item -> {
            item.setNodeName(identityId2OrgMap.get(item.getIdentityId()).getNodeName());
        });
        return statsOrgList;
    }

    @Override
    public List<StatsToken> getDataTokenUsedTop(Integer size) {
        List<StatsToken> statsTokenList = statsDataManager.getDataTokenUsedTop(size);
        return statsTokenList;
    }

    @Override
    public boolean updateStatsGlobal(StatsGlobal global) {
        return statsGlobalManager.updateById(global);
    }

    @Override
    public boolean statisticsOfStatsDay(StatsDayKeyEnum keyEnum) {
        Date newly = null;
        List<StatsDay> newlyList = statsDayManager.getNewestList(keyEnum, 2);
        if(newlyList.size() == 2){
            newly = newlyList.get(1).getStatsTime();
        }
        List<Task> taskList = taskService.statisticsOfDay(newly);
        taskList.forEach(item->{
            StatsDay statsDay = new StatsDay();
            statsDay.setStatsTime(item.getStatsTime());
            statsDay.setStatsKey(keyEnum);
            statsDay.setStatsValue(item.getTaskCount().longValue());
            statsDayManager.saveOrUpdate(statsDay);
        });
        return true;
    }

    @Override
    public StatsGlobal globalStats() {
        StatsGlobal statsGlobal = statsGlobalManager.getById(1);
        if(statsGlobal == null){
            statsGlobal = new StatsGlobal();
            statsGlobal.setId(1);
            statsGlobal.setTotalMemory(0);
            statsGlobal.setTotalBandwidth(0);
            statsGlobal.setTotalCore(0);
            statsGlobal.setDataSize(0);
            statsGlobal.setDataTokenUsed(0);
            statsGlobal.setDataTokenCount(0);
            statsGlobal.setAddressCountOfActive(0);
            statsGlobal.setAddressCountOfTask(0);
            statsGlobal.setTaskCount(0);
            statsGlobalManager.save(statsGlobal);
        }
        return statsGlobal;
    }
}
