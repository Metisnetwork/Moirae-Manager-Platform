package com.datum.platform.grpc.dynamic;

import lombok.Data;

@Data
public class TaskPowerPolicy {
    private String providerPartyId;
    private String powerPartyId;
    private String identityId;
}
