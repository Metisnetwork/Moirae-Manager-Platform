package com.datum.platform.grpc.dynamic;

import lombok.Data;

@Data
public class TaskReceiverPolicy {
    private String providerPartyId;
    private String receiverPartyId;
}
