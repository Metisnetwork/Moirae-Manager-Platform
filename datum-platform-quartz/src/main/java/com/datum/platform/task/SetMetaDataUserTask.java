package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.service.DataService;
import com.datum.platform.service.UserService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
public class SetMetaDataUserTask {

    @Resource
    private DataService dataService;
    @Resource
    private UserService userService;

    @Scheduled(fixedDelay = 60 * 1000)
    @Lock(keys = "SetMetaDataUserTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            //查询所有在线用户地址
            List<String> addressList = userService.getOnlineUserIdList();
            for (String address: addressList) {
                try {
                    dataService.setMetaDataUser(address);
                } catch (Exception e) {
                    log.error("设置用户可见的元数据, 明细失败：{}", address, e);
                }
            }
        } catch (Exception e) {
            log.error("设置用户可见的元数据, 失败原因：{}", e.getMessage(), e);
        }
        log.info("设置用户可见的元数据，总耗时:{}ms", DateUtil.current() - begin);
    }
}
