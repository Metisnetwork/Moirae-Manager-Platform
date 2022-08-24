package com.datum.platform.task;

import carrier.types.Identitydata;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.datum.platform.common.utils.AddressChangeUtils;
import com.datum.platform.grpc.client.impl.GrpcAuthServiceClientImpl;
import com.datum.platform.mapper.domain.Org;
import com.datum.platform.mapper.domain.OrgVc;
import com.datum.platform.mapper.enums.DataSyncTypeEnum;
import com.datum.platform.mapper.enums.OrgStatusEnum;
import com.datum.platform.service.OrgService;
import com.datum.platform.service.SysService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据中心的组织同步
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class SyncDcOrgTask {

    @Resource
    private GrpcAuthServiceClientImpl authServiceClient;
    @Resource
    private OrgService organizationService;
    @Resource
    private SysService sysService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "SyncDcOrgTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            sysService.syncFromDc(DataSyncTypeEnum.ORG_STATUS.getDataType(),DataSyncTypeEnum.ORG_STATUS.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return authServiceClient.getIdentityList(latestSynced);
                    },
                    (grpcResponseList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        // 批量更新
                        this.batchUpdateOrg(grpcResponseList);
                    },
                    (grpcResponseList) -> {//4.根据分页列表grpcResponseList获取最新的同步时间latestSynced
                        return grpcResponseList
                                .get(grpcResponseList.size() - 1)
                                .getUpdateAt();
                    });
        } catch (Exception e) {
            log.error("组织信息同步,从net同步元数据失败,失败原因：{}", e.getMessage(), e);
        }
        log.info("组织信息同步结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    /**
     * @param nodeIdentityDtoList 需更新数据
     */
    private void batchUpdateOrg(List<Identitydata.Organization> nodeIdentityDtoList) {
        List<OrgVc> orgVcList = new ArrayList<>();
        Set<String> publicityIdSet = new HashSet<>();
        List<Org> orgList = nodeIdentityDtoList.stream().map(nodeIdentityDto -> {
                    Org org = new Org();
                    org.setIdentityId(nodeIdentityDto.getIdentityId());
                    org.setWalletAddress(AddressChangeUtils.did20xAddress(nodeIdentityDto.getIdentityId()));
                    org.setNodeId(nodeIdentityDto.getNodeId());
                    org.setNodeName(nodeIdentityDto.getNodeName());
                    org.setImageUrl(nodeIdentityDto.getImageUrl());
                    org.setDetails(nodeIdentityDto.getDetails());
                    org.setStatus(OrgStatusEnum.find(nodeIdentityDto.getStatus().getNumber()));
                    org.setUpdateAt(new Date(nodeIdentityDto.getUpdateAt()));

                    if(StringUtils.isNotBlank(nodeIdentityDto.getCredential())){
                        JSONObject vc = JSONObject.parseObject(nodeIdentityDto.getCredential());
                        OrgVc orgVc = new OrgVc();
                        orgVc.setIdentityId(nodeIdentityDto.getIdentityId());
                        orgVc.setVcContent(nodeIdentityDto.getCredential());
                        orgVc.setIssuer(vc.getString("issuer"));
                        orgVc.setIssuanceDate(vc.getDate("issuanceDate"));
                        orgVc.setExpirationDate(vc.getDate("expirationDate"));
                        orgVc.setHolder(vc.getString("holder"));
                        orgVc.setPublicityId(vc.getJSONObject("claimData").getString("url"));
                        orgVc.setVcProof(vc.getString("proof"));
                        orgVcList.add(orgVc);

                        if(StringUtils.isNotBlank(orgVc.getPublicityId())){
                            publicityIdSet.add(orgVc.getPublicityId());
                        }
                    }
                    return org;
                })
                .collect(Collectors.toList());

        List<String> addOrgIdList = orgList
                .stream()
                .map(Org::getIdentityId)
                .collect(Collectors.toList());
        //更新
        organizationService.batchReplace(orgList, addOrgIdList, orgVcList, publicityIdSet);
    }
}
