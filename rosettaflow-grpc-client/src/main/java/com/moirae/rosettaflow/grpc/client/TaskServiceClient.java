package com.moirae.rosettaflow.grpc.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import com.moirae.rosettaflow.grpc.service.*;
import com.moirae.rosettaflow.grpc.task.req.dto.*;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
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
 * @description 任务处理相关接口
 */
@Slf4j
@Component
public class TaskServiceClient {

    @GrpcClient("carrier-grpc-server")
    TaskServiceGrpc.TaskServiceBlockingStub taskServiceBlockingStub;

    @GrpcClient("carrier-grpc-server")
    TaskServiceGrpc.TaskServiceStub taskServiceStub;

    /**
     * 发布一个任务,同步等待结果
     */
    public PublishTaskDeclareResponseDto syncPublishTask(Channel channel, TaskDto taskDto) {
//        PublishTaskDeclareResponse taskDeclareResponse = taskServiceBlockingStub.publishTaskDeclare(assemblyPublishTaskDeclareRequest(taskDto));
        PublishTaskDeclareResponse taskDeclareResponse = TaskServiceGrpc.newBlockingStub(channel).publishTaskDeclare(assemblyPublishTaskDeclareRequest(taskDto));


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
                log.error("task {} process fail,fail reason:{}", taskDto.getTaskName(), t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                log.info("task {} process finish", taskDto.getTaskName());
            }
        };
        taskServiceStub.publishTaskDeclare(assemblyPublishTaskDeclareRequest(taskDto), streamObserver);
    }

    /**
     * 查看本组织参与过的全部任务详情列表
     */
    public List<TaskDetailResponseDto> getTaskDetailList() {
        List<TaskDetailResponseDto> taskDetailResponseDtoList = new ArrayList<>();
        Empty empty = Empty.newBuilder().build();
        GetTaskDetailListResponse getTaskDetailListResponse = taskServiceBlockingStub.getTaskDetailList(empty);

        if (getTaskDetailListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            log.error("TaskServiceClient->getTaskDetailList() fail reason:{}", getTaskDetailListResponse.getMsg());
            throw new BusinessException(getTaskDetailListResponse.getStatus(), getTaskDetailListResponse.getMsg());
        }

        return getTaskDetailResponseDtos(taskDetailResponseDtoList, getTaskDetailListResponse);
    }

    public List<TaskDetailResponseDto> getTaskDetailResponseDtos(List<TaskDetailResponseDto> taskDetailResponseDtoList, GetTaskDetailListResponse getTaskDetailListResponse) {
        TaskDetailResponseDto taskDetailResponseDto;
        GetTaskDetailResponse getTaskDetailResponse;
        TaskDetailShowDto taskDetailDto;
        for (int i = 0; i < getTaskDetailListResponse.getTaskListCount(); i++) {
            getTaskDetailResponse = getTaskDetailListResponse.getTaskList(i);
            taskDetailResponseDto = new TaskDetailResponseDto();
            //任务详情
            taskDetailDto = new TaskDetailShowDto();

            // 任务Id
            taskDetailDto.setTaskId(getTaskDetailResponse.getInformation().getTaskId());
            // 任务名称
            taskDetailDto.setTaskName(getTaskDetailResponse.getInformation().getTaskName());
            //发起任务的用户的信息 (task是属于用户的)
            taskDetailDto.setUser(getTaskDetailResponse.getInformation().getUser());
            //用户类型 (0: 未定义; 1: 第二地址; 2: 测试网地址; 3: 主网地址)
            taskDetailDto.setUserType(getTaskDetailResponse.getInformation().getUserTypeValue());

            // 任务发起方组织信息
            taskDetailDto.setSender(getOrganizationIdentityInfoDto(getTaskDetailResponse.getInformation().getSender()));
            // 算法提供方组织信息 (目前就是和 任务发起方是同一个 ...)
            taskDetailDto.setAlgoSupplier(getOrganizationIdentityInfoDto(getTaskDetailResponse.getInformation().getAlgoSupplier()));
            //数据提供方组织信息
            assemblyDataSuppliers(taskDetailDto, getTaskDetailResponse.getInformation());
            //算力提供方信息
            assemblyPowerSuppliers(taskDetailDto, getTaskDetailResponse.getInformation());
            // 任务结果方
            List<OrganizationIdentityInfoDto> receivers = new ArrayList<>();
            for (int j = 0; j < getTaskDetailResponse.getInformation().getReceiversCount(); j++) {
                receivers.add(getOrganizationIdentityInfoDto(getTaskDetailResponse.getInformation().getReceivers(j)));
            }
            taskDetailDto.setReceivers(receivers);

            // 任务发起时间 (单位: ms)
            taskDetailDto.setCreateAt(getTaskDetailResponse.getInformation().getCreateAt());
            // 任务启动时间 (单位: ms)
            taskDetailDto.setStartAt(getTaskDetailResponse.getInformation().getStartAt());
            // 任务结束时间 (单位: ms)
            taskDetailDto.setEndAt(getTaskDetailResponse.getInformation().getEndAt());
            // 任务的状态 (0: 未知; 1: 等在中; 2: 计算中; 3: 失败; 4: 成功)
            taskDetailDto.setState(getTaskDetailResponse.getInformation().getState().getNumber());

            //任务所需资源声明
            TaskResourceCostDeclareDto taskOperationCostDeclareDto = new TaskResourceCostDeclareDto();
            taskOperationCostDeclareDto.setMemory(getTaskDetailResponse.getInformation().getOperationCost().getMemory());
            taskOperationCostDeclareDto.setProcessor(getTaskDetailResponse.getInformation().getOperationCost().getProcessor());
            taskOperationCostDeclareDto.setBandwidth(getTaskDetailResponse.getInformation().getOperationCost().getBandwidth());
            taskOperationCostDeclareDto.setDuration(getTaskDetailResponse.getInformation().getOperationCost().getDuration());
            taskDetailDto.setOperationCost(taskOperationCostDeclareDto);

            // 任务描述 (非必须)
            taskDetailDto.setDesc(getTaskDetailResponse.getInformation().getDesc());

            //拼装最外层信息
            taskDetailResponseDto.setInformation(taskDetailDto);
            taskDetailResponseDtoList.add(taskDetailResponseDto);
        }
        return taskDetailResponseDtoList;
    }

    /**
     * 组装算力提供方响应数据
     *
     * @param taskDetailDto 响应数据对象
     * @param information   底层返回结果信息
     */
    private void assemblyPowerSuppliers(TaskDetailShowDto taskDetailDto, TaskDetailShow information) {
        List<TaskPowerSupplierDto> powerSuppliers = new ArrayList<>();
        TaskPowerSupplierDto powerSupplierDto;
        TaskPowerSupplierShow powerSupplierShow;
        ResourceUsedDetailDto resourceUsedDetailDto;
        for (int j = 0; j < information.getPowerSuppliersCount(); j++) {
            powerSupplierShow = information.getPowerSuppliers(j);
            powerSupplierDto = new TaskPowerSupplierDto();

            resourceUsedDetailDto = new ResourceUsedDetailDto();
            resourceUsedDetailDto.setTotalMem(powerSupplierShow.getPowerInfo().getTotalMem());
            resourceUsedDetailDto.setTotalProcessor(powerSupplierShow.getPowerInfo().getTotalProcessor());
            resourceUsedDetailDto.setTotalBandwidth(powerSupplierShow.getPowerInfo().getTotalBandwidth());
            resourceUsedDetailDto.setTotalDisk(powerSupplierShow.getPowerInfo().getTotalDisk());
            resourceUsedDetailDto.setUsedMem(powerSupplierShow.getPowerInfo().getUsedMem());
            resourceUsedDetailDto.setUsedProcessor(powerSupplierShow.getPowerInfo().getUsedProcessor());
            resourceUsedDetailDto.setUsedBandwidth(powerSupplierShow.getPowerInfo().getUsedBandwidth());
            resourceUsedDetailDto.setUsedDisk(powerSupplierShow.getPowerInfo().getUsedDisk());

            powerSupplierDto.setMemberInfo(getOrganizationIdentityInfoDto(powerSupplierShow.getOrganization()));
            powerSupplierDto.setResourceUsedInfo(resourceUsedDetailDto);
            powerSuppliers.add(powerSupplierDto);
        }
        taskDetailDto.setPowerSuppliers(powerSuppliers);
    }

    /**
     * 组装数据提供方信息
     *
     * @param taskDetailDto 响应数据对象
     * @param information   底层返回结果信息
     */
    private void assemblyDataSuppliers(TaskDetailShowDto taskDetailDto, TaskDetailShow information) {
        List<TaskDataSupplierDto> dataSuppliers = new ArrayList<>();
        TaskDataSupplierDto dataSupplierDto;
        TaskDataSupplierShow dataSupplierShow;
        for (int j = 0; j < information.getDataSuppliersCount(); j++) {
            dataSupplierShow = information.getDataSuppliers(j);
            dataSupplierDto = new TaskDataSupplierDto();
            dataSupplierDto.setMemberInfo(getOrganizationIdentityInfoDto(dataSupplierShow.getOrganization()));
            dataSupplierDto.setMetaDataId(dataSupplierShow.getMetadataId());
            dataSupplierDto.setMetaDataName(dataSupplierShow.getMetadataName());
            dataSuppliers.add(dataSupplierDto);
        }
        taskDetailDto.setDataSuppliers(dataSuppliers);
    }

    /**
     * 查看某个任务的全部事件列表通过单个任务ID
     */
    public List<TaskEventDto> getTaskEventList(ManagedChannel channel, String taskId) {
        GetTaskEventListRequest getTaskEventListRequest = GetTaskEventListRequest.newBuilder().setTaskId(taskId).build();
//        GetTaskEventListResponse taskEventListResponse = taskServiceBlockingStub.getTaskEventList(getTaskEventListRequest);
        GetTaskEventListResponse taskEventListResponse = TaskServiceGrpc.newBlockingStub(channel).getTaskEventList(getTaskEventListRequest);
        return getTaskEventShowDots(taskEventListResponse);
    }

    /**
     * 查看多个任务的全部事件列表
     */
    public List<TaskEventDto> getTaskEventListByTaskIds(ManagedChannel channel, String[] taskIds) {
        GetTaskEventListByTaskIdsRequest.Builder getTaskEventListByTaskIdsRequestBuilder = GetTaskEventListByTaskIdsRequest.newBuilder();
        for (int i = 0; i < taskIds.length; i++) {
            getTaskEventListByTaskIdsRequestBuilder.setTaskIds(i, taskIds[i]);
        }
//        GetTaskEventListResponse taskEventListResponse = taskServiceBlockingStub.getTaskEventListByTaskIds(getTaskEventListByTaskIdsRequestBuilder.build());
        GetTaskEventListResponse taskEventListResponse = TaskServiceGrpc.newBlockingStub(channel).getTaskEventListByTaskIds(getTaskEventListByTaskIdsRequestBuilder.build());
        return getTaskEventShowDots(taskEventListResponse);
    }

    /**
     * 终止任务
     *
     * @return 终止任务响应对象
     */
    public TerminateTaskRespDto terminateTask(TerminateTaskRequestDto requestDto) {
        TerminateTaskRequest terminateReq = TerminateTaskRequest.newBuilder()
                .setUser(requestDto.getUser())
                .setUserTypeValue(requestDto.getUserType())
                .setTaskId(requestDto.getTaskId())
                .setSign(ByteString.copyFromUtf8(requestDto.getSign()))
                .build();
        SimpleResponse simpleResponse = taskServiceBlockingStub.terminateTask(terminateReq);
        TerminateTaskRespDto terminateTaskRespDto = new TerminateTaskRespDto();
        terminateTaskRespDto.setStatus(simpleResponse.getStatus());
        terminateTaskRespDto.setMsg(simpleResponse.getMsg());
        return terminateTaskRespDto;
    }

    private List<TaskEventDto> getTaskEventShowDots(GetTaskEventListResponse taskEventListResponse) {
        if (taskEventListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(taskEventListResponse.getStatus(), taskEventListResponse.getMsg());
        }

        List<TaskEventDto> taskEventShowDtoList = new ArrayList<>();
        TaskEventDto taskEventShowDto;
        for (int i = 0; i < taskEventListResponse.getTaskEventListCount(); i++) {
            taskEventShowDto = new TaskEventDto();
            taskEventShowDto.setType(taskEventListResponse.getTaskEventList(i).getType());
            taskEventShowDto.setTaskId(taskEventListResponse.getTaskEventList(i).getTaskId());
            taskEventShowDto.setPartyId(taskEventListResponse.getTaskEventList(i).getPartyId());

            NodeIdentityDto owner = new NodeIdentityDto();
            owner.setNodeName(taskEventListResponse.getTaskEventList(i).getOwner().getNodeName());
            owner.setNodeId(taskEventListResponse.getTaskEventList(i).getOwner().getNodeId());
            owner.setIdentityId(taskEventListResponse.getTaskEventList(i).getOwner().getIdentityId());
            owner.setStatus(taskEventListResponse.getTaskEventList(i).getOwner().getStatusValue());
            taskEventShowDto.setOwner(owner);

            taskEventShowDto.setPartyId(taskEventListResponse.getTaskEventList(i).getPartyId());
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
        //用户类型 (0: 未定义; 1: 第二地址; 2: 测试网地址; 3: 主网地址)
        publishTaskDeclareRequestBuilder.setUserType(UserType.forNumber(taskDto.getUserType()));
        //任务发起者 组织信息
        TaskOrganization sender = TaskOrganization.newBuilder()
                .setPartyId(taskDto.getSender().getPartyId())
                .setNodeName(taskDto.getSender().getNodeName())
                .setNodeId(taskDto.getSender().getNodeId())
                .setIdentityId(taskDto.getSender().getIdentityId())
                .build();
        publishTaskDeclareRequestBuilder.setSender(sender);
        //任务算法提供方 组织信息
        TaskOrganization algoSupplier = TaskOrganization.newBuilder()
                .setPartyId(taskDto.getAlgoSupplier().getPartyId())
                .setNodeName(taskDto.getAlgoSupplier().getNodeName())
                .setNodeId(taskDto.getAlgoSupplier().getNodeId())
                .setIdentityId(taskDto.getAlgoSupplier().getIdentityId())
                .build();
        publishTaskDeclareRequestBuilder.setAlgoSupplier(algoSupplier);
        // data_supplier
        TaskDataSupplierDeclareDto dataSupplierDeclareDto;
        for (int i = 0; i < taskDto.getTaskDataSupplierDeclareDtoList().size(); i++) {
            dataSupplierDeclareDto = taskDto.getTaskDataSupplierDeclareDtoList().get(i);
            // member_info
            TaskOrganization dataSupplierOrganization = TaskOrganization.newBuilder()
                    .setPartyId(dataSupplierDeclareDto.getTaskOrganizationIdentityInfoDto().getPartyId())
                    .setNodeName(dataSupplierDeclareDto.getTaskOrganizationIdentityInfoDto().getNodeName())
                    .setNodeId(dataSupplierDeclareDto.getTaskOrganizationIdentityInfoDto().getNodeId())
                    .setIdentityId(dataSupplierDeclareDto.getTaskOrganizationIdentityInfoDto().getIdentityId())
                    .build();
            //meta_data_info
            TaskMetadataDeclare.Builder taskMetaDataDeclareBuilder = TaskMetadataDeclare.newBuilder()
                    .setMetadataId(dataSupplierDeclareDto.getTaskMetaDataDeclareDto().getMetaDataId())
                    .setKeyColumn(dataSupplierDeclareDto.getTaskMetaDataDeclareDto().getKeyColumn());
            if (null != dataSupplierDeclareDto.getTaskMetaDataDeclareDto().getSelectedColumns()) {
                for (int j = 0; j < dataSupplierDeclareDto.getTaskMetaDataDeclareDto().getSelectedColumns().size(); j++) {
                    taskMetaDataDeclareBuilder.addSelectedColumns(dataSupplierDeclareDto.getTaskMetaDataDeclareDto().getSelectedColumns().get(j));
                }
            }

            TaskDataSupplierDeclare taskDataSupplierDeclare = TaskDataSupplierDeclare.newBuilder()
                    .setOrganization(dataSupplierOrganization)
                    .setMetadataInfo(taskMetaDataDeclareBuilder.build())
                    .build();

            publishTaskDeclareRequestBuilder.addDataSuppliers(taskDataSupplierDeclare);
        }
        //power_party_ids
        for (int i = 0; i < taskDto.getPowerPartyIds().size(); i++) {
            publishTaskDeclareRequestBuilder.addPowerPartyIds(taskDto.getPowerPartyIds().get(i));
        }
        //receivers
        TaskOrganization receiver;
        OrganizationIdentityInfoDto receiverDto;
        for (int i = 0; i < taskDto.getTaskResultReceiverDeclareDtoList().size(); i++) {
            receiverDto = taskDto.getTaskResultReceiverDeclareDtoList().get(i);
            receiver = TaskOrganization.newBuilder()
                    .setPartyId(receiverDto.getPartyId())
                    .setNodeName(receiverDto.getNodeName())
                    .setNodeId(receiverDto.getNodeId())
                    .setIdentityId(receiverDto.getIdentityId())
                    .build();
            publishTaskDeclareRequestBuilder.addReceivers(receiver);
        }
        //任务所需资源声明
        TaskResourceCostDeclare resourceCostBuilder = TaskResourceCostDeclare.newBuilder()
                .setMemory(taskDto.getResourceCostDeclareDto().getMemory())
                .setProcessor(taskDto.getResourceCostDeclareDto().getProcessor())
                .setBandwidth(taskDto.getResourceCostDeclareDto().getBandwidth())
                .setDuration(taskDto.getResourceCostDeclareDto().getDuration())
                .build();
        publishTaskDeclareRequestBuilder.setOperationCost(resourceCostBuilder);
        //  计算合约代码
        publishTaskDeclareRequestBuilder.setCalculateContractCode(taskDto.getCalculateContractCode());
        //  数据分片合约代码
        if (null != taskDto.getDataSplitContractCode()) {
            publishTaskDeclareRequestBuilder.setDataSplitContractCode(taskDto.getDataSplitContractCode());
        }
        //  合约调用的额外可变入参 (json 字符串, 根据算法来)
        publishTaskDeclareRequestBuilder.setContractExtraParams(taskDto.getContractExtraParams());
        //发起任务的账户的签名
        publishTaskDeclareRequestBuilder.setSign(ByteString.copyFromUtf8(taskDto.getSign()));
        //任务描述 (非必须)
        publishTaskDeclareRequestBuilder.setDesc(taskDto.getDesc());
        log.info("发布任务,任务名称{}请求数据>>>{}", taskDto.getTaskName(), publishTaskDeclareRequestBuilder.build());
        return publishTaskDeclareRequestBuilder.build();
    }

    /**
     * 转换组织信息
     *
     * @param org 原组织信息
     * @return 转换后的dto对象
     */
    private OrganizationIdentityInfoDto getOrganizationIdentityInfoDto(TaskOrganization org) {
        OrganizationIdentityInfoDto owner = new OrganizationIdentityInfoDto();
        owner.setPartyId(org.getPartyId());
        owner.setNodeName(org.getNodeName());
        owner.setNodeId(org.getNodeId());
        owner.setIdentityId(org.getIdentityId());
        return owner;
    }
}
