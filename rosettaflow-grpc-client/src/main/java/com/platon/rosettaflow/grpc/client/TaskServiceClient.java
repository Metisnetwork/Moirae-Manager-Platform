package com.platon.rosettaflow.grpc.client;

import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.service.*;
import com.platon.rosettaflow.grpc.task.dto.PublishTaskDeclareResponseDto;
import com.platon.rosettaflow.grpc.task.dto.TaskDataSupplierDeclareDto;
import com.platon.rosettaflow.grpc.task.dto.TaskDto;
import com.platon.rosettaflow.grpc.task.dto.TaskResultReceiverDeclareDto;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @date 2021/9/1
 * @description
 */
@Slf4j
@Component
public class TaskServiceClient {

    @GrpcClient("carrier-grpc-server")
    private TaskServiceGrpc.TaskServiceBlockingStub taskServiceBlockingStub;

    @GrpcClient("carrier-grpc-server")
    private TaskServiceGrpc.TaskServiceStub taskServiceStub;

    /**
     * 发布一个任务,同步等待结果
     */
    public PublishTaskDeclareResponseDto syncPublishTask(TaskDto taskDto) {
        PublishTaskDeclareResponse taskDeclareResponse = taskServiceBlockingStub.publishTaskDeclare(assemblyPublishTaskDeclareRequest(taskDto));
        if (taskDeclareResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(taskDeclareResponse.getStatus(), taskDeclareResponse.getMsg());
        }
        PublishTaskDeclareResponseDto publishTaskDeclareResponseDto = new PublishTaskDeclareResponseDto();
        publishTaskDeclareResponseDto.setStatus(taskDeclareResponse.getStatus());
        publishTaskDeclareResponseDto.setMsg(taskDeclareResponse.getMsg());
        publishTaskDeclareResponseDto.setTaskId(taskDeclareResponse.getTaskId());

        return publishTaskDeclareResponseDto;
    }

    /**
     * 发布一个任务,异步等待结果
     */
    public void asyncPublishTask(TaskDto taskDto) {

        StreamObserver<PublishTaskDeclareResponse> streamObserver = new StreamObserver<PublishTaskDeclareResponse>() {
            @Override
            public void onNext(PublishTaskDeclareResponse value) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };
        taskServiceStub.publishTaskDeclare(assemblyPublishTaskDeclareRequest(taskDto), streamObserver);
    }

    /**
     * 拼装符合rosettaNet发送任务请求参数
     *
     * @param taskDto 任务请求参数
     * @return rosettaNet请求参数对象
     */
    public PublishTaskDeclareRequest assemblyPublishTaskDeclareRequest(TaskDto taskDto) {
        //task_name
        PublishTaskDeclareRequest.Builder publishTaskDeclareRequestBuilder = PublishTaskDeclareRequest.newBuilder()
                .setTaskName("任务名称");

        //owner
        CommonMessage.TaskOrganizationIdentityInfo owner = CommonMessage.TaskOrganizationIdentityInfo.newBuilder()
                .setPartyId("partyId")
                .setName("name")
                .setNodeId("nodeId")
                .setIdentityId("identityId")
                .build();
        publishTaskDeclareRequestBuilder.setOwner(owner);

        // data_supplier
        TaskDataSupplierDeclareDto taskDataSupplierDeclareDto;
        for (int i = 0; i < taskDto.getTaskDataSupplierDeclareDtoList().size(); i++) {
            taskDataSupplierDeclareDto = taskDto.getTaskDataSupplierDeclareDtoList().get(i);
            // member_info
            CommonMessage.TaskOrganizationIdentityInfo taskOrganizationIdentityInfo = CommonMessage.TaskOrganizationIdentityInfo.newBuilder()
                    .setPartyId("partyId")
                    .setName("name")
                    .setNodeId("nodeId")
                    .setIdentityId("identityId")
                    .build();
            //meta_data_info
            TaskMetaDataDeclare.Builder taskMetaDataDeclareBuilder = TaskMetaDataDeclare.newBuilder();
            for (int j = 0; j < taskDataSupplierDeclareDto.getTaskMetaDataDeclareDto().getColumnIndexList().size(); j++) {
                taskMetaDataDeclareBuilder
                        .setMetaDataId("metaDataId")
                        .setColumnIndexList(j, taskDataSupplierDeclareDto.getTaskMetaDataDeclareDto().getColumnIndexList().get(j));
            }

            TaskDataSupplierDeclare taskDataSupplierDeclare = TaskDataSupplierDeclare.newBuilder()
                    .setMemberInfo(taskOrganizationIdentityInfo)
                    .setMetaDataInfo(taskMetaDataDeclareBuilder.build())
                    .build();

            publishTaskDeclareRequestBuilder.setDataSupplier(i, taskDataSupplierDeclare);
        }

        //power_party_ids
        for (int i = 0; i < taskDto.getPowerPartyIds().size(); i++) {
            publishTaskDeclareRequestBuilder.setPowerPartyIds(i, taskDto.getPowerPartyIds().get(i));
        }

        //receivers
        TaskResultReceiverDeclare.Builder taskResultReceiverDeclare;
        for (int i = 0; i < taskDto.getTaskResultReceiverDeclareDtoList().size(); i++) {
            taskResultReceiverDeclare = TaskResultReceiverDeclare.newBuilder();
            TaskResultReceiverDeclareDto taskResultReceiverDeclareDto = taskDto.getTaskResultReceiverDeclareDtoList().get(i);
            //member_info
            CommonMessage.TaskOrganizationIdentityInfo taskOrganizationIdentityInfo = CommonMessage.TaskOrganizationIdentityInfo.newBuilder()
                    .setIdentityId(taskResultReceiverDeclareDto.getMemberInfo().getIdentityId())
                    .setPartyId(taskResultReceiverDeclareDto.getMemberInfo().getPartyId())
                    .setName(taskResultReceiverDeclareDto.getMemberInfo().getName())
                    .setNodeId(taskResultReceiverDeclareDto.getMemberInfo().getNodeId())
                    .build();
            taskResultReceiverDeclare.setMemberInfo(taskOrganizationIdentityInfo);

            //providers
            for (int j = 0; j < taskResultReceiverDeclareDto.getProviderList().size(); j++) {
                CommonMessage.TaskOrganizationIdentityInfo provider = CommonMessage.TaskOrganizationIdentityInfo.newBuilder()
                        .setIdentityId(taskResultReceiverDeclareDto.getProviderList().get(j).getIdentityId())
                        .setPartyId(taskResultReceiverDeclareDto.getProviderList().get(j).getPartyId())
                        .setName(taskResultReceiverDeclareDto.getProviderList().get(j).getName())
                        .setNodeId(taskResultReceiverDeclareDto.getProviderList().get(j).getNodeId())
                        .build();
                taskResultReceiverDeclare.setProviders(j, provider);
            }
            publishTaskDeclareRequestBuilder.setReceivers(i, taskResultReceiverDeclare);
        }

        //operation_cost
        TaskOperationCostDeclare taskOperationCostDeclare = TaskOperationCostDeclare.newBuilder()
                .setCostMem(taskDto.getTaskOperationCostDeclareDto().getCostMem())
                .setCostProcessor(taskDto.getTaskOperationCostDeclareDto().getCostProcessor())
                .setCostBandwidth(taskDto.getTaskOperationCostDeclareDto().getCostBandwidth())
                .setDuration(taskDto.getTaskOperationCostDeclareDto().getDuration())
                .build();
        publishTaskDeclareRequestBuilder.setOperationCost(taskOperationCostDeclare);
        publishTaskDeclareRequestBuilder.setCalculateContractcode(taskDto.getCalculateContractCode());
        publishTaskDeclareRequestBuilder.setDatasplitContractcode(taskDto.getDataSplitContractCode());
        publishTaskDeclareRequestBuilder.setContractExtraParams(taskDto.getContractExtraParams());

        return publishTaskDeclareRequestBuilder.build();
    }
}
