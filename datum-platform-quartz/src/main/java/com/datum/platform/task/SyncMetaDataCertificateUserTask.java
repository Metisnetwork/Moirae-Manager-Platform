package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.datum.platform.chain.platon.contract.DataTokenTemplateContract;
import com.datum.platform.chain.platon.contract.ERC721TemplateContract;
import com.datum.platform.mapper.domain.MetaDataCertificate;
import com.datum.platform.mapper.domain.MetaDataCertificateUser;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import com.datum.platform.service.DataService;
import com.datum.platform.service.UserService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncMetaDataCertificateUserTask {

    @Resource
    private DataService dataService;
    @Resource
    private UserService userService;
    @Resource
    private DataTokenTemplateContract dataTokenTemplateContract;
    @Resource
    private ERC721TemplateContract erc721TemplateContract;

//    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncMetaDataCertificateUserTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            //查询所有在线用户地址
            List<String> addressList = userService.getOnlineUserIdList();
            //查询所有数据凭证
            List<MetaDataCertificate> metaDataCertificateList = dataService.listMetaDataCertificate();
            for (String address: addressList) {
                try {
                    sync(address, metaDataCertificateList);
                } catch (Exception e) {
                    log.error("MetaDataCertificateUser信息同步, 明细失败：{}", address, e);
                }
            }
        } catch (Exception e) {
            log.error("MetaDataCertificateUser信息同步失败原因：{}", e.getMessage(), e);
        }
        log.info("MetaDataCertificateUser信息同步，总耗时:{}ms", DateUtil.current() - begin);
    }

    private void sync(String address, List<MetaDataCertificate> metaDataCertificateList){
        List<MetaDataCertificateUser> metaDataCertificateUserList = new ArrayList<>();
        for (MetaDataCertificate metaDataCertificate: metaDataCertificateList) {
            MetaDataCertificateUser metaDataCertificateUser = queryOnLine(address, metaDataCertificate);
            if(metaDataCertificateUser != null){
                metaDataCertificateUserList.add(metaDataCertificateUser);
            }
        }
        if(metaDataCertificateUserList.size() > 0){
            dataService.saveOrUpdateBatchMetaDataCertificateUser(address, metaDataCertificateUserList);
        }
    }

    private MetaDataCertificateUser queryOnLine(String address, MetaDataCertificate metaDataCertificate){
        if(metaDataCertificate.getType() == MetaDataCertificateTypeEnum.NO_ATTRIBUTES){
            BigInteger balance = dataTokenTemplateContract.balanceOf(metaDataCertificate.getTokenAddress(), address);
            BigInteger allowance = dataTokenTemplateContract.allowance(metaDataCertificate.getTokenAddress(), address);
            if(balance.compareTo(BigInteger.ZERO) > 0 || allowance.compareTo(BigInteger.ZERO) > 0 || dataService.existMetaDataCertificateUser(address, metaDataCertificate.getId())){
                MetaDataCertificateUser metaDataCertificateUser = new MetaDataCertificateUser();
                metaDataCertificateUser.setAddress(address);
                metaDataCertificateUser.setMetaDataCertificateId(metaDataCertificate.getId());
                metaDataCertificateUser.setBalance(balance.toString());
                metaDataCertificateUser.setAuthorizeBalance(allowance.toString());
                return metaDataCertificateUser;
            }
        }

        if(metaDataCertificate.getType() == MetaDataCertificateTypeEnum.HAVE_ATTRIBUTES){
            String owner = erc721TemplateContract.ownerOf(metaDataCertificate.getTokenAddress(), new BigInteger(metaDataCertificate.getTokenId()));
            if(StringUtils.equals(owner, address) || dataService.existMetaDataCertificateUser(address, metaDataCertificate.getId())){
                MetaDataCertificateUser metaDataCertificateUser = new MetaDataCertificateUser();
                metaDataCertificateUser.setAddress(address);
                metaDataCertificateUser.setMetaDataCertificateId(metaDataCertificate.getId());
                metaDataCertificateUser.setBalance(StringUtils.equals(owner, address)?"1":"0");
                metaDataCertificateUser.setAuthorizeBalance("0");
                return metaDataCertificateUser;
            }
        }
        return null;
    }
}
