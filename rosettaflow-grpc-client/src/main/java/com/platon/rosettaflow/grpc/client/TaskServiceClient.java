package com.platon.rosettaflow.grpc.client;

import com.google.protobuf.ByteString;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import com.platon.rosettaflow.grpc.service.*;
import com.platon.rosettaflow.grpc.task.req.dto.*;
import com.platon.rosettaflow.grpc.task.resp.dto.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
    public void asyncPublishTask(TaskDto taskDto, Consumer<PublishTaskDeclareResponse> callback) {

        StreamObserver<PublishTaskDeclareResponse> streamObserver = new StreamObserver<PublishTaskDeclareResponse>() {
            @Override
            public void onNext(PublishTaskDeclareResponse value) {
                callback.accept(value);
            }

            @Override
            public void onError(Throwable t) {
                log.info("task {} process fail", taskDto.getTaskName());
            }

            @Override
            public void onCompleted() {
                log.info("task {} process finish", taskDto.getTaskName());
            }
        };
        taskServiceStub.publishTaskDeclare(assemblyPublishTaskDeclareRequest(taskDto), streamObserver);
    }

    /**
     * 查看全部任务详情列表
     */
    public void getTaskDetailList() {
        List<TaskDetailResponseDto> taskDetailResponseDtoList = new ArrayList<>();
        CommonMessage.EmptyGetParams emptyGetParams = CommonMessage.EmptyGetParams.newBuilder().build();
        GetTaskDetailListResponse getTaskDetailListResponse = taskServiceBlockingStub.getTaskDetailList(emptyGetParams);

        if (getTaskDetailListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(getTaskDetailListResponse.getStatus(), getTaskDetailListResponse.getMsg());
        }

        TaskDetailResponseDto taskDetailResponseDto;
        GetTaskDetailResponse getTaskDetailResponse;
        TaskDetailDto taskDetailDto;
        for (int i = 0; i < getTaskDetailListResponse.getTaskListCount(); i++) {
            getTaskDetailResponse = getTaskDetailListResponse.getTaskList(i);

            taskDetailResponseDto = new TaskDetailResponseDto();
            //任务详情
            taskDetailDto = new TaskDetailDto();
            // 任务Id
            taskDetailDto.setTaskId(getTaskDetailResponse.getInformation().getTaskId());
            // 任务名称
            taskDetailDto.setTaskName(getTaskDetailResponse.getInformation().getTaskName());
            // 发起任务的用户的信息 (task是属于用户的)
            taskDetailDto.setUser(getTaskDetailResponse.getInformation().getUser());
            //用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址)
            taskDetailDto.setUserType((byte) getTaskDetailResponse.getInformation().getUserType().getNumber());

            // 任务发起方
            OrganizationIdentityInfoDto sender = new OrganizationIdentityInfoDto();
            sender.setPartyId(getTaskDetailResponse.getInformation().getSender().getPartyId());
            sender.setName(getTaskDetailResponse.getInformation().getSender().getName());
            sender.setNodeId(getTaskDetailResponse.getInformation().getSender().getNodeId());
            sender.setIdentityId(getTaskDetailResponse.getInformation().getSender().getIdentityId());
            taskDetailDto.setSender(sender);

            //算法提供方 (目前就是和 任务发起方是同一个 ...)
            OrganizationIdentityInfoDto algoSupplier = new OrganizationIdentityInfoDto();
            algoSupplier.setPartyId(getTaskDetailResponse.getInformation().getAlgoSupplier().getPartyId());
            algoSupplier.setName(getTaskDetailResponse.getInformation().getAlgoSupplier().getName());
            algoSupplier.setNodeId(getTaskDetailResponse.getInformation().getAlgoSupplier().getNodeId());
            algoSupplier.setIdentityId(getTaskDetailResponse.getInformation().getAlgoSupplier().getIdentityId());
            taskDetailDto.setAlgoSupplier(algoSupplier);

            //数据提供方
            List<TaskDataSupplierDto> taskDataSupplierShowDtoList = new ArrayList<>();
            TaskDataSupplierDto taskDataSupplierDto;

            OrganizationIdentityInfoDto taskOrganizationIdentityInfoDto;
            for (int j = 0; j < getTaskDetailResponse.getInformation().getDataSupplierCount(); j++) {
                taskDataSupplierDto = new TaskDataSupplierDto();

                taskOrganizationIdentityInfoDto = new OrganizationIdentityInfoDto();
                taskOrganizationIdentityInfoDto.setPartyId(getTaskDetailResponse.getInformation().getDataSupplier(j).getMemberInfo().getPartyId());
                taskOrganizationIdentityInfoDto.setName(getTaskDetailResponse.getInformation().getDataSupplier(j).getMemberInfo().getName());
                taskOrganizationIdentityInfoDto.setNodeId(getTaskDetailResponse.getInformation().getDataSupplier(j).getMemberInfo().getNodeId());
                taskOrganizationIdentityInfoDto.setIdentityId(getTaskDetailResponse.getInformation().getDataSupplier(j).getMemberInfo().getIdentityId());

                taskDataSupplierDto.setMemberInfo(taskOrganizationIdentityInfoDto);
                taskDataSupplierDto.setMetaDataId(getTaskDetailResponse.getInformation().getDataSupplier(j).getMetaDataId());
                taskDataSupplierDto.setMetaDataName(getTaskDetailResponse.getInformation().getDataSupplier(j).getMetaDataName());

                taskDataSupplierShowDtoList.add(taskDataSupplierDto);
            }
            taskDetailDto.setDataSuppliers(taskDataSupplierShowDtoList);

            //算力提供方
            List<TaskPowerSupplierDto> taskPowerSupplierShowDtoList = new ArrayList<>();
            TaskPowerSupplierDto taskPowerSupplierShowDto;
            ResourceUsedDetailDto resourceUsedDetailShowDto;
            for (int j = 0; j < getTaskDetailResponse.getInformation().getPowerSupplierCount(); j++) {
                //算力提供方成员信息
                taskPowerSupplierShowDto = new TaskPowerSupplierDto();
                OrganizationIdentityInfoDto powerIdentityInfoDto = new OrganizationIdentityInfoDto();
                powerIdentityInfoDto.setPartyId(getTaskDetailResponse.getInformation().getPowerSupplier(j).getMemberInfo().getPartyId());
                powerIdentityInfoDto.setName(getTaskDetailResponse.getInformation().getPowerSupplier(j).getMemberInfo().getName());
                powerIdentityInfoDto.setNodeId(getTaskDetailResponse.getInformation().getPowerSupplier(j).getMemberInfo().getNodeId());
                powerIdentityInfoDto.setIdentityId(getTaskDetailResponse.getInformation().getPowerSupplier(j).getMemberInfo().getIdentityId());
                //算力提供方资源信息
                ResourceUsedDetailDto powerInfo = new ResourceUsedDetailDto();
                powerInfo.setTotalMem(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getTotalMem());
                powerInfo.setTotalProcessor(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getTotalProcessor());
                powerInfo.setTotalBandwidth(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getTotalBandwidth());
                powerInfo.setTotalDisk(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getTotalDisk());

                powerInfo.setUsedMem(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getUsedMem());
                powerInfo.setUsedProcessor(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getUsedProcessor());
                powerInfo.setUsedBandwidth(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getUsedBandwidth());
                powerInfo.setUsedDisk(getTaskDetailResponse.getInformation().getPowerSupplier(j).getPowerInfo().getUsedDisk());

                taskPowerSupplierShowDto.setMemberInfo(powerIdentityInfoDto);
                taskPowerSupplierShowDto.setPowerInfo(powerInfo);
                taskPowerSupplierShowDtoList.add(taskPowerSupplierShowDto);
            }
            taskDetailDto.setPowerSuppliers(taskPowerSupplierShowDtoList);

            //任务结果方
            List<OrganizationIdentityInfoDto> receivers = new ArrayList<>();
            OrganizationIdentityInfoDto receiver;
            for (int j = 0; j < getTaskDetailResponse.getInformation().getReceiversCount(); j++) {
                receiver = new OrganizationIdentityInfoDto();
                receiver.setPartyId(getTaskDetailResponse.getInformation().getReceivers(i).getPartyId());
                receiver.setName(getTaskDetailResponse.getInformation().getReceivers(i).getName());
                receiver.setNodeId(getTaskDetailResponse.getInformation().getReceivers(i).getNodeId());
                receiver.setIdentityId(getTaskDetailResponse.getInformation().getReceivers(i).getIdentityId());
                receivers.add(receiver);
            }
            taskDetailDto.setReceivers(receivers);

            //TODO 联调测试验证日期格式
            taskDetailDto.setCreateAt(getTaskDetailResponse.getInformation().getCreateAt());
            taskDetailDto.setStartAt(getTaskDetailResponse.getInformation().getStartAt());
            taskDetailDto.setEndAt(getTaskDetailResponse.getInformation().getEndAt());
            taskDetailDto.setState(getTaskDetailResponse.getInformation().getState());

            //任务所需资源声明
            TaskOperationCostDeclareDto taskOperationCostDeclareDto = new TaskOperationCostDeclareDto();
            taskOperationCostDeclareDto.setCostMem(getTaskDetailResponse.getInformation().getOperationCost().getCostMem());
            taskOperationCostDeclareDto.setCostProcessor(getTaskDetailResponse.getInformation().getOperationCost().getCostProcessor());
            taskOperationCostDeclareDto.setCostBandwidth(getTaskDetailResponse.getInformation().getOperationCost().getCostBandwidth());
            taskOperationCostDeclareDto.setDuration(getTaskDetailResponse.getInformation().getOperationCost().getDuration());
            taskDetailDto.setOperationCost(taskOperationCostDeclareDto);

            //拼装最外层信息
            taskDetailResponseDto.setInformation(taskDetailDto);
            taskDetailResponseDto.setRole(getTaskDetailResponse.getRole());
            taskDetailResponseDtoList.add(taskDetailResponseDto);
        }
    }

    /**
     * 查看某个任务的全部事件列表通过单个任务ID
     */
    public List<TaskEventShowDto> getTaskEventList(String taskId) {
        GetTaskEventListRequest getTaskEventListRequest = GetTaskEventListRequest.newBuilder().setTaskId(taskId).build();
        GetTaskEventListResponse taskEventListResponse = taskServiceBlockingStub.getTaskEventList(getTaskEventListRequest);
        return getTaskEventShowDots(taskEventListResponse);
    }

    /**
     * 查看某个任务的全部事件列表通过批量的任务ID
     */
    public List<TaskEventShowDto> getTaskEventListByTaskIds(String[] taskIds) {
        GetTaskEventListByTaskIdsRequest.Builder getTaskEventListByTaskIdsRequestBuilder = GetTaskEventListByTaskIdsRequest.newBuilder();
        for (int i = 0; i < taskIds.length; i++) {
            getTaskEventListByTaskIdsRequestBuilder.setTaskIds(i, taskIds[i]);
        }
        GetTaskEventListResponse taskEventListResponse = taskServiceBlockingStub.getTaskEventListByTaskIds(getTaskEventListByTaskIdsRequestBuilder.build());
        return getTaskEventShowDots(taskEventListResponse);
    }

    private List<TaskEventShowDto> getTaskEventShowDots(GetTaskEventListResponse taskEventListResponse) {
        if (taskEventListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(taskEventListResponse.getStatus(), taskEventListResponse.getMsg());
        }

        List<TaskEventShowDto> taskEventShowDtoList = new ArrayList<>();
        TaskEventShowDto taskEventShowDto;
        for (int i = 0; i < taskEventListResponse.getTaskEventListCount(); i++) {
            taskEventShowDto = new TaskEventShowDto();
            taskEventShowDto.setType(taskEventListResponse.getTaskEventList(i).getType());
            taskEventShowDto.setTaskId(taskEventListResponse.getTaskEventList(i).getTaskId());

            NodeIdentityDto owner = new NodeIdentityDto();
            owner.setName(taskEventListResponse.getTaskEventList(i).getOwner().getName());
            owner.setNodeId(taskEventListResponse.getTaskEventList(i).getOwner().getNodeId());
            owner.setIdentityId(taskEventListResponse.getTaskEventList(i).getOwner().getIdentityId());

            taskEventShowDto.setOwner(owner);
            taskEventShowDto.setContent(taskEventListResponse.getTaskEventList(i).getContent());
            taskEventShowDto.setCreateAt(taskEventListResponse.getTaskEventList(i).getCreateAt());
            taskEventShowDtoList.add(taskEventShowDto);
        }
        return taskEventShowDtoList;
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
                .setTaskName(taskDto.getTaskName());

        // 发起任务的用户的信息 (task是属于用户的)
        publishTaskDeclareRequestBuilder.setUser(taskDto.getUser());
        //用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址)
        publishTaskDeclareRequestBuilder.setUserType(CommonMessage.UserType.forNumber(taskDto.getUserType()));
        //任务发起者 组织信息
        CommonMessage.TaskOrganizationIdentityInfo sender = CommonMessage.TaskOrganizationIdentityInfo.newBuilder()
                .setPartyId("partyId")
                .setName("name")
                .setNodeId("nodeId")
                .setIdentityId("identityId")
                .build();
        publishTaskDeclareRequestBuilder.setSender(sender);

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
                        .setMetaDataId(taskDataSupplierDeclareDto.getTaskMetaDataDeclareDto().getMetaDataId())
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

        //发起任务的账户的签名
        publishTaskDeclareRequestBuilder.setSign(ByteString.copyFromUtf8(taskDto.getSign()));

        return publishTaskDeclareRequestBuilder.build();
    }
}
