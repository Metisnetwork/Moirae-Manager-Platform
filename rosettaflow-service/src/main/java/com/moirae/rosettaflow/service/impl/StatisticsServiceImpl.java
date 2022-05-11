package com.moirae.rosettaflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        List<StatsDay> statsDayList = statsDayManager.getNewestList(StatsDayKeyEnum.TASK_COUNT, size);
        int pending = size - statsDayList.size();
        if(pending > 0){
            Date last;
            if(statsDayList.size() > 0){
                last = statsDayList.get(statsDayList.size() - 1).getStatsTime();
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String s = sdf.format(new Date());
                try {
                    last =  sdf.parse(s);
                } catch (ParseException e) {
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.EXCEPTION.getMsg());
                }
            }

            for (int i = 0; i < pending; i++) {
                StatsDay statsDay = new StatsDay();
                statsDay.setStatsTime(last);
                statsDay.setStatsKey(StatsDayKeyEnum.TASK_COUNT);
                statsDay.setStatsValue(0L);
                statsDayList.add(statsDay);
                last = DateUtils.addDays(last, -1);
            }
        }
        return statsDayList;
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
            newly = newlyList.get(0).getStatsTime();
        }
        List<Task> taskList = taskService.statisticsOfDay(newly);
        Date finalNewly = newly;
        taskList.forEach(item->{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String s = sdf.format(item.getStatsTime());
            try {
                Date date =  sdf.parse(s);
                StatsDay statsDay = new StatsDay();
                statsDay.setStatsTime(date);
                statsDay.setStatsKey(keyEnum);
                statsDay.setStatsValue(item.getTaskCount().longValue());
                if(finalNewly != null && finalNewly.compareTo(date) == 0){
                    LambdaUpdateWrapper<StatsDay> updateWrapper = Wrappers.lambdaUpdate();
                    updateWrapper.set(StatsDay::getStatsValue, statsDay.getStatsValue());
                    updateWrapper.eq(StatsDay::getStatsTime, date);
                    updateWrapper.eq(StatsDay::getStatsKey, statsDay.getStatsKey());
                    statsDayManager.update(updateWrapper);
                }else{
                    statsDayManager.save(statsDay);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


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
