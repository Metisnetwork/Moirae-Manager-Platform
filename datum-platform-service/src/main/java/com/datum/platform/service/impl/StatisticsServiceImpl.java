package com.datum.platform.service.impl;

import com.datum.platform.common.enums.ErrorMsg;
import com.datum.platform.common.enums.NavigationTypeEnum;
import com.datum.platform.common.enums.RespCodeEnum;
import com.datum.platform.common.exception.BusinessException;
import com.datum.platform.manager.StatsGlobalManager;
import com.datum.platform.manager.StatsOrgManager;
import com.datum.platform.manager.StatsMetaDataManager;
import com.datum.platform.mapper.domain.*;
import com.datum.platform.service.DataService;
import com.datum.platform.service.OrgService;
import com.datum.platform.service.StatisticsService;
import com.datum.platform.service.TaskService;
import com.datum.platform.service.dto.statistics.NavigationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Resource
    private StatsGlobalManager statsGlobalManager;
    @Resource
    private StatsOrgManager statsOrgManager;
    @Resource
    private StatsMetaDataManager statsMetaDataManager;
    @Resource
    private TaskService taskService;
    @Resource
    private OrgService orgService;
    @Resource
    private DataService dataService;

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
            result.setType(NavigationTypeEnum.ORG);
            result.setId(org.getIdentityId());
            return result;
        }

        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.BIZ_QUERY_NOT_EXIST.getMsg());
    }

    @Override
    @Cacheable("getTaskTrend-1")
    public List<StatsDay> getTaskTrend(Integer size) {
        List<Task> taskList = taskService.statisticsOfDay(size);
        Map<Date, StatsDay> statsDayMap = new HashMap<>();
        for (Task task: taskList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String s = sdf.format(task.getStatsTime());
            try {
                Date date =  sdf.parse(s);
                StatsDay statsDay = new StatsDay();
                statsDay.setStatsTime(date);
                statsDay.setStatsValue(task.getTaskCount().longValue());

                statsDayMap.put(date, statsDay);
            } catch (ParseException e) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.BIZ_QUERY_NOT_EXIST.getMsg(), e);
            }
        }

        Date now;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataStr = sdf.format(new Date());
            now =  sdf.parse(dataStr);
        } catch (ParseException e) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.EXCEPTION.getMsg());
        }

        List<StatsDay> statsDayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            statsDayList.add(statsDayMap.computeIfAbsent(now, item -> {
                StatsDay statsDay = new StatsDay();
                statsDay.setStatsTime(item);
                statsDay.setStatsValue(0L);
                statsDay.setPrivacyStatsValue(0L);
                statsDay.setNoPrivacyStatsValue(0L);
                return statsDay;
            }));
            now = DateUtils.addDays(now, -1);
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
    public List<StatsMetaData> getMetaDataUsedTop(Integer size) {
        List<StatsMetaData> statsTokenList = statsMetaDataManager.getMetaDataUsedTop(size);
        return statsTokenList;
    }

    @Override
    public boolean updateStatsGlobal(StatsGlobal global) {
        return statsGlobalManager.updateById(global);
    }

    @Override
    public StatsMetaData getStatsMetaDataById(String metaDataId) {
        StatsMetaData statsMetaData = statsMetaDataManager.getById(metaDataId);
        if(statsMetaData == null && dataService.existMetaData(metaDataId)){
            statsMetaData = new StatsMetaData();
            statsMetaData.setMetaDataId(metaDataId);
            statsMetaData.setUsageCount(0L);
        }
        return statsMetaData;
    }

    @Override
    public boolean saveOrUpdateBatchStatsMetaData(Collection<StatsMetaData> statsMetaDataList) {
        return statsMetaDataManager.saveOrUpdateBatch(statsMetaDataList);
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
            statsGlobal.setDataUsed(0);
            statsGlobal.setDataCount(0);
            statsGlobal.setAddressCountOfActive(0);
            statsGlobal.setAddressCountOfTask(0);
            statsGlobal.setTaskCount(0);
            statsGlobalManager.save(statsGlobal);
        }
        return statsGlobal;
    }
}
