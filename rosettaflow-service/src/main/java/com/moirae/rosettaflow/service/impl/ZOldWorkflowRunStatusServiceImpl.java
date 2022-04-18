//package com.moirae.rosettaflow.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.moirae.rosettaflow.common.constants.AlgorithmConstant;
//import com.moirae.rosettaflow.common.constants.SysConstant;
//import com.moirae.rosettaflow.common.enums.*;
//import com.moirae.rosettaflow.common.exception.AppException;
//import com.moirae.rosettaflow.common.utils.JsonUtils;
//import com.moirae.rosettaflow.dto.WorkflowDto;
//import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
//import com.moirae.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
//import com.moirae.rosettaflow.grpc.service.GrpcSysService;
//import com.moirae.rosettaflow.grpc.client.GrpcTaskServiceClient;
//import com.moirae.rosettaflow.grpc.sys.resp.dto.GetTaskResultFileSummaryResponseDto;
//import com.moirae.rosettaflow.grpc.task.req.dto.*;
//import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
//import com.moirae.rosettaflow.mapper.ZOldWorkflowRunStatusMapper;
//import com.moirae.rosettaflow.mapper.domain.*;
//import com.moirae.rosettaflow.service.*;
//import io.grpc.ManagedChannel;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//public class ZOldWorkflowRunStatusServiceImpl extends ServiceImpl<ZOldWorkflowRunStatusMapper, ZOldWorkflowRunStatus> implements ZOldIWorkflowRunStatusService {
//
//    @Resource
//    private ZOldIWorkflowService workflowService;
//    @Resource
//    private ZOldIWorkflowNodeService workflowNodeService;
//    @Resource
//    private ZOldIWorkflowRunTaskStatusService workflowRunTaskStatusService;
//    @Resource
//    private ZOldIWorkflowRunTaskResultService workflowRunTaskResultService;
//    @Resource
//    private GrpcTaskServiceClient grpcTaskService;
//    @Resource
//    private AlgService algService;
//    @Resource
//    private OrgService organizationService;
//    @Resource
//    private ModelService modelService;
//    @Resource
//    private GrpcSysService grpcSysService;
//    @Resource
//    private DataService dataService;
//
//    @Override
//    public TaskDto assemblyTaskDto(WorkflowDto workflowDto) {
//        return null;
//    }
//
//    @Override
//    public ZOldWorkflowRunStatus submitTaskAndExecute(Long workflowId, Integer version, String address, String sign) {
//        // 加载工作流设置信息
//        ZOldWorkflow workflow = workflowService.queryWorkflowDetail(workflowId, version);
//        // 配置启动校验(检验保存时未校验的)
//        checkBeforeStart(workflow);
//        // 生成执行记录
//        ZOldWorkflowRunStatus workflowRunStatus = createAndSaveWorkflowRunStatus(workflow, address, sign);
//        List<ZOldWorkflowRunTaskStatus> workflowRunTaskStatusList = createAndSaveWorkflowRunTaskStatus(workflow, workflowRunStatus);
//        // 设置关联字段
//        workflow.setWorkflowNodeMap(workflow.getWorkflowNodeReqList().stream().collect(Collectors.toMap(ZOldWorkflowNode::getNodeStep, item -> item)));
//        workflowRunStatus.setWorkflowRunTaskStatusMap(workflowRunTaskStatusList.stream().collect(Collectors.toMap(ZOldWorkflowRunTaskStatus::getNodeStep, item -> item)));
//        workflowRunStatus.setWorkflow(workflow);
//        // 发起任务
//        executeTask(workflowRunStatus);
//        return workflowRunStatus;
//    }
//
//    @Override
//    public List<ZOldWorkflowRunTaskStatus> queryUnConfirmedWorkflowRunTaskStatus() {
//        return workflowRunTaskStatusService.queryUnConfirmedWorkflowRunTaskStatus();
//    }
//
//    @Override
//    @Transactional(rollbackFor = RuntimeException.class)
//    public void taskFinish(Long workflowRunStatusId, String taskId, int state, long taskStartAt, long taskEndAt) {
//        if (state == TaskRunningStatusEnum.SUCCESS.getValue() || state == TaskRunningStatusEnum.FAIL.getValue()) {
//            // 加载工作量运行信息
//            ZOldWorkflowRunStatus workflowRunStatus = queryWorkflowRunStatusDetail(workflowRunStatusId);
//            // 如果任务成功
//            if(state == TaskRunningStatusEnum.SUCCESS.getValue()){
//                taskSuccess(workflowRunStatus, taskId, taskStartAt, taskEndAt);
//                if(workflowRunStatus.getCurStep().compareTo(workflowRunStatus.getStep()) < 0){
//                    workflowRunStatus.setCurStep(workflowRunStatus.getCurStep() + 1);
//                    executeTask(workflowRunStatus);
//                }
//            }
//            // 如果任务成功
//            if(state == TaskRunningStatusEnum.FAIL.getValue()){
//                taskFail(workflowRunStatus, taskId, taskStartAt, taskEndAt);
//            }
//        }
//    }
//
//    @Override
//    public void updateCancelStatus(Long workflowRunStatusId, byte value) {
//        ZOldWorkflowRunStatus workflowRunStatus = getById(workflowRunStatusId);
//        workflowRunStatus.setCancelStatus(value);
//        updateById(workflowRunStatus);
//    }
//
//    @Override
//    public boolean cancel(ZOldWorkflowRunTaskStatus workflowRunTaskStatus) {
//        ZOldWorkflowRunStatus workflowRunStatus = getById(workflowRunTaskStatus.getWorkflowRunId());
//        if(workflowRunStatus.getCancelStatus() !=null && WorkflowRunStatusEnum.RUNNING.getValue() == workflowRunStatus.getCancelStatus()){
//            ZOldWorkflowNode workflowNode = workflowNodeService.getById(workflowRunTaskStatus.getWorkflowNodeId());
//            TerminateTaskRequestDto terminateTaskRequestDto = assemblyTerminateTaskRequestDto(workflowRunStatus, workflowRunTaskStatus.getTaskId());
//            try {
//                TerminateTaskRespDto terminateTaskRespDto = grpcTaskService.terminateTask(organizationService.getChannel(workflowNode.getSenderIdentityId()), terminateTaskRequestDto);
//                log.info("终止工作流返回， terminateTaskRespDto = {}", terminateTaskRespDto);
//                if (terminateTaskRespDto != null && terminateTaskRespDto.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
//                    workflowRunStatus.setCancelStatus(WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
//                    updateById(workflowRunStatus);
//                }else{
//                    log.error("终止工作流失败，失败原因! 原因 = {}", terminateTaskRespDto);
//                    workflowRunStatus.setCancelStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//                    updateById(workflowRunStatus);
//                }
//            } catch (Exception e) {
//                log.error("终止工作流失败，失败原因!", e);
//                workflowRunStatus.setCancelStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//                updateById(workflowRunStatus);
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public IPage<ZOldWorkflowRunStatus> runningRecordList(Long userId, Long projectId, String workflowName, IPage<ZOldWorkflowRunStatus> page) {
//        return this.baseMapper.runningRecordList(userId, projectId, workflowName, page);
//    }
//
//    public TerminateTaskRequestDto assemblyTerminateTaskRequestDto(ZOldWorkflowRunStatus workflowRunStatus, String taskId) {
//        TerminateTaskRequestDto terminateTaskRequestDto = new TerminateTaskRequestDto();
//        terminateTaskRequestDto.setUser(workflowRunStatus.getAddress());
//        terminateTaskRequestDto.setUserType(UserTypeEnum.checkUserType(workflowRunStatus.getAddress()));
//        terminateTaskRequestDto.setTaskId(taskId);
//        terminateTaskRequestDto.setSign(workflowRunStatus.getSign());
//        return terminateTaskRequestDto;
//    }
//
//    private void taskSuccess(ZOldWorkflowRunStatus workflowRunStatus, String taskId, long taskStartAt, long taskEndAt) {
//        ZOldWorkflowRunTaskStatus curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusMap().get(workflowRunStatus.getCurStep());
//        ZOldWorkflowNode workflowNode = workflowRunStatus.getWorkflow().getWorkflowNodeMap().get(workflowRunStatus.getCurStep());
//
//        if(!taskId.equals(curWorkflowRunTaskStatus.getTaskId())){
//            log.error("工作流状态错误！ workflowRunStatusId = {}  task = {}", workflowRunStatus.getId(), taskId);
//            return;
//        }
//        // 更新状态
//        Date begin = taskStartAt > 0 ? new Date(taskStartAt) : null;
//        Date end = taskEndAt > 0 ? new Date(taskEndAt) : null;
//        curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
//        curWorkflowRunTaskStatus.setBeginTime(begin);
//        curWorkflowRunTaskStatus.setEndTime(end);
//        workflowRunTaskStatusService.updateById(curWorkflowRunTaskStatus);
//
//        // 最后一个任务
//        if(workflowRunStatus.getCurStep().compareTo(workflowRunStatus.getStep()) == 0){
//            workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
//            workflowRunStatus.setEndTime(new Date());
//            updateById(workflowRunStatus);
//        }
//
//         // 结果文件
//        Set<String> identityIdSet = workflowNode.getWorkflowNodeOutputReqList().stream().map(ZOldWorkflowNodeOutput::getIdentityId).collect(Collectors.toSet());
//        Algorithm algorithm = algorithmService.getAlgorithmById(workflowNode.getAlgorithmId());
//
//        List<ZOldWorkflowRunTaskResult> taskResultList = new ArrayList<>();
//        List<Model> modelList = new ArrayList<>();
//        for (String identityId : identityIdSet) {
//            ManagedChannel channel = organizationService.getChannel(identityId);
//            GetTaskResultFileSummaryResponseDto taskResultResponseDto = grpcSysService.getTaskResultFileSummary(channel, taskId);
//            if (Objects.isNull(taskResultResponseDto)) {
//                log.error("WorkflowNodeStatusMockTask获取任务结果失败！ info = {}", taskResultResponseDto);
//                return;
//            }
//            // 处理结果
//            ZOldWorkflowRunTaskResult taskResult = BeanUtil.copyProperties(taskResultResponseDto, ZOldWorkflowRunTaskResult.class);
//            taskResult.setIdentityId(identityId);
//            taskResultList.add(taskResult);
//            // 处理模型
//            if(OutputModelEnum.NEED.getValue() == algorithm.getOutputModel()){
//                Algorithm inputAlgorithm = algorithmService.getAlgorithmByIdCode(algorithm.getAlgorithmCode(), InputModelEnum.NEED.getValue());
//                Model model = new Model();
//                model.setOrgIdentityId(identityId);
//                model.setName(algorithm.getAlgorithmName()+"(" + taskId + ")");
//                model.setMetaDataId(taskResult.getMetadataId());
//                model.setFileId(taskResult.getOriginId());
//                model.setFilePath(taskResult.getFilePath());
//                model.setTrainTaskId(taskId);
//                model.setTrainAlgorithmId(workflowNode.getAlgorithmId());
//                model.setTrainUserAddress(workflowRunStatus.getAddress());
//                model.setSupportedAlgorithmId(inputAlgorithm.getId());
//                modelService.save(model);
//                modelList.add(model);
//            }
//        }
//        if(taskResultList.size() > 0){
//            workflowRunTaskResultService.saveBatch(taskResultList);
//        }
//        if(modelList.size() > 0 && workflowRunStatus.getStep() > workflowRunStatus.getCurStep()){
//            // 为下个任务设置模型id
//            ZOldWorkflowRunTaskStatus nextWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusMap().get(workflowRunStatus.getCurStep()+1);
//            ZOldWorkflowNode nextWorkflowNode = workflowRunStatus.getWorkflow().getWorkflowNodeMap().get(workflowRunStatus.getCurStep() + 1);
//            if(nextWorkflowNode.getInputModel() == SysConstant.INT_1 && nextWorkflowNode.getModelId() == 0){
//                Model model = modelList.stream().filter(item -> nextWorkflowNode.getSenderIdentityId().equals(item.getOrgIdentityId())).findFirst().get();
//                nextWorkflowRunTaskStatus.setModelId(model.getId());
//            }
//        }
//    }
//
//    private void taskFail(ZOldWorkflowRunStatus workflowRunStatus, String taskId, long taskStartAt, long taskEndAt) {
//        ZOldWorkflowRunTaskStatus curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusMap().get(workflowRunStatus.getCurStep());
//
//        if(!taskId.equals(curWorkflowRunTaskStatus.getTaskId())){
//            log.error("工作流状态错误！ workflowRunStatusId = {}  task = {}", workflowRunStatus.getId(), taskId);
//        }
//        // 更新状态
//        Date begin = taskStartAt > 0 ? new Date(taskStartAt) : null;
//        Date end = taskEndAt > 0 ? new Date(taskEndAt) : null;
//        curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//        curWorkflowRunTaskStatus.setRunMsg("task fail!");
//        curWorkflowRunTaskStatus.setBeginTime(begin);
//        curWorkflowRunTaskStatus.setEndTime(end);
//        workflowRunTaskStatusService.updateById(curWorkflowRunTaskStatus);
//
//        workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//        workflowRunStatus.setEndTime(new Date());
//        updateById(workflowRunStatus);
//    }
//
//    private ZOldWorkflowRunStatus queryWorkflowRunStatusDetail(Long workflowRunStatusId) {
//        ZOldWorkflowRunStatus workflowRunStatus = getById(workflowRunStatusId);
//        List<ZOldWorkflowRunTaskStatus> workflowRunTaskStatusList = workflowRunTaskStatusService.listByWorkflowRunStatusId(workflowRunStatusId);
//        // 加载工作流设置信息
//        ZOldWorkflow workflow = workflowService.queryWorkflowDetail(workflowRunStatus.getWorkflowId(), workflowRunStatus.getWorkflowEditVersion());
//        // 设置关联字段
//        workflow.setWorkflowNodeMap(workflow.getWorkflowNodeReqList().stream().collect(Collectors.toMap(ZOldWorkflowNode::getNodeStep, item -> item)));
//        workflowRunStatus.setWorkflowRunTaskStatusMap(workflowRunTaskStatusList.stream().collect(Collectors.toMap(ZOldWorkflowRunTaskStatus::getNodeStep, item -> item)));
//        workflowRunStatus.setWorkflow(workflow);
//        return workflowRunStatus;
//    }
//
//    private void checkBeforeStart(ZOldWorkflow workflow) {
//        workflow.getWorkflowNodeReqList().forEach(item ->{
//            // 输入节点校验
//            if (null == item.getWorkflowNodeInputReqList() || item.getWorkflowNodeInputReqList().size() == 0) {
//                throw new AppException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_INPUT_EXIST.getMsg());
//            }
//            // 输出节点校验
//            if (null == item.getWorkflowNodeOutputReqList() || item.getWorkflowNodeOutputReqList().size() == 0) {
//                throw new AppException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_OUTPUT_EXIST.getMsg());
//            }
//            // 组织校验(任务发起发方组织、算法提供方组织、数据提供方组织、结果接收方组织)
//            if(!organizationService.isEffective(item.getSenderIdentityId())){
//                throw new AppException(RespCodeEnum.BIZ_FAILED, StrUtil.format(ErrorMsg.ORGANIZATION_UNAVAILABLE_SENDER.getMsg(),item.getSenderIdentityId()) );
//            }
//            item.getWorkflowNodeInputReqList().stream().forEach(subItem ->{
//                if(!organizationService.isEffective(subItem.getIdentityId())){
//                    throw new AppException(RespCodeEnum.BIZ_FAILED, StrUtil.format(ErrorMsg.ORGANIZATION_UNAVAILABLE_DATA_PROVIDED.getMsg(), subItem.getIdentityId()));
//                }
//            });
//            item.getWorkflowNodeOutputReqList().stream().forEach(subItem ->{
//                if(!organizationService.isEffective(subItem.getIdentityId())){
//                    throw new AppException(RespCodeEnum.BIZ_FAILED, StrUtil.format(ErrorMsg.ORGANIZATION_UNAVAILABLE_OUTPUT.getMsg(), subItem.getIdentityId()));
//                }
//            });
//
//            // 元数据状态校验
//            item.getWorkflowNodeInputReqList().stream().forEach(subItem ->{
//                dataService.checkMetaDataEffective(subItem.getDataTableId());
//            });
//        });
//    }
//
//    private void executeTask(ZOldWorkflowRunStatus workflowRunStatus){
//        ZOldWorkflowRunTaskStatus curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusMap().get(workflowRunStatus.getCurStep());
//
//        if(curWorkflowRunTaskStatus.getRunStatus() == WorkflowRunStatusEnum.UN_RUN.getValue()){
//            curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUNNING.getValue());
//            curWorkflowRunTaskStatus.setBeginTime(new Date());
//            // 提交任务到 Net
//            TaskDto taskDto = assemblyTaskDto(workflowRunStatus);
//            PublishTaskDeclareResponseDto respDto;
//            try {
//                respDto = grpcTaskService.syncPublishTask(organizationService.getChannel(taskDto.getSender().getIdentityId()), taskDto);
//                curWorkflowRunTaskStatus.setTaskId(respDto.getTaskId());
//                curWorkflowRunTaskStatus.setRunMsg(respDto.getMsg());
//                if (GrpcConstant.GRPC_SUCCESS_CODE != respDto.getStatus()) {
//                    curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//                    curWorkflowRunTaskStatus.setEndTime(new Date());
//                    workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//                    workflowRunStatus.setEndTime(new Date());
//                }
//                log.info("任务发布结果>>>>工作流id:{},任务名称：{},rosettanet收到处理任务，返回的taskId：{}", taskDto.getWorkFlowNodeId(), taskDto.getTaskName(), respDto.getTaskId());
//            } catch (Exception e){
//                log.error("executeTask error! runStatusId = {}  runTaskStatusId = {}", curWorkflowRunTaskStatus.getId());
//                log.error("executeTask error! ", e);
//                curWorkflowRunTaskStatus.setRunMsg(e.getMessage());
//                curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//                curWorkflowRunTaskStatus.setEndTime(new Date());
//                workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
//                workflowRunStatus.setEndTime(new Date());
//            }
//            updateById(workflowRunStatus);
//            workflowRunTaskStatusService.updateById(curWorkflowRunTaskStatus);
//        }
//    }
//
//    private TaskDto assemblyTaskDto(ZOldWorkflowRunStatus workflowRunStatus) {
//
//        ZOldWorkflowNode curlWorkflowNode = workflowRunStatus.getWorkflow().getWorkflowNodeMap().get(workflowRunStatus.getCurStep());
//
//        //有指定当前工作流节点模型输入,获取工作流节点模型
//        if(curlWorkflowNode.getInputModel() == SysConstant.INT_1){
//            Model model = modelService.getById(workflowRunStatus.getWorkflowRunTaskStatusMap().get(workflowRunStatus.getCurStep()).getModelId());
//            if (model == null) {
//                log.error("ZOldWorkflowServiceImpl->getDataSupplierList,fail reason:{}", ErrorMsg.WORKFLOW_NODE_TASK_RESULT_NOT_EXIST.getMsg());
//                throw new AppException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_TASK_RESULT_NOT_EXIST.getMsg());
//            }
//            curlWorkflowNode.setModel(model);
//        }
//
//        //获取参与机构列表(发送者和数据提供者)
//        Set<String> inputOrgIdSet = curlWorkflowNode.getWorkflowNodeInputReqList().stream()
//                .map(ZOldWorkflowNodeInput::getIdentityId)
//                .collect(Collectors.toSet());
//        Set<String> outputOrgIdSet = curlWorkflowNode.getWorkflowNodeOutputReqList().stream()
//                .map(ZOldWorkflowNodeOutput::getIdentityId)
//                .collect(Collectors.toSet());
//        inputOrgIdSet.add(curlWorkflowNode.getSenderIdentityId());
//        inputOrgIdSet.addAll(outputOrgIdSet);
//
//        curlWorkflowNode.setOrganizationMap(organizationService.getOrgListByIdentityIdList(inputOrgIdSet).stream().collect(Collectors.toMap(Org::getIdentityId, organization -> organization)));
//
//        // 拼接任务
//        TaskDto taskDto = new TaskDto();
//        taskDto.setWorkFlowNodeId(curlWorkflowNode.getId());
//        //任务名称
//        taskDto.setTaskName(generateTaskName(workflowRunStatus));
//        //发起任务用户
//        taskDto.setUser(workflowRunStatus.getAddress());
//        //发起账户用户类型
//        taskDto.setUserType(UserTypeEnum.checkUserType(workflowRunStatus.getAddress()));
//        //设置发起方
//        taskDto.setSender(senderOrganization(curlWorkflowNode.getOrganizationMap().get(curlWorkflowNode.getSenderIdentityId())));
//        //设置算法提供方
//        taskDto.setAlgoSupplier(algoSupplierOrganization(curlWorkflowNode.getOrganizationMap().get(curlWorkflowNode.getSenderIdentityId())));
//        //算力提供方
//        taskDto.setPowerPartyIds(getPowerPartyIds());
//        //数据提供方
//        taskDto.setTaskDataSupplierDeclareDtoList(getDataSupplierList(curlWorkflowNode, curlWorkflowNode.getModel()));
//        //任务结果接受者
//        taskDto.setTaskResultReceiverDeclareDtoList(getReceivers(curlWorkflowNode));
//        // 任务需要花费的资源声明
//        taskDto.setResourceCostDeclareDto(getResourceCostDeclare(curlWorkflowNode));
//        //算法代码
//        taskDto.setCalculateContractCode(curlWorkflowNode.getWorkflowNodeCodeReq().getCalculateContractCode());
//        //数据分片合约代码
//        taskDto.setDataSplitContractCode(curlWorkflowNode.getWorkflowNodeCodeReq().getDataSplitContractCode());
//        //合n约调用的额外可变入参 (jso 字符串, 根据算法来)
//        taskDto.setContractExtraParams(getContractExtraParams(curlWorkflowNode));
//        //发起任务的账户的签名
//        taskDto.setSign(workflowRunStatus.getSign());
//        //任务描述 (非必须)
//        taskDto.setDesc(curlWorkflowNode.getNodeName());
//
//        return taskDto;
//    }
//
//    /**
//     * 生成任务名称
//     * {内部id}_{用户地址}_{算法名称}_{工作流名称}
//     * @param workflowRunStatus
//     * @return
//     */
//    private String generateTaskName(ZOldWorkflowRunStatus workflowRunStatus) {
//        Long id = workflowRunStatus.getWorkflowRunTaskStatusMap().get(workflowRunStatus.getCurStep()).getId();
//        String address = workflowRunStatus.getAddress();
//        String algorithmName = workflowRunStatus.getWorkflow().getWorkflowNodeMap().get(workflowRunStatus.getCurStep()).getNodeName();
//        String workflowName = workflowRunStatus.getWorkflow().getWorkflowName();
//        return StringUtils.abbreviate(id+ "_" + address + "_" + algorithmName + "_" + workflowName, 100);
//    }
//
//    private List<ZOldWorkflowRunTaskStatus> createAndSaveWorkflowRunTaskStatus(ZOldWorkflow workflow, ZOldWorkflowRunStatus workflowRunStatus) {
//        return workflow.getWorkflowNodeReqList().stream()
//            .map(item -> {
//                ZOldWorkflowRunTaskStatus workflowRunTaskStatus = new ZOldWorkflowRunTaskStatus();
//                workflowRunTaskStatus.setWorkflowRunId(workflowRunStatus.getId());
//                workflowRunTaskStatus.setWorkflowNodeId(item.getId());
//                workflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
//                workflowRunTaskStatus.setNodeStep(item.getNodeStep());
//                workflowRunTaskStatus.setModelId(item.getModelId());
//                workflowRunTaskStatusService.save(workflowRunTaskStatus);
//                return workflowRunTaskStatus;
//            })
//            .collect(Collectors.toList());
//    }
//
//    private ZOldWorkflowRunStatus createAndSaveWorkflowRunStatus(ZOldWorkflow workflow, String address, String sign){
//        ZOldWorkflowRunStatus workflowRunStatus = new ZOldWorkflowRunStatus();
//        workflowRunStatus.setWorkflowId(workflow.getId());
//        workflowRunStatus.setWorkflowEditVersion(workflow.getEditVersion());
//        workflowRunStatus.setSign(sign);
//        workflowRunStatus.setCurStep(1);
//        workflowRunStatus.setStep(workflow.getWorkflowNodeReqList().size());
//        workflowRunStatus.setAddress(address);
//        workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUNNING.getValue());
//        save(workflowRunStatus);
//        return workflowRunStatus;
//    }
//
//
//    private OrganizationIdentityInfoDto senderOrganization(Org organization){
//        OrganizationIdentityInfoDto sender = new OrganizationIdentityInfoDto();
//        sender.setPartyId("s0");
//        sender.setNodeName(organization.getNodeName());
//        sender.setNodeId(organization.getNodeId());
//        sender.setIdentityId(organization.getIdentityId());
//        return sender;
//    }
//
//    private OrganizationIdentityInfoDto algoSupplierOrganization(Org organization) {
//        OrganizationIdentityInfoDto algoSupplier = new OrganizationIdentityInfoDto();
//        algoSupplier.setPartyId("A0");
//        algoSupplier.setNodeName(organization.getNodeName());
//        algoSupplier.setNodeId(organization.getNodeId());
//        algoSupplier.setIdentityId(organization.getIdentityId());
//        return algoSupplier;
//    }
//
//    private OrganizationIdentityInfoDto inputModelOrganization(Org organization, int partNumber){
//        OrganizationIdentityInfoDto org = new OrganizationIdentityInfoDto();
//        org.setPartyId("p" + partNumber);
//        org.setNodeName(organization.getNodeName());
//        org.setNodeId(organization.getNodeId());
//        org.setIdentityId(organization.getIdentityId());
//        return org;
//    }
//
//    private OrganizationIdentityInfoDto inputDataOrganization(Org organization, ZOldWorkflowNodeInput input){
//        OrganizationIdentityInfoDto org = new OrganizationIdentityInfoDto();
//        org.setPartyId(input.getPartyId());
//        org.setNodeName(organization.getNodeName());
//        org.setNodeId(organization.getNodeId());
//        org.setIdentityId(organization.getIdentityId());
//        return org;
//    }
//
//    private OrganizationIdentityInfoDto outputDataOrganization(Org organization, ZOldWorkflowNodeOutput output){
//        OrganizationIdentityInfoDto org = new OrganizationIdentityInfoDto();
//        org.setPartyId(output.getPartyId());
//        org.setNodeName(organization.getNodeName());
//        org.setNodeId(organization.getNodeId());
//        org.setIdentityId(organization.getIdentityId());
//        return org;
//    }
//
//    /**
//     * 获取算力提供方列表
//     *
//     * @return 算力提供方列表
//     */
//    private List<String> getPowerPartyIds() {
//        return new ArrayList<>(Arrays.asList("y1", "y2", "y3"));
//    }
//
//    private List<TaskDataSupplierDeclareDto> getDataSupplierList(ZOldWorkflowNode curlWorkflowNode, Model model) {
//        List<TaskDataSupplierDeclareDto> taskDataSupplierDeclareDtoList = new ArrayList<>();
//        // 数据提供
//        for (ZOldWorkflowNodeInput input : curlWorkflowNode.getWorkflowNodeInputReqList()) {
//            TaskDataSupplierDeclareDto taskDataSupplierDeclareDto = new TaskDataSupplierDeclareDto();
//            // 设置组织
//            taskDataSupplierDeclareDto.setTaskOrganizationIdentityInfoDto( inputDataOrganization(curlWorkflowNode.getOrganizationMap().get(input.getIdentityId()), input));
//
//            // 设置数据
//            TaskMetaDataDeclareDto taskMetaDataDeclareDto = new TaskMetaDataDeclareDto();
//            taskMetaDataDeclareDto.setMetaDataId(input.getDataTableId());
//
//            String[] columnIdsArr = input.getDataColumnIds().split(",");
//            if (columnIdsArr.length < 1) {
//                log.error("ZOldWorkflowServiceImpl->getDataSupplierList 获取当前工作流节点索引列不存在");
//                throw new AppException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_INDEX_COLUMN_NOT_EXIST.getMsg());
//            }
//            List<Integer> columnIndexList = Arrays.stream(columnIdsArr).map(Integer::valueOf).collect(Collectors.toList());
//
//            taskMetaDataDeclareDto.setKeyColumn(input.getKeyColumn().intValue());
//
//            List<Integer> selectedColumns = new ArrayList<>(columnIndexList);
//            taskMetaDataDeclareDto.setSelectedColumns(selectedColumns);
//
//            taskDataSupplierDeclareDto.setTaskMetaDataDeclareDto(taskMetaDataDeclareDto);
//            taskDataSupplierDeclareDtoList.add(taskDataSupplierDeclareDto);
//        }
//
//        // 模型提供
//        if(!Objects.isNull(model)){
//            TaskDataSupplierDeclareDto taskDataSupplierDeclareDto = new TaskDataSupplierDeclareDto();
//            // 设置组织
//            taskDataSupplierDeclareDto.setTaskOrganizationIdentityInfoDto(inputModelOrganization(curlWorkflowNode.getOrganizationMap().get(model.getOrgIdentityId()), curlWorkflowNode.getWorkflowNodeInputReqList().size()));
//            // 设置数据
//            TaskMetaDataDeclareDto taskMetaDataDeclareDto = new TaskMetaDataDeclareDto();
//            taskMetaDataDeclareDto.setMetaDataId(model.getMetaDataId());
//            taskMetaDataDeclareDto.setKeyColumn(0);
//            taskDataSupplierDeclareDto.setTaskMetaDataDeclareDto(taskMetaDataDeclareDto);
//            taskDataSupplierDeclareDtoList.add(taskDataSupplierDeclareDto);
//        }
//        return taskDataSupplierDeclareDtoList;
//    }
//
//    /**
//     * 获取任务结果接收方
//     */
//    private List<OrganizationIdentityInfoDto> getReceivers(ZOldWorkflowNode curlWorkflowNode) {
//        List<OrganizationIdentityInfoDto> receiverList = new ArrayList<>();
//        for (ZOldWorkflowNodeOutput output : curlWorkflowNode.getWorkflowNodeOutputReqList()) {
//            receiverList.add(outputDataOrganization(curlWorkflowNode.getOrganizationMap().get(output.getIdentityId()), output));
//        }
//        return receiverList;
//    }
//
//    /**
//     * 获取任务所需要的资源声明
//     */
//    private TaskResourceCostDeclareDto getResourceCostDeclare(ZOldWorkflowNode curlWorkflowNode) {
//        TaskResourceCostDeclareDto taskResourceCostDeclareDto = new TaskResourceCostDeclareDto();
//        taskResourceCostDeclareDto.setMemory(curlWorkflowNode.getWorkflowNodeResourceReq().getCostMem());
//        taskResourceCostDeclareDto.setProcessor(curlWorkflowNode.getWorkflowNodeResourceReq().getCostCpu());
//        taskResourceCostDeclareDto.setBandwidth(curlWorkflowNode.getWorkflowNodeResourceReq().getCostBandwidth());
//        taskResourceCostDeclareDto.setDuration(curlWorkflowNode.getWorkflowNodeResourceReq().getRunTime());
//        return taskResourceCostDeclareDto;
//
//    }
//
//    private String getContractExtraParams(ZOldWorkflowNode curlWorkflowNode) {
//        AlgorithmVariableStruct jsonStruct = algorithmVariableStructService.getByAlgorithmId(curlWorkflowNode.getAlgorithmId());
//        if (null == jsonStruct) {
//            return null;
//        } else {
//            String struct = jsonStruct.getStruct();
//            // 把可变参数进行替换
//            log.info("jsonStruct.getStruct() is:{}", struct);
//            if (!JsonUtils.isJson(struct)) {
//                log.error("ZOldWorkflowServiceImpl->getContractExtraParams,{}", ErrorMsg.ALG_VARIABLE_STRUCT_ERROR.getMsg());
//                throw new AppException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_VARIABLE_STRUCT_ERROR.getMsg());
//            }
//            JSONObject jsonObject = JSON.parseObject(struct);
//            //逻辑训练动态参数[因变量(标签)]
//            if (jsonObject.containsKey(AlgorithmConstant.LABEL_COLUMN)) {
//                for (ZOldWorkflowNodeInput input : curlWorkflowNode.getWorkflowNodeInputReqList()) {
//                    if (input.getDependentVariable() != null) {
//                        jsonObject.put("label_column", dataService.getByKey(input.getDataTableId(), input.getDependentVariable().intValue()).getColumnName());
//                    }
//                }
//            }
//            if (jsonObject.containsKey(AlgorithmConstant.MODEL_RESTORE_PARTY)) {
//                jsonObject.put("model_restore_party", "p" + curlWorkflowNode.getWorkflowNodeInputReqList().size());
//            }
//            //逻辑回归动态参数[模型所在的路径，需填绝对路径]
//            if (jsonObject.containsKey(AlgorithmConstant.MODEL_PATH)) {
//                if (curlWorkflowNode.getModel() == null || curlWorkflowNode.getModel().getFilePath() == null) {
//                    log.error("启动工作流失败,未指定当前工作流节点的模型输入路径");
//                    throw new AppException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_MODEL_NOT_EXIST.getMsg());
//                }
//                jsonObject.put("model_path", curlWorkflowNode.getModel().getFilePath());
//            }
//            return jsonObject.toJSONString();
//        }
//    }
//}
