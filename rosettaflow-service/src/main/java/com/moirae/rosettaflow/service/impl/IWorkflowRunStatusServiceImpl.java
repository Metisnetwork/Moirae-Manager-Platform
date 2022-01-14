package com.moirae.rosettaflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.AlgorithmConstant;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.JsonUtils;
import com.moirae.rosettaflow.dto.NodeMetaDataDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDataSupplierDeclareDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskMetaDataDeclareDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskResourceCostDeclareDto;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import com.moirae.rosettaflow.mapper.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.*;
import com.zengtengpeng.operation.RedissonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IWorkflowRunStatusServiceImpl extends ServiceImpl<WorkflowRunStatusMapper, WorkflowRunStatus> implements IWorkflowRunStatusService {

    @Resource
    private SysConfig sysConfig;
    @Resource
    private IWorkflowService workflowService;
    @Resource
    private IWorkflowRunTaskStatusService workflowRunTaskStatusService;
    @Resource
    private GrpcTaskService grpcTaskService;
    @Resource
    private NetManager netManager;

    @Resource
    private CommonService commonService;

    @Resource
    private IProjectService projectService;

    @Resource
    private IMetaDataDetailsService metaDataDetailsService;

    @Resource
    private IAlgorithmService algorithmService;

    @Resource
    private IAlgorithmCodeService algorithmCodeService;

    @Resource
    private IWorkflowNodeService workflowNodeService;

    @Resource
    private IWorkflowNodeCodeService workflowNodeCodeService;

    @Resource
    private IWorkflowNodeInputService workflowNodeInputService;

    @Resource
    private IWorkflowNodeOutputService workflowNodeOutputService;

    @Resource
    private IWorkflowNodeResourceService workflowNodeResourceService;

    @Resource
    private IOrganizationService organizationService;

    @Resource
    private IModelService modelService;


    @Resource
    private IAlgorithmVariableStructService algorithmVariableStructService;

    @Resource
    private RedissonObject redissonObject;

    @Resource
    private IUserMetaDataService userMetaDataService;
    @Resource
    private IWorkflowRunStatusService workflowRunStatusService;

    @Resource
    private WorkflowNodeMapper workflowNodeMapper;
    @Resource
    private WorkflowNodeInputMapper workflowNodeInputMapper;
    @Resource
    private WorkflowNodeOutputMapper workflowNodeOutputMapper;
    @Resource
    private WorkflowNodeResourceMapper workflowNodeResourceMapper;
    @Resource
    private WorkflowNodeCodeMapper workflowNodeCodeMapper;

    @Override
    public TaskDto assemblyTaskDto(WorkflowDto workflowDto) {
        return null;
    }

    @Override
    public List<WorkflowRunTaskStatus> queryWorkflowRunTaskStatusByTaskId(String taskId) {
        return null;
    }

    @Override
    public WorkflowRunStatus submitTaskAndExecute(Long workflowId, Integer version, String address, String sign) {
        // 加载工作流设置信息
        Workflow workflow = workflowService.queryWorkflowDetail(workflowId, version);
        // 配置启动校验(检验保存时未校验的)
        checkBeforeStart(workflow);
        // 生成执行记录
        WorkflowRunStatus workflowRunStatus = createAndSaveWorkflowRunStatus(workflow, address, sign);
        List<WorkflowRunTaskStatus> workflowRunTaskStatusList = createAndSaveWorkflowRunTaskStatus(workflow, workflowRunStatus);
        // 设置关联字段
        workflow.setWorkflowNodeMap(workflow.getWorkflowNodeReqList().stream().collect(Collectors.toMap(WorkflowNode::getNodeStep, item -> item)));
        workflowRunStatus.setWorkflowRunTaskStatusMap(workflowRunTaskStatusList.stream().collect(Collectors.toMap(WorkflowRunTaskStatus::getNodeStep, item -> item)));
        workflowRunStatus.setWorkflow(workflow);
        // 发起任务
        executeTask(workflowRunStatus);
        return workflowRunStatus;
    }

    private void checkBeforeStart(Workflow workflow) {
        workflow.getWorkflowNodeReqList().forEach(item ->{
            // 输入节点校验
            if (null == item.getWorkflowNodeInputReqList() || item.getWorkflowNodeInputReqList().size() == 0) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_INPUT_EXIST.getMsg());
            }
            // 输出节点校验
            if (null == item.getWorkflowNodeOutputReqList() || item.getWorkflowNodeOutputReqList().size() == 0) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_OUTPUT_EXIST.getMsg());
            }
        });
    }

    public WorkflowRunStatus executeTask(Long workflowRunStatusId) {
        // 创建工作流运行状态
        WorkflowRunStatus workflowRunStatus = load(workflowRunStatusId);
        executeTask(workflowRunStatus);
        return workflowRunStatus;
    }

    private void executeTask(WorkflowRunStatus workflowRunStatus){
        WorkflowRunTaskStatus curWorkflowRunTaskStatus = workflowRunStatus.getWorkflowRunTaskStatusMap().get(workflowRunStatus.getCurStep());

        if(curWorkflowRunTaskStatus.getRunStatus() == WorkflowRunStatusEnum.UN_RUN.getValue() ){
            curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUNNING.getValue());
            // 提交任务到 Net
            TaskDto taskDto = assemblyTaskDto(workflowRunStatus,null, curWorkflowRunTaskStatus);
            PublishTaskDeclareResponseDto respDto;
            try {
                respDto = grpcTaskService.syncPublishTask(netManager.getChannel(taskDto.getSender().getIdentityId()), taskDto);
                curWorkflowRunTaskStatus.setTaskId(respDto.getTaskId());
                curWorkflowRunTaskStatus.setRunMsg(respDto.getMsg());
                if (GrpcConstant.GRPC_SUCCESS_CODE != respDto.getStatus()) {
                    curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
                    workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
                }
                log.info("任务发布结果>>>>工作流id:{},任务名称：{},rosettanet收到处理任务，返回的taskId：{}", taskDto.getWorkFlowNodeId(), taskDto.getTaskName(), respDto.getTaskId());
            } catch (Exception e){
                log.error("executeTask error! runStatusId = {}  runTaskStatusId = {}", curWorkflowRunTaskStatus.getId());
                log.error("executeTask error! ", e);
                curWorkflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
                workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
            }
            updateById(workflowRunStatus);
            workflowRunTaskStatusService.updateById(curWorkflowRunTaskStatus);
        }
    }

    private TaskDto assemblyTaskDto(WorkflowRunStatus workflowRunStatus, WorkflowRunTaskStatus preWorkflowRunTaskStatus, WorkflowRunTaskStatus curWorkflowRunTaskStatus) {
        WorkflowNode workflowNode = curWorkflowRunTaskStatus.getWorkflowNode();

        //有指定当前工作流节点模型输入,获取工作流节点模型
        if (workflowNode.getModelId() != null && workflowNode.getModelId() > 0) {

            Model model = modelService.getById(workflowNode.getModelId());
            if (model == null) {
                log.error("WorkflowServiceImpl->getDataSupplierList,fail reason:{}", ErrorMsg.WORKFLOW_NODE_TASK_RESULT_NOT_EXIST.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_TASK_RESULT_NOT_EXIST.getMsg());
            }
        }

        //获取工作流代码输入信息
        String calculateContractCode;
        String dataSplitContractCode;
        WorkflowNodeCode workflowNodeCode = curWorkflowRunTaskStatus.getWorkflowNode().getWorkflowNodeCodeReq();
        if (null == workflowNodeCode) {
            AlgorithmCode algorithmCode = algorithmCodeService.getByAlgorithmId(workflowNode.getAlgorithmId());
            if (null == algorithmCode) {
                log.error("Can not find algorithm code by id:{}", workflowNode.getAlgorithmId());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_CODE_NOT_EXIST.getMsg());
            }
            calculateContractCode = algorithmCode.getCalculateContractCode();
            dataSplitContractCode = algorithmCode.getDataSplitContractCode();
        } else {
            calculateContractCode = workflowNodeCode.getCalculateContractCode();
            dataSplitContractCode = workflowNodeCode.getDataSplitContractCode();
        }

        //获取工作流节点输入信息
        List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.queryByWorkflowNodeId(workflowNode.getId());
        if (null == workflowNodeInputList || workflowNodeInputList.size() == 0) {
            log.error("Start workflow->assemblyTaskDto getByWorkflowNodeId fail by id:{}", workflowNode.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_INPUT_NOT_EXIST.getMsg());
        }

        //获取工作流节点输出信息
        List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.getByWorkflowNodeId(workflowNode.getId());

        //获取工作流节点自变量及因变量(这期没有些功能)
//        List<WorkflowNodeVariable> workflowNodeVariableList = workflowNodeVariableService.getByWorkflowNodeId(workflowNode.getId());

        //工作流节点资源表
        WorkflowNodeResource workflowNodeResource = getWorkflowNodeResource(workflowNode);

        //获取参与机构列表
        String[] identityIdArr = new String[workflowNodeInputList.size()];
        for (int i = 0; i < workflowNodeInputList.size(); i++) {
            identityIdArr[i] = workflowNodeInputList.get(i).getIdentityId();
        }
        Map<String, Organization> organizationMap = organizationService.getByIdentityIds(identityIdArr).stream().collect(Collectors.toMap(Organization::getIdentityId, organization -> organization));

        TaskDto taskDto = new TaskDto();

        taskDto.setWorkFlowNodeId(workflowNode.getId());
        //任务名称
        taskDto.setTaskName(commonService.generateTaskName(workflowNode.getId()));
        //发起任务用户
        taskDto.setUser(workflowRunStatus.getAddress());
        //发起账户用户类型
        taskDto.setUserType(UserTypeEnum.checkUserType(workflowRunStatus.getAddress()));
        //设置发起方
        taskDto.setSender(getSender(workflowNodeInputList));
        //任务算法提供方 组织信息
        taskDto.setAlgoSupplier(getAlgoSupplier(taskDto.getSender()));
        // 算力提供方 暂定三方
        taskDto.setPowerPartyIds(getPowerPartyIds());
        //数据提供方
        taskDto.setTaskDataSupplierDeclareDtoList(getDataSupplierList(workflowNodeInputList, organizationMap, preWorkflowRunTaskStatus.getModel()));
        //任务结果接受者
        taskDto.setTaskResultReceiverDeclareDtoList(getReceivers(workflowNodeOutputList, organizationMap));
        // 任务需要花费的资源声明
        taskDto.setResourceCostDeclareDto(getResourceCostDeclare(workflowNodeResource));
        //算法代码
        taskDto.setCalculateContractCode(calculateContractCode);
        //数据分片合约代码
        taskDto.setDataSplitContractCode(dataSplitContractCode);
        //合n约调用的额外可变入参 (jso 字符串, 根据算法来)
        taskDto.setContractExtraParams(getContractExtraParams(workflowNode.getAlgorithmId(), preWorkflowRunTaskStatus.getModel(), workflowNodeInputList));
        //发起任务的账户的签名
        taskDto.setSign(workflowRunStatus.getSign());
        //任务描述 (非必须)
        taskDto.setDesc(workflowNode.getNodeName());

        return taskDto;
    }


    private WorkflowNodeResource getWorkflowNodeResource(WorkflowNode workflowNode) {
        WorkflowNodeResource workflowNodeResource = workflowNodeResourceService.queryByWorkflowNodeId(workflowNode.getId());
        if (null == workflowNodeResource) {
            Algorithm algorithm = algorithmService.getById(workflowNode.getAlgorithmId());
            if (null == algorithm) {
                log.error("Can not find algorithm by id:{}", workflowNode.getAlgorithmId());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_NOT_EXIST.getMsg());
            }
            workflowNodeResource = new WorkflowNodeResource();
            workflowNodeResource.setCostMem(algorithm.getCostMem());
            workflowNodeResource.setCostCpu(algorithm.getCostCpu());
            workflowNodeResource.setCostBandwidth(algorithm.getCostBandwidth());
            workflowNodeResource.setRunTime(algorithm.getRunTime());
        }
        return workflowNodeResource;
    }

    private WorkflowRunStatus load(Long workflowRunStatusId) {
        return null;
    }

    private List<WorkflowRunTaskStatus> createAndSaveWorkflowRunTaskStatus(Workflow workflow, WorkflowRunStatus workflowRunStatus) {
        return workflow.getWorkflowNodeReqList().stream()
            .map(item -> {
                WorkflowRunTaskStatus workflowRunTaskStatus = new WorkflowRunTaskStatus();
                workflowRunTaskStatus.setWorkflowRunId(workflowRunStatus.getId());
                workflowRunTaskStatus.setWorkflowNodeId(item.getId());
                workflowRunTaskStatus.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
                workflowRunTaskStatus.setWorkflowNode(item);
                workflowRunTaskStatus.setNodeStep(item.getNodeStep());
                workflowRunTaskStatusService.save(workflowRunTaskStatus);
                return workflowRunTaskStatus;
            })
            .collect(Collectors.toList());
    }

    private WorkflowRunStatus createAndSaveWorkflowRunStatus(Workflow workflow, String address, String sign){
        WorkflowRunStatus workflowRunStatus = new WorkflowRunStatus();
        workflowRunStatus.setWorkflowId(workflow.getId());
        workflowRunStatus.setWorkflowEditVersion(workflow.getEditVersion());
        workflowRunStatus.setSign(sign);
        workflowRunStatus.setCurStep(1);
        workflowRunStatus.setBeginTime(new Date());  //TODO 使用数据将时间
        workflowRunStatus.setStep(workflow.getWorkflowNodeReqList().size());
        workflowRunStatus.setAddress(address);
        workflowRunStatus.setRunStatus(WorkflowRunStatusEnum.RUNNING.getValue());
        save(workflowRunStatus);
        return workflowRunStatus;
    }

    /**
     * 获取当前节点连接机构信息
     *
     * @param workflowNodeInputList 任务输入信息列表
     * @return 机构信息
     */
    private OrganizationIdentityInfoDto getSender(List<WorkflowNodeInput> workflowNodeInputList) {
//        // 获取用户绑定连接的组织
//        UserDto userDto = commonService.getCurrentUser();
//        if (StrUtil.isNotBlank(userDto.getIdentityId())) {
//            return this.saveSender(userDto.getIdentityId());
//        }
        // 用户没有绑定组织，默认发起方组织
        //todo
//        for (WorkflowNodeInput workflowNodeInput : workflowNodeInputList) {
//            if (null != workflowNodeInput.getSenderFlag() && SenderFlagEnum.TRUE.getValue() == workflowNodeInput.getSenderFlag()) {
//                return this.saveSender(workflowNodeInput.getIdentityId());
//            }
//        }
        log.error("获取当前工作流节点输入信息中不存在发起方，请核对信息:{}", workflowNodeInputList);
        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_SENDER_NOT_EXIST.getMsg());
    }


    private OrganizationIdentityInfoDto getAlgoSupplier(OrganizationIdentityInfoDto sender) {
        OrganizationIdentityInfoDto algoSupplier = new OrganizationIdentityInfoDto();
        algoSupplier.setPartyId("A0");
        algoSupplier.setNodeName(sender.getNodeName());
        algoSupplier.setNodeId(sender.getNodeId());
        algoSupplier.setIdentityId(sender.getIdentityId());
        return algoSupplier;
    }

    /**
     * 获取算力提供方列表
     *
     * @return 算力提供方列表
     */
    private List<String> getPowerPartyIds() {
        return new ArrayList<>(Arrays.asList("y1", "y2", "y3"));
    }


    /**
     * 获取算力提供方
     *
     * @param workflowNodeInputList 工作流节点信息
     * @param organizationMap       组织信息
     * @param preTaskResult         上一个任务执行结果
     * @return 数据提供方列表
     */
    private List<TaskDataSupplierDeclareDto> getDataSupplierList(List<WorkflowNodeInput> workflowNodeInputList, Map<String, Organization> organizationMap, Model preTaskResult) {
        List<TaskDataSupplierDeclareDto> taskDataSupplierDeclareDtoList = new ArrayList<>();
        TaskDataSupplierDeclareDto taskDataSupplierDeclareDto;

        OrganizationIdentityInfoDto taskOrganizationIdentityInfoDto;
        TaskMetaDataDeclareDto taskMetaDataDeclareDto;
        for (WorkflowNodeInput input : workflowNodeInputList) {
            taskOrganizationIdentityInfoDto = new OrganizationIdentityInfoDto();
            taskOrganizationIdentityInfoDto.setPartyId(input.getPartyId());
            taskOrganizationIdentityInfoDto.setNodeId(organizationMap.get(input.getIdentityId()).getNodeId());
            taskOrganizationIdentityInfoDto.setNodeName(organizationMap.get(input.getIdentityId()).getNodeName());
            taskOrganizationIdentityInfoDto.setIdentityId(input.getIdentityId());

            taskMetaDataDeclareDto = new TaskMetaDataDeclareDto();
            taskMetaDataDeclareDto.setMetaDataId(input.getDataTableId());

            String[] columnIdsArr = input.getDataColumnIds().split(",");
            if (columnIdsArr.length < 1) {
                log.error("WorkflowServiceImpl->getDataSupplierList 获取当前工作流节点索引列不存在");
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_INDEX_COLUMN_NOT_EXIST.getMsg());
            }
            List<Integer> columnIndexList = metaDataDetailsService.getColumnIndexByIds(columnIdsArr);

            taskMetaDataDeclareDto.setKeyColumn(metaDataDetailsService.getColumnIndexById(input.getKeyColumn()).getColumnIndex());

            List<Integer> selectedColumns = new ArrayList<>(columnIndexList);
            taskMetaDataDeclareDto.setSelectedColumns(selectedColumns);

            taskDataSupplierDeclareDto = new TaskDataSupplierDeclareDto();
            taskDataSupplierDeclareDto.setTaskOrganizationIdentityInfoDto(taskOrganizationIdentityInfoDto);
            taskDataSupplierDeclareDto.setTaskMetaDataDeclareDto(taskMetaDataDeclareDto);

            taskDataSupplierDeclareDtoList.add(taskDataSupplierDeclareDto);

            //如果有上一个节点的模型，需要做为输入传给底层
//            if (null != preTaskResult && SenderFlagEnum.TRUE.getValue() == input.getSenderFlag()) {
//                taskDataSupplierDeclareDto = new TaskDataSupplierDeclareDto();
//                OrganizationIdentityInfoDto modelOrg = new OrganizationIdentityInfoDto();
//                BeanUtil.copyProperties(taskOrganizationIdentityInfoDto, modelOrg);
//                modelOrg.setPartyId("p" + workflowNodeInputList.size());
//                taskDataSupplierDeclareDto.setTaskOrganizationIdentityInfoDto(modelOrg);
//
//                taskMetaDataDeclareDto = new TaskMetaDataDeclareDto();
//                taskMetaDataDeclareDto.setMetaDataId(preTaskResult.getMetadataId());
//                taskMetaDataDeclareDto.setKeyColumn(0);
//                taskDataSupplierDeclareDto.setTaskMetaDataDeclareDto(taskMetaDataDeclareDto);
//                taskDataSupplierDeclareDtoList.add(taskDataSupplierDeclareDto);
//            }
        }
        //模型提供方
        return taskDataSupplierDeclareDtoList;
    }

    /**
     * 获取任务结果接收方
     *
     * @param workflowNodeOutputList 工作流输入列表
     * @return 任务结果接收方列表
     */
    private List<OrganizationIdentityInfoDto> getReceivers(List<WorkflowNodeOutput> workflowNodeOutputList, Map<String, Organization> organizationMap) {
        List<OrganizationIdentityInfoDto> receiverList = new ArrayList<>();
        OrganizationIdentityInfoDto organizationIdentityInfoDto;
        for (WorkflowNodeOutput output : workflowNodeOutputList) {
            organizationIdentityInfoDto = new OrganizationIdentityInfoDto();
            organizationIdentityInfoDto.setPartyId(output.getPartyId());
            organizationIdentityInfoDto.setNodeName(organizationMap.get(output.getIdentityId()).getNodeName());
            organizationIdentityInfoDto.setNodeId(organizationMap.get(output.getIdentityId()).getNodeId());
            organizationIdentityInfoDto.setIdentityId(output.getIdentityId());
            receiverList.add(organizationIdentityInfoDto);
        }
        return receiverList;
    }

    /**
     * 获取任务所需要的资源声明
     *
     * @param workflowNodeResource 工作流资源信息
     */
    private TaskResourceCostDeclareDto getResourceCostDeclare(WorkflowNodeResource workflowNodeResource) {
        TaskResourceCostDeclareDto taskResourceCostDeclareDto = new TaskResourceCostDeclareDto();
        taskResourceCostDeclareDto.setMemory(workflowNodeResource.getCostMem());
        taskResourceCostDeclareDto.setProcessor(workflowNodeResource.getCostCpu());
        taskResourceCostDeclareDto.setBandwidth(workflowNodeResource.getCostBandwidth());
        taskResourceCostDeclareDto.setDuration(workflowNodeResource.getRunTime());
        return taskResourceCostDeclareDto;

    }


    /**
     * 合约调用的额外可变入参
     *
     * @param algorithmId 算法id
     * @return 额外可变入参
     */
    private String getContractExtraParams(Long algorithmId, Model preTaskResult, List<WorkflowNodeInput> workflowNodeInputList) {
        AlgorithmVariableStruct jsonStruct = algorithmVariableStructService.getByAlgorithmId(algorithmId);
        if (null == jsonStruct) {
            return null;
        } else {
            String struct = jsonStruct.getStruct();
            // 把可变参数进行替换
            log.info("jsonStruct.getStruct() is:{}", struct);
            if (!JsonUtils.isJson(struct)) {
                log.error("WorkflowServiceImpl->getContractExtraParams,{}", ErrorMsg.ALG_VARIABLE_STRUCT_ERROR.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALG_VARIABLE_STRUCT_ERROR.getMsg());
            }
            JSONObject jsonObject = JSON.parseObject(struct);
            //逻辑训练动态参数[因变量(标签)]
            if (jsonObject.containsKey(AlgorithmConstant.LABEL_COLUMN)) {
                for (WorkflowNodeInput input : workflowNodeInputList) {
                    //TODO
//                    if (input.getSenderFlag() == SenderFlagEnum.TRUE.getValue()) {
//                        jsonObject.put("label_column",
//                                metaDataDetailsService.getColumnIndexById(input.getDependentVariable()).getColumnName());
//                    }
                }
            }
            if (jsonObject.containsKey(AlgorithmConstant.MODEL_RESTORE_PARTY)) {
                jsonObject.put("model_restore_party", "p" + workflowNodeInputList.size());
            }
            //逻辑回归动态参数[模型所在的路径，需填绝对路径]
            if (jsonObject.containsKey(AlgorithmConstant.MODEL_PATH)) {
                if (preTaskResult == null || preTaskResult.getFilePath() == null) {
                    log.error("启动工作流失败,未指定当前工作流节点的模型输入路径");
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_MODEL_NOT_EXIST.getMsg());
                }
                jsonObject.put("model_path", preTaskResult.getFilePath());
            }
            return jsonObject.toJSONString();
        }
    }

    /**
     * 启动前判断当前节点算法是否有模型
     */
    private void checkAlgorithm(WorkflowNode workflowNode) {
        Algorithm algorithm = algorithmService.getAlgorithmById(workflowNode.getAlgorithmId());
        // 启动前判断当前节点算法是否有模型
        boolean modelFlag = workflowNode.getModelId() == null || workflowNode.getModelId() == 0;
        //如果算法需要使用模型，且是第一个节点，则需要判断是否有模型，否则可以重前一个节点获取模型
        if (workflowNode.getNodeStep() == SysConstant.INT_1 && InputModelEnum.NEED.getValue() == algorithm.getInputModel() && modelFlag) {
            log.error("checkModel--当前节点未配置模型, inputModel:{}, workflowNode:{}", algorithm.getInputModel(), workflowNode);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_MODEL_NOT_EXIST.getMsg());
        }
        // 启动数是前判断当前节点多方数据行否相等
        if (SysConstant.INT_1 == algorithm.getDataRowsFlag()) {
            List<NodeMetaDataDto> nodeMetaDataDtoList = workflowNodeInputService.getMetaDataByWorkflowNodeId(workflowNode.getId());
            if (!nodeMetaDataDtoList.isEmpty()) {
                NodeMetaDataDto initNodeMetaDataDto = nodeMetaDataDtoList.get(0);
                for (NodeMetaDataDto nodeMetaDataDto : nodeMetaDataDtoList) {
                    if (initNodeMetaDataDto.getMetaDataRows() != nodeMetaDataDto.getMetaDataRows()) {
                        log.error("checkModel--工作流节点多方数据行数不一致, initNodeMetaDataDto:{}, nodeMetaDataDto:{}", initNodeMetaDataDto, nodeMetaDataDto);
                        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_DATA_ROWS_CHECK.getMsg());
                    }
                }
            }
        }
    }

}
