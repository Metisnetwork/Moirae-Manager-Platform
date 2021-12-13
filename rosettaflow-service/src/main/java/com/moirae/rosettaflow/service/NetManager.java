package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.mapper.domain.Organization;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hudenian
 * @date 2021/12/10
 */
@Slf4j
@Component
public class NetManager {

    private final Map<String, ManagedChannel> channelMap = new ConcurrentHashMap<>();
    private final Map<String, String> identityIdIpPortMap = new HashMap<>();
    private final String delimiter = "-";

    @Resource
    private IOrganizationService organizationService;

    @PostConstruct
    public void init() {
        List<Organization> organizationList = organizationService.list();

        for (Organization organization : organizationList) {
            channelMap.put(organization.getIdentityId(), assemblyChannel(organization.getIdentityIp(), Integer.parseInt(organization.getIdentityPort())));
            identityIdIpPortMap.put(organization.getIdentityId(),organization.getIdentityIp()+delimiter+organization.getIdentityPort());
        }
    }

    public ManagedChannel assemblyChannel(String ip, int port) {
        return ManagedChannelBuilder
                .forAddress(ip, port)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .build();
    }

    public ManagedChannel getChannel(String identityId){
        return channelMap.get(identityId);
    }

    public List<String> getIpPortByIdentityId(String identityId){
        List<String> ipPortList = new ArrayList<>();
        ipPortList.add(identityIdIpPortMap.get(identityId).split(delimiter)[0]);
        ipPortList.add(identityIdIpPortMap.get(identityId).split(delimiter)[0]);
        return ipPortList;
    }
}
