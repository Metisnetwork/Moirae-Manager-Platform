package com.moirae.rosettaflow.service;

import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
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

    @Resource
    private GrpcAuthService grpcAuthService;

    @PostConstruct
    public void init() {
        try {
            //判断组织的nodeid是否同步，未同步则进行同步操作
            List<Organization> organizationList = organizationService.list();
            if (organizationList.size() == 0) {
                log.error(">>>>>>>>>>>>>>>>>>>>>未配置组织信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                return;
            }
            int nodeIdLength = 30;
            if (organizationList.get(0).getNodeId().length() < nodeIdLength) {
                Map<String, String> identityNodeIdMap = new HashMap<>(organizationList.size());
                List<NodeIdentityDto> nodeIdentityDtoList = grpcAuthService.getAllIdentityList();
                for (NodeIdentityDto nodeIdentityDto : nodeIdentityDtoList) {
                    identityNodeIdMap.put(nodeIdentityDto.getIdentityId(), nodeIdentityDto.getNodeId());
                }
                for (Organization organization : organizationList) {
                    organization.setNodeId(identityNodeIdMap.get(organization.getIdentityId()));
                }
                organizationService.saveOrUpdateBatch(organizationList);
            }

            for (Organization organization : organizationList) {
                channelMap.put(organization.getIdentityId(), assemblyChannel(organization.getIdentityIp(), organization.getIdentityPort()));
                identityIdIpPortMap.put(organization.getIdentityId(), organization.getIdentityIp() + delimiter + organization.getIdentityPort());
            }
        } catch (Exception e) {
            log.error("NetManager-同步组织数据失败, 错误信息:{}", e.getMessage());
        }
    }

    /**
     * 根据ip 与 port 组装channel
     *
     * @param ip   组织ip
     * @param port 组织端口
     * @return channel
     */
    public ManagedChannel assemblyChannel(String ip, Integer port) {
        return ManagedChannelBuilder
                .forAddress(ip, port)
                .usePlaintext()
                .keepAliveWithoutCalls(true)
                .build();
    }

    /**
     * 根据identityId获取channel
     *
     * @param identityId identityId
     * @return channel
     */
    public ManagedChannel getChannel(String identityId) {
        ManagedChannel managedChannel = channelMap.get(identityId);
        if (managedChannel == null) {
            managedChannel = loadOrganizationByIdentityId(identityId);
        }
        return managedChannel;
    }

    /**
     * 根据identityId加载机构channel
     */
    public ManagedChannel loadOrganizationByIdentityId(String identityId) {
        Organization organization = organizationService.getByIdentityId(identityId);
        if (null == organization) {
            log.error("Can not find organization by identityId:{}", identityId);
            throw new BusinessException(RespCodeEnum.BIZ_EXCEPTION, ErrorMsg.ORGANIZATION_NOT_EXIST.getMsg());
        }

        List<NodeIdentityDto> nodeIdentityDtoList = grpcAuthService.getAllIdentityList();
        for (NodeIdentityDto nodeIdentityDto : nodeIdentityDtoList) {
            if (organization.getIdentityId().equals(nodeIdentityDto.getIdentityId())) {
                organization.setNodeId(nodeIdentityDto.getNodeId());
                break;
            }
        }
        organizationService.updateById(organization);
        ManagedChannel channel = assemblyChannel(organization.getIdentityIp(), organization.getIdentityPort());

        channelMap.put(organization.getIdentityId(), channel);
        identityIdIpPortMap.put(organization.getIdentityId(), organization.getIdentityIp() + delimiter + organization.getIdentityPort());
        return channel;
    }

    /**
     * 根据identityId获取对应的ip与端口
     *
     * @param identityId 组织的identityId
     * @return List[0]=ip List[1]=port
     */
    @SuppressWarnings("unused")
    public List<String> getIpPortByIdentityId(String identityId) {
        List<String> ipPortList = new ArrayList<>();
        ipPortList.add(identityIdIpPortMap.get(identityId).split(delimiter)[0]);
        ipPortList.add(identityIdIpPortMap.get(identityId).split(delimiter)[1]);
        return ipPortList;
    }

    public void addChannel(String identityId, ManagedChannel channel, String ip, Integer port) {
        //如果存在老的channel关闭
        ManagedChannel oldChannel = channelMap.get(identityId);
        if (oldChannel != null) {
            oldChannel.shutdown();
        }
        channelMap.put(identityId, channel);
        identityIdIpPortMap.put(identityId, ip + delimiter + port);
    }
}
