package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.mapper.domain.OrgVc;
import com.datum.platform.service.OrgService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据中心的组织同步
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncOrgVcTask {

    @Resource
    private OrgService orgService;

    @Scheduled(fixedDelay = 60 * 1000)
    @Lock(keys = "SyncOrgVcTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            List<OrgVc> orgVcList = orgService.listNeedVerifyOrgVc();
            orgVcList.forEach(orgVc -> {
                try {
                    log.error("work");
//                    orgService.verifyOrgVcFinish(orgVc.getIdentityId(), 1);
                } catch (Exception e) {
                    log.error("组织VC明细验证失败 id = " + orgVc.getIdentityId(), e);
                }
            });
        } catch (Exception e) {
            log.error("组织VC验证,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织VC验证完成，总耗时:{}ms", DateUtil.current() - begin);
    }
}
