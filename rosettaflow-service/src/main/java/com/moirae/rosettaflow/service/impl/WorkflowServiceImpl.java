package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.AlgorithmConstant;
import com.moirae.rosettaflow.common.constants.SysConfig;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.common.utils.JsonUtils;
import com.moirae.rosettaflow.dto.NodeMetaDataDto;
import com.moirae.rosettaflow.dto.UserDto;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.constant.GrpcConstant;
import com.moirae.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.*;
import com.moirae.rosettaflow.grpc.task.resp.dto.PublishTaskDeclareResponseDto;
import com.moirae.rosettaflow.mapper.WorkflowMapper;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.*;
import com.zengtengpeng.operation.RedissonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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
    private SysConfig sysConfig;

    @Resource
    private CommonService commonService;

    @Resource
    private IProjectService projectService;

    @Resource
    private IMetaDataService metaDataService;

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
    private GrpcTaskService grpcTaskService;

    @Resource
    private ISubJobService subJobService;

    @Resource
    private ISubJobNodeService subJobNodeService;

    @Resource
    private IOrganizationService organizationService;

    @Resource
    private IAlgorithmVariableStructService algorithmVariableStructService;

    @Resource
    private RedissonObject redissonObject;

    @Resource
    private ITaskResultService taskResultService;

    @Resource
    private IUserMetaDataService userMetaDataService;

    public WorkflowServiceImpl(IMetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @Override
    public IPage<WorkflowDto> queryWorkFlowPageList(Long projectId, String workflowName, Long current, Long size) {
        IPage<WorkflowDto> page = new Page<>(current, size);
        return this.baseMapper.queryWorkFlowPageList(projectId, workflowName, page);
    }

    @Override
    public List<Workflow> queryListById(List<Long> idList) {
        LambdaQueryWrapper<Workflow> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Workflow::getId, idList);
        queryWrapper.eq(Workflow::getStatus, StatusEnum.VALID.getValue());
        return this.list(queryWrapper);
    }

    @Override
    public List<Workflow> queryListByProjectId(List<Long> projectIdList) {
        LambdaQueryWrapper<Workflow> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Workflow::getStatus, StatusEnum.VALID.getValue());
        queryWrapper.in(Workflow::getProjectId, projectIdList);
        return this.list(queryWrapper);
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
            log.error("Workflow does not exist, workflowId:{}", id);
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
            // 更新最新时间
            workflow.setUpdateTime(new Date());
            this.updateById(workflow);
        } catch (DuplicateKeyException dke) {
            log.info("editWorkflow--编辑工作流接口失败:{}", dke.getMessage(), dke);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
        }
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, rollbackFor = RuntimeException.class)
    public void deleteWorkflow(Long id) {
        Workflow workflow = this.queryWorkflowDetail(id);
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        // 删除当前工作流所有节点数据
        this.deleteWorkflowAllNodeData(id);
        // 逻辑删除工作流，并修改版本标识
        workflow.setId(id);
        workflow.setDelVersion(id);
        workflow.setStatus(StatusEnum.UN_VALID.getValue());
        this.updateById(workflow);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteWorkflowBatch(String ids) {
        if (ids.trim().length() == 0) {
            return;
        }
        // 转换id类型
        List<Long> idsList = convertIdType(ids);
        List<Workflow> workflowList = this.queryListById(idsList);
        if (workflowList.size() > 0) {
            // 校验是否有编辑权限
            workflowList.forEach(workflow -> this.checkEditPermission(workflow.getProjectId()));
            // 并行删除工作流中所有节点数据
            workflowList.parallelStream().forEach(workflow -> this.deleteWorkflowAllNodeData(workflow.getId()));
        }
        // 逻辑删除工作流，并修改版本标识
        if (idsList.size() > 0) {
            List<Workflow> list = new ArrayList<>();
            idsList.forEach(id -> {
                Workflow workflow = new Workflow();
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
    public void deleteWorkflowAllNodeData(Long id) {
        baseMapper.deleteWorkflowAllNodeData(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void copyWorkflow(Long originId, String workflowName, String workflowDesc) {
        try {
            // 复制新增一条新的工作流数据
            Long newWorkflowId = saveCopyWorkflow(originId, workflowName, workflowDesc);
            // 查询原工作流节点
            List<WorkflowNode> workflowNodeOldList = workflowNodeService.getWorkflowNodeList(originId);
            // 保存复制的工作流节点及所属数据, Boolean.FALSE表示不是模板copy判断变量
            workflowNodeService.saveCopyWorkflowNode(newWorkflowId, workflowNodeOldList, Boolean.FALSE);
        } catch (Exception e) {
            log.error("copyWorkflow--复制工作流接口失败, 错误信息:{}, 异常:{}", e.getMessage(), e);
            if (e instanceof DuplicateKeyException) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
            }
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_COPY_ERROR.getMsg());
        }
    }

    /**
     * 复制新增一条新的工作流数据
     */
    private Long saveCopyWorkflow(Long originId, String workflowName, String workflowDesc) {
        UserDto userDto = commonService.getCurrentUser();
        Workflow originWorkflow = this.queryWorkflowDetail(originId);
        // 校验是否有编辑权限
        checkEditPermission(originWorkflow.getProjectId());
        Workflow newWorkflow = new Workflow();
        newWorkflow.setProjectId(originWorkflow.getProjectId());
        newWorkflow.setUserId(userDto.getId());
        newWorkflow.setWorkflowName(workflowName);
        newWorkflow.setWorkflowDesc(workflowDesc);
        newWorkflow.setNodeNumber(originWorkflow.getNodeNumber());
        this.save(newWorkflow);
        return newWorkflow.getId();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void start(WorkflowDto workflowDto) {
        Workflow workflow = this.queryWorkflowDetail(workflowDto.getId());
        // 校验是否有编辑权限:非作业且启动第一个节点才进行校验
        if (!workflowDto.isJobFlg() && workflowDto.getStartNode() == 1) {
            checkEditPermission(workflow.getProjectId());
        }
        // 如果截止节点为空，设置为工作流最后一个节点
        if (null == workflowDto.getEndNode()) {
            workflowDto.setEndNode(workflow.getNodeNumber());
        }
        // 截止节点不能超过工作流最大节点
        if (null == workflow.getNodeNumber() || workflow.getNodeNumber() < workflowDto.getEndNode()) {
            log.error("endNode is:{} can not more than workflow max nodeNumber:{}", workflowDto.getEndNode(), workflow.getNodeNumber());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_END_NODE_OVERFLOW.getMsg());
        }
        // 保存用户和地址及签名并更新工作流状态为运行中
        if (!workflowDto.isJobFlg() && workflowDto.getStartNode() == 1) {
            this.updateSign(workflowDto);
        }

        /* ------ 此处先执行第一个节点，待第一个节点执行成功后再执行 -----*/
        // 组装发布任务请求对象
        TaskDto taskDto = this.assemblyTaskDto(workflowDto);
        log.info("任务发布>>>>开始启动工作流任务workflowId:{},任务名称：{},请求数据为：{}", taskDto.getWorkFlowNodeId(), taskDto.getTaskName(), JSON.toJSONString(taskDto));
        WorkflowNode workflowNode = workflowNodeService.getById(taskDto.getWorkFlowNodeId());

        // 启动前判断当前节点算法是否有模型
        this.checkAlgorithm(workflowNode);

        PublishTaskDeclareResponseDto respDto = new PublishTaskDeclareResponseDto();
        SubJobNode subJobNodeInfo = new SubJobNode();
        boolean isPublishSuccess = false;
        try {
            respDto = grpcTaskService.syncPublishTask(taskDto);
            if (GrpcConstant.GRPC_SUCCESS_CODE == respDto.getStatus()) {
                isPublishSuccess = true;
            }
            log.info("任务发布结果>>>>工作流id:{},任务名称：{},rosettanet收到处理任务，返回的taskId：{}", taskDto.getWorkFlowNodeId(), taskDto.getTaskName(), respDto.getTaskId());
        } catch (Exception e) {
            log.error("publish task fail, task name:{}, work flow nodeId:{},error msg:{}", taskDto.getTaskName(), taskDto.getWorkFlowNodeId(), e.getMessage(), e);
            if (workflowDto.isJobFlg()) {
                // 更新子作业
                this.updateSubJobInfo(workflowDto, false);
                // 更新子作业节点信息
                this.updateSubJobNodeInfo(workflowDto, workflowNode, false, respDto);
                return;
            } else {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_RUNNING_FAIL.getMsg());
            }
        }
        if (workflowDto.isJobFlg()) {
            //1.更新子作业
            this.updateSubJobInfo(workflowDto, isPublishSuccess);
            //2.更新子作业节点信息
            subJobNodeInfo = this.updateSubJobNodeInfo(workflowDto, workflowNode, isPublishSuccess, respDto);
        } else {
            //2.更新工作流节点
            workflowNode.setTaskId(isPublishSuccess ? respDto.getTaskId() : "");
            workflowNode.setRunStatus(isPublishSuccess ? WorkflowRunStatusEnum.RUNNING.getValue() : WorkflowRunStatusEnum.RUN_FAIL.getValue());
            workflowNode.setRunMsg(respDto.getMsg());
            // 更新最新修改时间
            workflowNode.setUpdateTime(new Date());
            workflowNodeService.updateById(workflowNode);
            //更新状态、最新修改时间
            workflow.setRunStatus(isPublishSuccess ? WorkflowRunStatusEnum.RUNNING.getValue() : WorkflowRunStatusEnum.RUN_FAIL.getValue());
            workflow.setUpdateTime(new Date());
            this.updateById(workflow);
            //更新元数据使用次数
            List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.getByWorkflowNodeId(workflowNode.getId());
            if (null != workflowNodeInputList && workflowNodeInputList.size() > 0) {
                List<String> inputDataList = new ArrayList<>();
                for(WorkflowNodeInput workflowNodeInput : workflowNodeInputList) {
                    inputDataList.add(workflowNodeInput.getDataTableId());
                }
                // 被使用的次数加1
                userMetaDataService.updateTimesByMetaDataId(inputDataList, workflowDto.getAddress());
            }
        }
        //4.如果不是最后一个节点，当前工作流执行成功，继续执行下一个工作流节点,放在redis中待下次处理
        boolean hasNext = workflowNode.getNextNodeStep() != null && workflowNode.getNextNodeStep() > 1;
        if (isPublishSuccess && hasNext) {
            workflowDto.setStartNode(workflowNode.getNextNodeStep());
            workflowDto.setTaskId(workflowDto.isJobFlg() ? subJobNodeInfo.getTaskId() : workflowNode.getTaskId());
            String taskKey = workflowDto.isJobFlg() ? SysConstant.REDIS_SUB_JOB_PREFIX_KEY : SysConstant.REDIS_WORKFLOW_PREFIX_KEY;
            redissonObject.setValue(taskKey + workflowDto.getTaskId(), workflowDto, sysConfig.getRedisTimeOut());
            log.info("多节点继续执行下个节点>>>>redis key:{},", taskKey + workflowDto.getTaskId());
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
                        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_DATA_ROWS_CHECK.getMsg());
                    }
                }
            }
        }
    }

    private void updateSign(WorkflowDto workflowDto) {
        LambdaUpdateWrapper<Workflow> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Workflow::getAddress, workflowDto.getAddress());
        updateWrapper.set(Workflow::getSign, workflowDto.getSign());
        updateWrapper.set(Workflow::getRunStatus, WorkflowRunStatusEnum.RUNNING.getValue());
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
    public List<TaskEventDto> getTaskEventList(Long workflowId) {
        List<TaskEventDto> dtoList = new ArrayList<>();
        List<WorkflowNode> workflowNodeList = workflowNodeService.getWorkflowNodeList(workflowId);
        if (null != workflowNodeList && workflowNodeList.size() > 0) {
            for (WorkflowNode workflowNode : workflowNodeList) {
                if (StrUtil.isNotBlank(workflowNode.getTaskId())) {
                    List<TaskEventDto> taskEventShowDtoList;
                    try {
                        taskEventShowDtoList = grpcTaskService.getTaskEventList(workflowNode.getTaskId());
                    }catch (Exception e) {
                        log.error("调用rpc接口异常--获取运行日志, workflowId:{}, 错误信息:{}", workflowId, e);
                        throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.RPC_INTERFACE_FAIL.getMsg());
                    }
                    dtoList.addAll(taskEventShowDtoList);
                }
            }
        }
        return dtoList;
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
            TerminateTaskRequestDto terminateTaskRequestDto = assemblyTerminateTaskRequestDto(workflow, workflowNode.getTaskId());
            TerminateTaskRespDto terminateTaskRespDto = null;
            for (int i = 0; i < 3; i++) {
                try {
                    terminateTaskRespDto = grpcTaskService.terminateTask(terminateTaskRequestDto);
                } catch (Exception e) {
                    log.error("终止工作流失败，失败原因：{}", e.getMessage());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        log.error("线程休眠失败，失败原因：{}", ex.getMessage());
                    }
                    continue;
                }
                break;
            }

            if (terminateTaskRespDto != null && terminateTaskRespDto.getStatus() == GrpcConstant.GRPC_SUCCESS_CODE) {
                this.updateRunStatus(workflowId, WorkflowRunStatusEnum.UN_RUN.getValue());
                workflowNodeService.updateRunStatusByWorkflowId(workflowId, workflowNode.getRunStatus(), WorkflowRunStatusEnum.UN_RUN.getValue());
            } else {
                log.error("Terminate workflow with id:{} fail", workflowId);
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_TERMINATE_NET_PROCESS_ERROR.getMsg());
            }
        }
    }

    @Override
    public TerminateTaskRequestDto assemblyTerminateTaskRequestDto(Workflow workflow, String taskId) {
        TerminateTaskRequestDto terminateTaskRequestDto = new TerminateTaskRequestDto();
        terminateTaskRequestDto.setUser(workflow.getAddress());
        terminateTaskRequestDto.setUserType(UserTypeEnum.checkUserType(workflow.getAddress()));
        terminateTaskRequestDto.setTaskId(taskId);
        terminateTaskRequestDto.setSign(workflow.getSign());
        return terminateTaskRequestDto;
    }

    @Override
    public void updateRunStatus(Long workflowId, Byte runStatus) {
        LambdaUpdateWrapper<Workflow> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Workflow::getRunStatus, runStatus);
        updateWrapper.eq(Workflow::getId, workflowId);
        this.update(updateWrapper);
    }

    /**
     * 组装发布任务请求对象
     *
     * @param workflowDto 工作流节点参数信息
     */
    @Override
    public TaskDto assemblyTaskDto(WorkflowDto workflowDto) {
        WorkflowNode workflowNode = workflowNodeService.getByWorkflowIdAndStep(workflowDto.getId(), workflowDto.getStartNode());

        //有指定当前工作流节点模型输入,获取工作流节点模型
        if (workflowNode.getModelId() != null && workflowNode.getModelId() > 0) {
            TaskResult taskResult = taskResultService.getById(workflowNode.getModelId());
            if (taskResult == null) {
                log.error("WorkflowServiceImpl->getDataSupplierList,fail reason:{}", ErrorMsg.WORKFLOW_NODE_TASK_RESULT_NOT_EXIST.getMsg());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_TASK_RESULT_NOT_EXIST.getMsg());
            }
            workflowDto.setPreTaskResult(taskResult);
        }

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
        taskDto.setUser(workflowDto.getAddress());
        //发起账户用户类型
        taskDto.setUserType(UserTypeEnum.checkUserType(workflowDto.getAddress()));
        //设置发起方
        taskDto.setSender(getSender(workflowNodeInputList));
        //任务算法提供方 组织信息
        taskDto.setAlgoSupplier(getAlgoSupplier(taskDto.getSender()));
        // 算力提供方 暂定三方
        taskDto.setPowerPartyIds(getPowerPartyIds());
        //数据提供方
        taskDto.setTaskDataSupplierDeclareDtoList(getDataSupplierList(workflowNodeInputList, organizationMap, workflowDto.getPreTaskResult()));
        //任务结果接受者
        taskDto.setTaskResultReceiverDeclareDtoList(getReceivers(workflowNodeOutputList, organizationMap));
        // 任务需要花费的资源声明
        taskDto.setResourceCostDeclareDto(getResourceCostDeclare(workflowNodeResource));
        //算法代码
        taskDto.setCalculateContractCode(calculateContractCode);
        //数据分片合约代码
        taskDto.setDataSplitContractCode(dataSplitContractCode);
        //合约调用的额外可变入参 (json 字符串, 根据算法来)
        taskDto.setContractExtraParams(getContractExtraParams(workflowNode.getAlgorithmId(), workflowDto.getPreTaskResult(), workflowNodeInputList));
        //发起任务的账户的签名
        taskDto.setSign(workflowDto.getSign());
        //任务描述 (非必须)
        taskDto.setDesc(workflowNode.getNodeName());

        return taskDto;
    }

    @Override
    public void updateRunStatus(Object[] ids, Byte runStatus) {
        LambdaUpdateWrapper<Workflow> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Workflow::getRunStatus, runStatus);
        updateWrapper.set(Workflow::getUpdateTime, now());
        updateWrapper.in(Workflow::getId, ids);
        this.update(updateWrapper);
    }

    /**
     * 更新子作业
     *
     * @param workflowDto      工作流请求信息
     * @param isPublishSuccess 节点是否发布成功
     */
    private void updateSubJobInfo(WorkflowDto workflowDto, boolean isPublishSuccess) {
        SubJob subJob = subJobService.getById(workflowDto.getSubJobId());
        subJob.setEndTime(now());
        subJob.setRunTime(String.valueOf(DateUtil.between(subJob.getBeginTime(), subJob.getEndTime(), DateUnit.MINUTE)));
        subJob.setSubJobStatus(isPublishSuccess ? SubJobStatusEnum.RUNNING.getValue() : SubJobStatusEnum.RUN_FAIL.getValue());
        subJobService.updateById(subJob);
    }

    /**
     * 记录子作业节点信息，存在则更新(子作业重启)，不存在则保存
     *
     * @param workflowDto      工作流请求信息
     * @param workflowNode     工作流节点
     * @param isPublishSuccess 节点是否发布成功
     * @param respDto          发布响应结果
     * @return SubJobNode      子作业节点
     */
    private SubJobNode updateSubJobNodeInfo(WorkflowDto workflowDto, WorkflowNode workflowNode, boolean isPublishSuccess, PublishTaskDeclareResponseDto respDto) {
        SubJobNode subJobNode = subJobNodeService.querySubJobNodeByJobIdAndNodeStep(workflowDto.getSubJobId(), workflowDto.getStartNode());
        if (Objects.isNull(subJobNode)) {
            subJobNode = new SubJobNode();
            subJobNode.setSubJobId(workflowDto.getSubJobId());
            subJobNode.setAlgorithmId(workflowNode.getAlgorithmId());
            subJobNode.setNodeStep(workflowNode.getNodeStep());
        }
        subJobNode.setRunStatus(isPublishSuccess ? SubJobNodeStatusEnum.RUNNING.getValue() : SubJobNodeStatusEnum.RUN_FAIL.getValue());
        subJobNode.setTaskId(isPublishSuccess ? respDto.getTaskId() : "");
        subJobNode.setRunMsg(respDto.getMsg());
        subJobNode.setUpdateTime(now());
        boolean isSuccess = subJobNodeService.saveOrUpdate(subJobNode);
        if (!isSuccess) {
            log.error("start sub job fail, is save sub job node. subJobNode:{}", JSON.toJSONString(subJobNode));
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.SUB_JOB_RESTART_FAILED_ERROR.getMsg());
        }
        return subJobNode;
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
     * @param algorithmId 算法id
     * @return 额外可变入参
     */
    private String getContractExtraParams(Long algorithmId, TaskResult preTaskResult, List<WorkflowNodeInput> workflowNodeInputList) {
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
                    if (input.getSenderFlag() == SenderFlagEnum.TRUE.getValue()) {
                        jsonObject.put("label_column",
                                metaDataDetailsService.getColumnIndexById(input.getDependentVariable()).getColumnName());
                    }
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
    private List<TaskDataSupplierDeclareDto> getDataSupplierList(List<WorkflowNodeInput> workflowNodeInputList, Map<String, Organization> organizationMap, TaskResult preTaskResult) {
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
            if (null != preTaskResult && SenderFlagEnum.TRUE.getValue() == input.getSenderFlag()) {
                taskDataSupplierDeclareDto = new TaskDataSupplierDeclareDto();
                OrganizationIdentityInfoDto modelOrg = new OrganizationIdentityInfoDto();
                BeanUtil.copyProperties(taskOrganizationIdentityInfoDto, modelOrg);
                modelOrg.setPartyId("p" + workflowNodeInputList.size());
                taskDataSupplierDeclareDto.setTaskOrganizationIdentityInfoDto(modelOrg);

                taskMetaDataDeclareDto = new TaskMetaDataDeclareDto();
                taskMetaDataDeclareDto.setMetaDataId(preTaskResult.getMetadataId());
                taskMetaDataDeclareDto.setKeyColumn(0);
                taskDataSupplierDeclareDto.setTaskMetaDataDeclareDto(taskMetaDataDeclareDto);
                taskDataSupplierDeclareDtoList.add(taskDataSupplierDeclareDto);
            }
        }
        //模型提供方
        return taskDataSupplierDeclareDtoList;
    }

    /**
     * 获取当前节点连接机构信息
     *
     * @param workflowNodeInputList 任务输入信息列表
     * @return 机构信息
     */
    private OrganizationIdentityInfoDto getSender(List<WorkflowNodeInput> workflowNodeInputList) {
        for (WorkflowNodeInput workflowNodeInput : workflowNodeInputList) {
            if (SenderFlagEnum.TRUE.getValue() == workflowNodeInput.getSenderFlag()) {
                OrganizationIdentityInfoDto sender = new OrganizationIdentityInfoDto();
                Organization organization = organizationService.getByIdentityId(workflowNodeInput.getIdentityId());
                sender.setPartyId("s0");
                sender.setNodeName(organization.getNodeName());
                sender.setNodeId(organization.getNodeId());
                sender.setIdentityId(workflowNodeInput.getIdentityId());
                return sender;
            }
        }

        log.error("获取当前工作流节点输入信息中不存发起方，请核对信息:{}", workflowNodeInputList);
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
     * 校验是否有编辑权限
     */
    private void checkEditPermission(Long projectId) {
        Byte role = projectService.getRoleByProjectId(projectId);
        if (null == role || ProjectMemberRoleEnum.VIEW.getRoleId() == role) {
            log.error("checkEditPermission error:{}", ErrorMsg.USER_NOT_PERMISSION_ERROR.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_NOT_PERMISSION_ERROR.getMsg());
        }
    }

    @Override
    public Map<String, Object> getWorkflowStatusById(Long id) {
        Map<String, Object> param = new HashMap<>(2);
        Workflow workflow = queryWorkflowDetail(id);
        List<WorkflowNode> workflowNodeList = workflowNodeService.getWorkflowNodeList(id);
        List<Map<String, Object>> nodeList = new ArrayList<>();
        workflowNodeList.forEach(workflowNode -> {
            Map<String, Object> map = new HashMap<>(2);
            map.put("workflowNodeId", workflowNode.getId());
            map.put("runStatus", workflowNode.getRunStatus());
            map.put("runMsg", workflowNode.getRunMsg());
            nodeList.add(map);
        });
        param.put("runStatus", workflow.getRunStatus());
        param.put("nodeList", nodeList);
        return param;
    }
}
