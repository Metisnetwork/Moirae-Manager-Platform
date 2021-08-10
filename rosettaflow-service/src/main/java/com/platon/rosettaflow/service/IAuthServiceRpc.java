package com.platon.rosettaflow.service;

import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;

/**
 * @author hudenian
 * @date 2021/7/15
 */
public interface IAuthServiceRpc {

    /**
     * test gRPC
     * @return result
     */
    NodeIdentityDto getOwner();
}
