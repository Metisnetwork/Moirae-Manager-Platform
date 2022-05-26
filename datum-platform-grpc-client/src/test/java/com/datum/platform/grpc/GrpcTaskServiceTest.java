package com.datum.platform.grpc;

import com.datum.platform.grpc.client.GrpcSysServiceClient;
import com.datum.platform.grpc.client.impl.GrpcSysServiceClientImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrpcSysServiceClientImpl.class)
public class GrpcTaskServiceTest {

    @Resource
    private GrpcSysServiceClient grpcSysServiceClient;

    @Test
    public void getAlgTree() throws Exception {
        System.out.println(grpcSysServiceClient);
    }
}
