package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.datum.platform.common.constants.SysConfig;
import com.datum.platform.common.utils.CustomHttpClient;
import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.service.SysService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据中心的组织同步
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncPublicityTask {

    @Resource
    private SysService sysService;
    @Resource
    private SysConfig sysConfig;

    @Scheduled(fixedDelay = 1 * 60 * 1000)
    @Lock(keys = "SyncPublicityTask", lockWatchdogTimeout = 5 * 60 * 1000,  attemptTimeout = -1)
    public void run() {
        long begin = DateUtil.current();
        try {
            List<Publicity> publicityList = sysService.listNeedSyncPublicity();
            List<Publicity> updateList = new ArrayList<>();
            publicityList.forEach(publicity -> {
                try {
                    Request request = new Request.Builder().url(publicity.getId().replace("ipfs://", sysConfig.getIpfsGateway())).build();
                    Response response = CustomHttpClient.client.newCall(request).execute();
                    if(response.isSuccessful()){
                        JSONObject resp = JSONObject.parseObject(response.body().string());
                        publicity.setImageUrl(resp.getString("image"));
                        publicity.setDescribe(resp.getString("desc"));
                        publicity.setRemark(resp.getString("remark"));
                        publicity.setCreateTime(null);
                        publicity.setUpdateTime(null);
                        updateList.add(publicity);
                    }
                } catch (Exception e){
                    log.error("同步明细失败！ 公示id = {} message = {}", publicity.getId(), e.getMessage());
                }
            });
            if(updateList.size() > 0){
                sysService.batchUpdatePublicity(updateList);
            }
        } catch (Exception e) {
            log.error("公示信息,失败原因：{}", e.getMessage(), e);
        }
        log.info("公示信息，总耗时:{}ms", DateUtil.current() - begin);
    }
}
