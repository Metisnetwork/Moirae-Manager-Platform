package com.datum.platform.grpc.client.impl;

import carrier.api.WorkFlowServiceGrpc;
import carrier.api.WorkflowRpcApi;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.datum.platform.grpc.client.GrpcWorkflowServiceClient;
import com.datum.platform.grpc.dynamic.WorkflowPolicyOrdinary;
import common.constant.CarrierEnum;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liushuyu
 * @date 2022/9/29 17:49
 * @desc
 */

@Slf4j
@Component
public class GrpcWorkflowServiceClientImpl implements GrpcWorkflowServiceClient {

    @Override
    public WorkflowRpcApi.PublishWorkFlowDeclareResponse publishWorkFlowDeclare(Channel channel, WorkflowRpcApi.PublishWorkFlowDeclareRequest request) {
        log.info("publishWorkFlowDeclare req = {}", request);
        WorkflowRpcApi.PublishWorkFlowDeclareResponse response = WorkFlowServiceGrpc.newBlockingStub(channel).publishWorkFlowDeclare(request);
        log.info("publishWorkFlowDeclare resp = {}", response);
        return response;
    }

    @Override
    public WorkflowRpcApi.QueryWorkStatusResponse queryWorkFlowStatus(Channel channel, WorkflowRpcApi.QueryWorkStatusRequest request) {
        log.info("queryWorkFlowStatus req = {}", request);
        WorkflowRpcApi.QueryWorkStatusResponse response = WorkFlowServiceGrpc.newBlockingStub(channel).queryWorkFlowStatus(request);
        log.info("queryWorkFlowStatus resp = {}", response);
        return response;
    }

    public static void main(String[] args) {
        //工作流任务List
        List<WorkflowRpcApi.WorkFlowTaskStatus> taskList = new ArrayList<>();
        WorkflowRpcApi.WorkFlowTaskStatus task1 = WorkflowRpcApi.WorkFlowTaskStatus.newBuilder()
                .setTaskId("taskId1")
                .setStatus(CarrierEnum.TaskState.TaskState_Failed)
                .setStartAt(1000)
                .setEndAt(2000)
                .build();
        WorkflowRpcApi.WorkFlowTaskStatus task2 = WorkflowRpcApi.WorkFlowTaskStatus.newBuilder()
                .setTaskId("taskId2")
                .setStatus(CarrierEnum.TaskState.TaskState_Failed)
                .setStartAt(1000)
                .setEndAt(2000)
                .build();
        taskList.add(task1);
        taskList.add(task2);

        //工作流List
        List<WorkflowRpcApi.WorkFlowStatus> workflowList = new ArrayList<>();
        WorkflowRpcApi.WorkFlowStatus workFlowStatus = WorkflowRpcApi.WorkFlowStatus.newBuilder()
                .setStatus(CarrierEnum.WorkFlowState.WorkFlowState_Succeed)
                .addAllTaskList(taskList)
                .build();
        workflowList.add(workFlowStatus);

        WorkflowRpcApi.QueryWorkStatusResponse response = WorkflowRpcApi.QueryWorkStatusResponse.newBuilder()
                .setMsg("1")
                .setStatus(0)
                .addAllWorkflowStatusList(workflowList)
                .build();
        System.out.println(response);
        log.info("queryWorkFlowStatus resp = {}", response);
        aa();
    }


    public static void aa(){
        String policy = "[{\"origin\":\"1_0x6f852ba98639a001a315065ecaf2069c7479f4cc_\351\232\220\347\247\201\346\261\202\344\272\244\351\233\206\357\274\210PSI\357\274\211_2_333333333333333333333-v7\",\"reference\":[]},{\"origin\":\"2_0x6f852ba98639a001a315065ecaf2069c7479f4cc_\351\232\220\347\247\201\347\272\277\346\200\247\345\233\236\345\275\222\350\256\255\347\273\203_2_333333333333333333333-v7\",\"reference\":[{\"dependPartyId\":[\"data1\",\"data2\"],\"target\":\"1_0x6f852ba98639a001a315065ecaf2069c7479f4cc_\351\232\220\347\247\201\346\261\202\344\272\244\351\233\206\357\274\210PSI\357\274\211_2_333333333333333333333-v7\",\"dependParams\":[\"{\\\"inputType\\\":1,\\\"keyColumnName\\\":\\\"CLIENT_ID\\\",\\\"partyId\\\":\\\"data1\\\",\\\"selectedColumnNames\\\":[\\\"DEFAULT\\\"]}\",\"{\\\"inputType\\\":1,\\\"keyColumnName\\\":\\\"CLIENT_ID\\\",\\\"partyId\\\":\\\"data2\\\",\\\"selectedColumnNames\\\":[\\\"DEFAULT\\\"]}\"],\"merge\":false,\"dependParamsType\":[30001,30001]}]},{\"origin\":\"3_0x6f852ba98639a001a315065ecaf2069c7479f4cc_\351\232\220\347\247\201\346\261\202\344\272\244\351\233\206\357\274\210PSI\357\274\211_2_333333333333333333333-v7\",\"reference\":[]},{\"origin\":\"4_0x6f852ba98639a001a315065ecaf2069c7479f4cc_\351\232\220\347\247\201\347\272\277\346\200\247\345\233\236\345\275\222\351\242\204\346\265\213_2_333333333333333333333-v7\",\"reference\":[{\"dependPartyId\":[\"data1\",\"data2\"],\"target\":\"3_0x6f852ba98639a001a315065ecaf2069c7479f4cc_\351\232\220\347\247\201\346\261\202\344\272\244\351\233\206\357\274\210PSI\357\274\211_2_333333333333333333333-v7\",\"dependParams\":[\"{\\\"inputType\\\":1,\\\"keyColumnName\\\":\\\"CLIENT_ID\\\",\\\"partyId\\\":\\\"data1\\\",\\\"selectedColumnNames\\\":[\\\"DEFAULT\\\"]}\",\"{\\\"inputType\\\":1,\\\"keyColumnName\\\":\\\"CLIENT_ID\\\",\\\"partyId\\\":\\\"data2\\\",\\\"selectedColumnNames\\\":[\\\"DEFAULT\\\"]}\"],\"merge\":false,\"dependParamsType\":[30001,30001]},{\"dependPartyId\":[\"data1\",\"data2\"],\"target\":\"2_0x6f852ba98639a001a315065ecaf2069c7479f4cc_\351\232\220\347\247\201\347\272\277\346\200\247\345\233\236\345\275\222\350\256\255\347\273\203_2_333333333333333333333-v7\",\"dependParams\":[\"{\\\"inputType\\\":2,\\\"partyId\\\":\\\"data1\\\"}\",\"{\\\"inputType\\\":2,\\\"partyId\\\":\\\"data2\\\"}\"],\"merge\":false,\"dependParamsType\":[30002,30002]}]}]";

        System.out.println(policy);
    }
}
