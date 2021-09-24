package com.platon.rosettaflow.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.*;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import com.platon.rosettaflow.grpc.service.GrpcAuthService;
import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.task.req.dto.*;
import com.platon.rosettaflow.mapper.WorkflowMapper;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DateTime.now;

/**
 * 工作流服务实现类
 *
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@Service
public class WorkflowServiceImpl extends ServiceImpl<WorkflowMapper, Workflow> implements IWorkflowService {

    @Resource
    private CommonService commonService;

    @Resource
    private IProjectService projectService;

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
    private IWorkflowNodeVariableService workflowNodeVariableService;

    @Resource
    private GrpcTaskService grpcTaskService;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private ISubJobService subJobService;

    @Resource
    private ISubJobNodeService subJobNodeService;

    @Override
    public IPage<WorkflowDto> queryWorkFlowPageList(Long projectId, String workflowName, Long current, Long size) {
        IPage<WorkflowDto> page = new Page<>(current, size);
        return this.baseMapper.queryWorkFlowPageList(projectId, workflowName, page);
    }

    @Override
    public List<Workflow> queryWorkFlowByProjectId(Long projectId) {
        LambdaQueryWrapper<Workflow> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Workflow::getProjectId, projectId);
        queryWrapper.eq(Workflow::getRunStatus, WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
        queryWrapper.eq(Workflow::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }

    @Override
    public Workflow queryWorkflowDetail(Long id) {
        LambdaQueryWrapper<Workflow> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Workflow::getId, id);
        queryWrapper.eq(Workflow::getStatus, StatusEnum.VALID.getValue());
        Workflow workflow = this.getOne(queryWrapper);
        if (Objects.isNull(workflow)) {
            log.error("workflow not found by id:{}", id);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }
        return workflow;
    }

    @Override
    public void addWorkflow(Workflow workflow) {
        try {
            // 校验是否有编辑权限
            checkEditPermission(workflow.getProjectId());
            workflow.setUserId(commonService.getCurrentUser().getId());
            this.save(workflow);
        } catch (DuplicateKeyException e) {
            log.info("addWorkflow--添加工作流接口失败:{}", e.getMessage(), e);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
        }
    }

    @Override
    public void editWorkflow(Long id, String workflowName, String workflowDesc) {
        Workflow workflow = this.queryWorkflowDetail(id);
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        try {
            workflow.setWorkflowName(workflowName);
            workflow.setWorkflowDesc(workflowDesc);
            this.updateById(workflow);
        } catch (DuplicateKeyException dke) {
            log.info("editWorkflow--编辑工作流接口失败:{}", dke.getMessage(), dke);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
        }
    }

    @Override
    public void deleteWorkflow(Long id) {
        Workflow workflow = this.queryWorkflowDetail(id);
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        // 逻辑删除工作流，并修改版本标识
        workflow.setId(id);
        workflow.setDelVersion(id);
        workflow.setStatus((byte) 0);
        this.updateById(workflow);
    }

    @Override
    public void deleteWorkflowBatch(String ids) {
        if (ids.trim().length() == 0) {
            return;
        }
        // 转换id类型
        List<Long> idsList = convertIdType(ids);
        if (idsList.size() > 0) {
            List<Workflow> list = new ArrayList<>();
            idsList.forEach(id -> {
                Workflow workflow = this.queryWorkflowDetail(id);
                // 校验是否有编辑权限
                checkEditPermission(workflow.getProjectId());
                // 逻辑删除工作流，并修改版本标识
                workflow.setId(id);
                workflow.setDelVersion(id);
                workflow.setStatus((byte) 0);
                list.add(workflow);
            });
            this.updateBatchById(list);
        }
    }

    /**
     * 转换id类型
     */
    private List<Long> convertIdType(String ids) {
        return Arrays.stream(ids.split(",")).map(id ->
                Long.parseLong(id.trim())).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void copyWorkflow(Long originId, String workflowName, String workflowDesc) {
        try {
            // 将复制的工作流数据id置空，新增一条新的工作流数据
            Long newWorkflowId = saveCopyWorkflow(originId, workflowName, workflowDesc);
            // 查询原工作流节点
            List<WorkflowNode> workflowNodeOldList = workflowNodeService.getWorkflowNodeList(originId);
            // 保存为新工作流节点
            workflowNodeService.copySaveWorkflowNode(newWorkflowId, workflowNodeOldList);
        } catch (Exception e) {
            log.error("copyWorkflow--复制工作流接口失败:{}", e.getMessage(), e);
            if (e instanceof DuplicateKeyException) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
            }
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_COPY_ERROR.getMsg());
        }
    }

    /**
     * 将复制的工作流数据id置空，新增一条新的工作流数据
     */
    private Long saveCopyWorkflow(Long originId, String workflowName, String workflowDesc) {
        Workflow originWorkflow = this.queryWorkflowDetail(originId);
        // 校验是否有编辑权限
        checkEditPermission(originWorkflow.getProjectId());
        Workflow newWorkflow = new Workflow();
        newWorkflow.setProjectId(originWorkflow.getProjectId());
        newWorkflow.setUserId(originWorkflow.getUserId());
        newWorkflow.setWorkflowName(workflowName);
        newWorkflow.setWorkflowDesc(workflowDesc);
        newWorkflow.setNodeNumber(originWorkflow.getNodeNumber());
        this.save(newWorkflow);
        return newWorkflow.getId();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void start(WorkflowDto workflowDto) {
        Workflow orgWorkflow = this.queryWorkflowDetail(workflowDto.getId());
        // 校验是否有编辑权限
        checkEditPermission(orgWorkflow.getProjectId());
        //截止节点不能超过工作流最大节点
        //如果截止节点为空，设置为工作流最后一个节点
        if (null == workflowDto.getEndNode()) {
            workflowDto.setEndNode(orgWorkflow.getNodeNumber());
        } else if (null == orgWorkflow.getNodeNumber() || orgWorkflow.getNodeNumber() < workflowDto.getEndNode()) {
            log.error("endNode is:{} can not more than workflow max nodeNumber:{}", workflowDto.getEndNode(), orgWorkflow.getNodeNumber());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_END_NODE_OVERFLOW.getMsg());
        }

        //保存用户和地址及签名
        updateSign(workflowDto);

        //此处先执行第一个节点，待第一个节点执行成功后再执行
        TaskDto taskDto = assemblyTaskDto(orgWorkflow.getId(), workflowDto.getStartNode(), workflowDto.getAddress(), workflowDto.getSign());
        grpcTaskService.asyncPublishTask(taskDto, publishTaskDeclareResponse -> {

            WorkflowNode workflowNode = workflowNodeService.getById(taskDto.getWorkFlowNodeId());
            if (workflowDto.isJobFlg()) {
                //更新作业
                SubJob subJob = subJobService.getById(workflowDto.getJobId());
                subJob.setEndTime(now());
                //TODO CHECK
                subJob.setRunTime(String.valueOf(DateUtil.between(subJob.getBeginTime(), subJob.getEndTime(), DateUnit.MINUTE)));
                //子作业节点信息
                SubJobNode subJobNode = new SubJobNode();
                subJobNode.setSubJobId(subJob.getId());
                subJobNode.setAlgorithmId(workflowNode.getAlgorithmId());
                subJobNode.setNodeStep(workflowNode.getNodeStep());

                if (publishTaskDeclareResponse.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {

                    subJobNode.setRunStatus(SubJobNodeStatusEnum.RUN_SUCCESS.getValue());

                    //如果是最后一个节点
                    if (null == workflowNode.getNextNodeStep() || workflowNode.getNextNodeStep() < 1) {
                        subJob.setSubJobStatus(SubJobStatusEnum.RUN_SUCCESS.getValue());
                        subJobService.updateById(subJob);
                        subJobNodeService.save(subJobNode);
                    } else {
                        subJob.setSubJobStatus(SubJobStatusEnum.RUNNING.getValue());
                        subJobService.updateById(subJob);
                        subJobNodeService.save(subJobNode);

                        //当前工作流执行成功，继续执行下一个工作流节点
                        workflowDto.setStartNode(workflowNode.getNextNodeStep());
                        this.start(workflowDto);
                    }
                } else {
                    subJob.setSubJobStatus(SubJobStatusEnum.RUN_FAIL.getValue());
                    subJobNode.setRunStatus(SubJobNodeStatusEnum.RUN_FAIL.getValue());
                }

                subJobService.updateById(subJob);
                subJobNodeService.save(subJobNode);
            } else {
                //更新工作流节点
                Workflow workflow;
                if (publishTaskDeclareResponse.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
                    //处理成功
                    workflowNode.setTaskId(publishTaskDeclareResponse.getTaskId());
                    workflowNode.setRunStatus(WorkflowRunStatusEnum.RUN_SUCCESS.getValue());

                    //更新整个工作流信息:如果是最后一个节点，整个工作流更新成处理成功，否则更新成处理中
                    workflow = this.getById(workflowNode.getWorkflowId());
                    if (null == workflowNode.getNextNodeStep() || workflowNode.getNextNodeStep() < 1) {
                        workflow.setRunStatus(WorkflowRunStatusEnum.RUN_SUCCESS.getValue());
                    } else {
                        workflow.setRunStatus(WorkflowRunStatusEnum.RUNNING.getValue());
                        this.updateById(workflow);

                        //当前工作流执行成功，继续执行下一个节点工作流
                        workflowDto.setStartNode(workflowNode.getNextNodeStep());
                        this.start(workflowDto);
                    }
                } else {
                    //处理失败
                    workflowNode.setTaskId(publishTaskDeclareResponse.getTaskId() != null ? publishTaskDeclareResponse.getTaskId() : "");
                    workflowNode.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());

                    //更新整个工作流信息:处理失败
                    workflow = this.getById(workflowNode.getWorkflowId());
                    workflow.setRunStatus(WorkflowRunStatusEnum.RUN_FAIL.getValue());
                }
                workflowNode.setRunMsg(publishTaskDeclareResponse.getMsg());
                workflowNodeService.updateById(workflowNode);

                this.updateById(workflow);
            }
        });
    }

    private void updateSign(WorkflowDto workflowDto) {
        LambdaUpdateWrapper<Workflow> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Workflow::getAddress, workflowDto.getAddress());
        updateWrapper.set(Workflow::getSign, workflowDto.getSign());
        updateWrapper.eq(Workflow::getId, workflowDto.getId());
        this.update(updateWrapper);
    }

    @Override
    public Long addWorkflowByTemplate(Long projectId, Long userId, WorkflowTemp workflowTemp) {
        Workflow workflow = new Workflow();
        workflow.setProjectId(projectId);
        workflow.setUserId(userId);
        workflow.setWorkflowName(workflowTemp.getWorkflowName());
        workflow.setWorkflowDesc(workflowTemp.getWorkflowDesc());
        workflow.setNodeNumber(workflowTemp.getNodeNumber());
        workflow.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
        this.save(workflow);
        return workflow.getId();
    }

    @Override
    public void terminate(Long workflowId) {
        Workflow workflow = this.queryWorkflowDetail(workflowId);
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        if (workflow.getRunStatus() != WorkflowRunStatusEnum.RUNNING.getValue()) {
            log.error("workflow by id:{} is not running can not terminate", workflowId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_RUNNING.getMsg());
        }

        //获取工作流正在执行的节点进行终止，并更新整个工作流状态为停止
        WorkflowNode workflowNode = workflowNodeService.getRunningNodeByWorkflowId(workflowId);
        if (null == workflowNode) {
            this.updateRunStatus(workflowId, WorkflowRunStatusEnum.UN_RUN.getValue());
        } else {
            TerminateTaskRequestDto terminateTaskRequestDto = assemblyTerminateTaskRequestDto(workflow, workflowNode);
            TerminateTaskRespDto terminateTaskRespDto = grpcTaskService.terminateTask(terminateTaskRequestDto);

            if (terminateTaskRespDto.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
                this.updateRunStatus(workflowId, WorkflowRunStatusEnum.UN_RUN.getValue());
                workflowNodeService.updateRunStatusByWorkflowId(workflowId, workflowNode.getRunStatus(), WorkflowRunStatusEnum.UN_RUN.getValue());
            } else {
                log.error("Terminate workflow with id:{} fail", workflowId);
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_TERMINATE_NET_PROCESS_ERROR.getMsg());
            }
        }
    }

    private TerminateTaskRequestDto assemblyTerminateTaskRequestDto(Workflow workflow, WorkflowNode workflowNode) {
        TerminateTaskRequestDto terminateTaskRequestDto = new TerminateTaskRequestDto();
        terminateTaskRequestDto.setUser(workflow.getAddress());
        terminateTaskRequestDto.setUserType(UserTypeEnum.checkUserType(workflow.getAddress()));
        terminateTaskRequestDto.setTaskId(workflowNode.getTaskId());
        terminateTaskRequestDto.setSign(workflow.getSign());
        return terminateTaskRequestDto;
    }

    @Override
    public void updateRunStatus(Long workflowId, Byte runStatus) {
        LambdaUpdateWrapper<Workflow> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Workflow::getRunStatus, WorkflowRunStatusEnum.UN_RUN.getValue());
        updateWrapper.eq(Workflow::getId, workflowId);
        this.update(updateWrapper);
    }

    /**
     * 组装发布任务请求对象
     *
     * @param workFlowId  工作流id
     * @param currentNode 工作流节点序号
     * @param address     发起任务时的钱包地址
     * @return 发布任务请求对象
     */
    public TaskDto assemblyTaskDto(Long workFlowId, Integer currentNode, String address, String sign) {
        WorkflowNode workflowNode = workflowNodeService.getByWorkflowIdAndStep(workFlowId, currentNode);

        //获取工作流代码输入信息
        String calculateContractCode;
        String dataSplitContractCode;
        WorkflowNodeCode workflowNodeCode = workflowNodeCodeService.getByWorkflowNodeId(workflowNode.getId());
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
        List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.getByWorkflowNodeId(workflowNode.getId());

        //获取工作流节点输出信息
        List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.getByWorkflowNodeId(workflowNode.getId());

        //获取工作流节点自变量及因变量
        List<WorkflowNodeVariable> workflowNodeVariableList = workflowNodeVariableService.getByWorkflowNodeId(workflowNode.getId());

        //工作流节点资源表
        WorkflowNodeResource workflowNodeResource = getWorkflowNodeResource(workflowNode);

        TaskDto taskDto = new TaskDto();

        taskDto.setWorkFlowNodeId(workflowNode.getId());
        //任务名称
        taskDto.setTaskName(commonService.generateTaskName(workflowNode.getId()));
        //发起任务用户
        taskDto.setUser(address);
        //发起账户用户类型
        taskDto.setUserType(UserTypeEnum.checkUserType(address));
        //设置发起方
        taskDto.setSender(getSender(workflowNodeInputList));
        //任务算法提供方 组织信息
        taskDto.setAlgoSupplier(getAlgoSupplier(taskDto.getSender()));
        // 算力提供方 暂定三方
        taskDto.setPowerPartyIds(getPowerPartyIds());
        //数据提供方
        taskDto.setTaskDataSupplierDeclareDtoList(getDataSupplierList(workflowNodeInputList));
        //任务结果接受者
        taskDto.setTaskResultReceiverDeclareDtoList(getReceivers(workflowNodeOutputList));
        // 任务需要花费的资源声明
        taskDto.setResourceCostDeclareDto(getResourceCostDeclare(workflowNodeResource));
        //算法代码
        taskDto.setCalculateContractCode(calculateContractCode);
        //数据分片合约代码
        taskDto.setDataSplitContractCode(dataSplitContractCode);
        //合约调用的额外可变入参 (json 字符串, 根据算法来)
        taskDto.setContractExtraParams(getContractExtraParams(workflowNodeVariableList));
        //发起任务的账户的签名
        taskDto.setSign(sign);
        //任务描述 (非必须)
        taskDto.setDesc(workflowNode.getNodeName());

        return taskDto;
    }

    private WorkflowNodeResource getWorkflowNodeResource(WorkflowNode workflowNode) {
        WorkflowNodeResource workflowNodeResource = workflowNodeResourceService.getByWorkflowNodeId(workflowNode.getId());
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

    /**
     * 合约调用的额外可变入参
     *
     * @param workflowNodeVariableList 合约的可变参数列表
     * @return 额外可变入参
     */
    private String getContractExtraParams(List<WorkflowNodeVariable> workflowNodeVariableList) {
        JSONObject jsonObject = JSONUtil.createObj();
        for (WorkflowNodeVariable variable : workflowNodeVariableList) {
            jsonObject.set(variable.getVarNodeKey(), variable.getVarNodeValue());
        }
        return jsonObject.toString();
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
     * 获取任务结果接收方
     *
     * @param workflowNodeOutputList 工作流输入列表
     * @return 任务结果接收方列表
     */
    private List<OrganizationIdentityInfoDto> getReceivers(List<WorkflowNodeOutput> workflowNodeOutputList) {
        List<OrganizationIdentityInfoDto> receiverList = new ArrayList<>();
        OrganizationIdentityInfoDto organizationIdentityInfoDto;
        for (WorkflowNodeOutput output : workflowNodeOutputList) {
            organizationIdentityInfoDto = new OrganizationIdentityInfoDto();
            organizationIdentityInfoDto.setPartyId(output.getPartyId());
            organizationIdentityInfoDto.setNodeName(output.getIdentityName());
            organizationIdentityInfoDto.setNodeId(output.getNodeId());
            organizationIdentityInfoDto.setIdentityId(output.getIdentityId());
            receiverList.add(organizationIdentityInfoDto);
        }
        return receiverList;
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
     * @return 数据提供方列表
     */
    private List<TaskDataSupplierDeclareDto> getDataSupplierList(List<WorkflowNodeInput> workflowNodeInputList) {
        List<TaskDataSupplierDeclareDto> taskDataSupplierDeclareDtoList = new ArrayList<>();
        TaskDataSupplierDeclareDto taskDataSupplierDeclareDto;

        OrganizationIdentityInfoDto taskOrganizationIdentityInfoDto;
        TaskMetaDataDeclareDto taskMetaDataDeclareDto;
        for (WorkflowNodeInput input : workflowNodeInputList) {
            taskOrganizationIdentityInfoDto = new OrganizationIdentityInfoDto();
            taskOrganizationIdentityInfoDto.setPartyId(input.getPartyId());
            taskOrganizationIdentityInfoDto.setNodeId(input.getNodeId());
            taskOrganizationIdentityInfoDto.setIdentityId(input.getIdentityId());

            taskMetaDataDeclareDto = new TaskMetaDataDeclareDto();
            taskMetaDataDeclareDto.setMetaDataId(input.getDataTableId());

            List<Integer> selectedColumns = new ArrayList<>();
            String[] columnIdsArr = input.getDataColumnIds().split(",");
            if (columnIdsArr.length > 0) {
                // TODO 索引列暂定选择列中的第一列
                taskMetaDataDeclareDto.setKeyColumn(Integer.valueOf(columnIdsArr[0]));
            }
            for (String s : columnIdsArr) {
                selectedColumns.add(Integer.valueOf(s.trim()));
            }

            taskMetaDataDeclareDto.setSelectedColumns(selectedColumns);

            taskDataSupplierDeclareDto = new TaskDataSupplierDeclareDto();
            taskDataSupplierDeclareDto.setTaskOrganizationIdentityInfoDto(taskOrganizationIdentityInfoDto);
            taskDataSupplierDeclareDto.setTaskMetaDataDeclareDto(taskMetaDataDeclareDto);

            taskDataSupplierDeclareDtoList.add(taskDataSupplierDeclareDto);
        }
        return taskDataSupplierDeclareDtoList;
    }

    /**
     * 获取当前节点连接机构信息
     *
     * @param workflowNodeInputList 任务输入信息列表
     * @return 机构信息
     */
    private OrganizationIdentityInfoDto getSender(List<WorkflowNodeInput> workflowNodeInputList) {
//        for (int i = 0; i < workflowNodeInputList.size(); i++) {
//            workflowNodeInputList.get(i).gets
//        }
        OrganizationIdentityInfoDto sender = new OrganizationIdentityInfoDto();
        NodeIdentityDto nodeIdentityDto = grpcAuthService.getNodeIdentity();
        //发起方的默认设置成p0
        sender.setPartyId("p0");
        sender.setNodeName(nodeIdentityDto.getNodeName());
        sender.setNodeId(nodeIdentityDto.getNodeId());
        sender.setIdentityId(nodeIdentityDto.getIdentityId());
        return sender;
    }

    private OrganizationIdentityInfoDto getAlgoSupplier(OrganizationIdentityInfoDto sender) {
        OrganizationIdentityInfoDto algoSupplier = new OrganizationIdentityInfoDto();
        //发起方的默认设置成a1
        algoSupplier.setIdentityId("a1");
        sender.setNodeName(sender.getNodeName());
        sender.setNodeId(sender.getNodeId());
        sender.setIdentityId(sender.getIdentityId());
        return algoSupplier;
    }

    /**
     * 校验是否有编辑权限
     */
    private void checkEditPermission(Long projectId) {
        Byte role = projectService.getRoleByProjectId(projectId);
        if (null == role || ProjectMemberRoleEnum.VIEW.getRoleId() == role) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_NOT_PERMISSION_ERROR.getMsg());
        }
    }
}
