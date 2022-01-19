package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.grpc.service.GrpcTaskService;
import com.moirae.rosettaflow.grpc.task.req.dto.TaskEventDto;
import com.moirae.rosettaflow.grpc.task.req.dto.TerminateTaskRequestDto;
import com.moirae.rosettaflow.mapper.*;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    private IOrganizationService organizationService;
    @Resource
    private IUserMetaDataService userMetaDataService;
    @Resource
    private IWorkflowRunStatusService workflowRunStatusService;
    @Resource
    private IModelService modelService;

    @Resource
    private GrpcTaskService grpcTaskService;
    @Resource
    private NetManager netManager;
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
    public IPage<WorkflowDto> queryWorkFlowPageList(Long projectId, String workflowName, Long current, Long size) {
        IPage<WorkflowDto> page = new Page<>(current, size);
        this.checkAccessPermission(projectId);
        return this.baseMapper.queryWorkFlowAndStatusPageList(projectId, workflowName, page);
    }

    @Override
    public List<Workflow> queryListByProjectId(List<Long> projectIdList) {
        LambdaQueryWrapper<Workflow> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Workflow::getStatus, StatusEnum.VALID.getValue());
        queryWrapper.in(Workflow::getProjectId, projectIdList);
        return this.list(queryWrapper);
    }

    @Override
    public Workflow queryWorkflow(Long id) {
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
        if (isExistWorkflowName(workflow.getWorkflowName())) {
            log.info("addWorkflow--添加工作流名称已存在");
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NAME_EXIST.getMsg());
        }
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
        Workflow workflow = this.queryWorkflow(id);
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        if ((!workflow.getWorkflowName().equalsIgnoreCase(workflowName)) && isExistWorkflowName(workflowName)) {
            log.info("editWorkflow--编辑工作流名称已存在,workflowId:{}", id);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NAME_EXIST.getMsg());
        }
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
        Workflow workflow = this.queryWorkflow(id);
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        // 逻辑删除工作流，并修改版本标识
        workflow.setStatus(StatusEnum.UN_VALID.getValue());
        this.updateById(workflow);
    }

    @Override
    public Workflow addWorkflowByTemplate(Long projectId, Long userId, WorkflowTemp workflowTemp, String language) {
        Workflow workflow = new Workflow();
        workflow.setProjectId(projectId);
        workflow.setUserId(userId);
        // 处理国际化
        workflow.setWorkflowName(SysConstant.EN_US.equals(language) ? workflowTemp.getWorkflowNameEn() : workflowTemp.getWorkflowName());
        workflow.setWorkflowDesc(SysConstant.EN_US.equals(language) ? workflowTemp.getWorkflowDescEn() : workflowTemp.getWorkflowDesc());
        workflow.setEditVersion(1);
        workflow.setStatus(StatusEnum.VALID.getValue());
        this.save(workflow);
        return workflow;
    }

    @Override
    public List<TaskEventDto> getTaskEventList(Long workflowId) {
        Workflow workflow = getWorkflowStatusById(workflowId);
        List<TaskEventDto> dtoList = workflow.getGetNodeStatusVoList().stream()
                .flatMap(item ->{
                    List<TaskEventDto> taskEventShowDtoList = new ArrayList<>();
                    if (StrUtil.isNotBlank(item.getTaskId())) {
                        try {
                            taskEventShowDtoList = grpcTaskService.getTaskEventList(netManager.getChannel(item.getWorkflowNodeSenderIdentityId()), item.getTaskId());
                        } catch (Exception e) {
                            log.error("调用rpc接口异常--获取运行日志, workflowId:{}, 错误信息:{}", workflowId, e);
                            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.RPC_INTERFACE_FAIL.getMsg());
                        }
                    }
                    return taskEventShowDtoList.stream();
                })
                .collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public void terminate(Long workflowId) {
        Workflow workflow = getWorkflowStatusById(workflowId);
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        // 校验是否运行中
        if (workflow.getRunStatus() != WorkflowRunStatusEnum.RUNNING.getValue()) {
            log.error("workflow by id:{} is not running can not terminate", workflowId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_RUNNING.getMsg());
        }
        // 校验取消状态
        if (workflow.getCancelStatus() == WorkflowRunStatusEnum.RUNNING.getValue()) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELING.getMsg());
        }
        if (workflow.getCancelStatus() == WorkflowRunStatusEnum.RUN_SUCCESS.getValue()) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELLED_SUCCESS.getMsg());
        }
        if (workflow.getCancelStatus() == WorkflowRunStatusEnum.RUN_FAIL.getValue()) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_CANCELLED_FAIL.getMsg());
        }

        // 更新工作流运行状态
        workflowRunStatusService.updateCancelStatus(workflow.getWorkflowRunStatusId(), WorkflowRunStatusEnum.RUNNING.getValue());
    }

    @Override
    public boolean isExistWorkflowName(String workflowName) {
        LambdaQueryWrapper<Workflow> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Workflow::getWorkflowName, workflowName);
        queryWrapper.eq(Workflow::getStatus, StatusEnum.VALID.getValue());
        Workflow workflow = this.getOne(queryWrapper);
        return Objects.nonNull(workflow);
    }
    @Override
    public Workflow queryWorkflowDetailAndStatus(Long workflowId, String language) {
        // 查询工作流信息
        Workflow workflow = queryWorkflow(workflowId);
        // 获取工作流节点及最新执行状态列表
        List<WorkflowNode> workflowNodeList = workflowNodeMapper.queryWorkflowNodeAndStatusList(workflow.getId(), workflow.getEditVersion());
        workflow.setWorkflowNodeVoList(workflowNodeList);
        // 获得工作流节点配置的算法信息
        workflowNodeList.forEach(item -> {
            Algorithm algorithm = algorithmService.queryAlgorithmStepDetails(item.getAlgorithmId());
            if (Objects.nonNull(algorithm)) {
                // 处理国际化语言
                if (SysConstant.EN_US.equals(language)) {
                    algorithm.setAlgorithmName(algorithm.getAlgorithmNameEn());
                    algorithm.setAlgorithmDesc(algorithm.getAlgorithmDescEn());
                }
                // 工作流节点算法代码, 如果可查询出，表示已修改，否则没有变动
                WorkflowNodeCode workflowNodeCode = getWorkflowNodeCodeByNodeId(item.getWorkflowNodeId());
                if (Objects.nonNull(workflowNodeCode)) {
                    algorithm.setEditType(workflowNodeCode.getEditType());
                    algorithm.setCalculateContractCode(workflowNodeCode.getCalculateContractCode());
                }
                // 工作流节点算法资源环境, 如果可查询出，表示已修改，否则没有变动
                WorkflowNodeResource nodeResource = getWorkflowNodeResourceByNodeId(item.getWorkflowNodeId());
                if (Objects.nonNull(nodeResource)) {
                    if (null != nodeResource.getCostCpu()) {
                        algorithm.setCostCpu(nodeResource.getCostCpu());
                    }
                    if (null != nodeResource.getCostGpu()) {
                        algorithm.setCostGpu(nodeResource.getCostGpu());
                    }
                    if (null != nodeResource.getCostMem()) {
                        algorithm.setCostMem(nodeResource.getCostMem());
                    }
                    if (null != nodeResource.getCostBandwidth()) {
                        algorithm.setCostBandwidth(nodeResource.getCostBandwidth());
                    }
                    if (null != nodeResource.getRunTime()) {
                        algorithm.setRunTime(nodeResource.getRunTime());
                    }
                }
            }
            item.setNodeAlgorithmVo(algorithm);
        });

        // 获得工作流节点配置的输入信息
        workflowNodeList.forEach(item -> {
            List<WorkflowNodeInput> workflowNodeInputList = getWorkflowNodeInputByNodeId(item.getWorkflowNodeId());
            item.setWorkflowNodeInputVoList(workflowNodeInputList);
        });

        // 获得工作流节点配置的输出信息
        workflowNodeList.forEach(item -> {
            List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputMapper.getWorkflowNodeOutputAndOrgNameByNodeId(item.getWorkflowNodeId());
            item.setWorkflowNodeOutputVoList(workflowNodeOutputList);
        });

        // 获得工作流模型
        workflowNodeList.forEach(item -> {
            if(item.getModelId() != null && item.getModelId() > 0 ){
                Model model = modelService.getById(item.getModelId());
                model.setIdentityId(model.getOrgIdentityId());
                model.setModelId(model.getId());
                model.setFileName(model.getName());
                item.setModel(model);
            }
        });
        return workflow;
    }

    @Override
    public Workflow queryWorkflowDetail(Long workflowId, Integer version) {
        Workflow workflow = queryWorkflow(workflowId);
        List<WorkflowNode> workflowNodeList = workflowNodeService.queryByWorkflowIdAndVersion(workflowId, version);
        workflow.setWorkflowNodeReqList(workflowNodeList);
        for (WorkflowNode workflowNode : workflowNodeList) {
            WorkflowNodeCode workflowNodeCode = workflowNodeCodeService.queryByWorkflowNodeId(workflowNode.getId());
            List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.queryByWorkflowNodeId(workflowNode.getId());
            List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.queryByWorkflowNodeId(workflowNode.getId());
            WorkflowNodeResource workflowNodeResource = workflowNodeResourceService.queryByWorkflowNodeId(workflowNode.getId());
            List<WorkflowNodeVariable> workflowNodeVariables = workflowNodeVariableService.queryByWorkflowNodeId(workflowNode.getId());
            workflowNode.setWorkflowNodeCodeReq(workflowNodeCode);
            workflowNode.setWorkflowNodeInputReqList(workflowNodeInputList);
            workflowNode.setWorkflowNodeOutputReqList(workflowNodeOutputList);
            workflowNode.setWorkflowNodeResourceReq(workflowNodeResource);
            workflowNode.setWorkflowNodeVariableReqList(workflowNodeVariables);
        }
        return workflow;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkflowDetail(Workflow reqWorkflow) {
        saveWorkflowDetailInnner(reqWorkflow);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkflowDetailAndStart(Workflow workflow) {
        // 保存工作流
        Integer version = saveWorkflowDetailInnner(workflow);
        // 提交并执行工作流
        workflowRunStatusService.submitTaskAndExecute(workflow.getWorkflowId(), version, workflow.getAddress(), workflow.getSign());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void clearWorkflowNode(Long workflowId) {
        Workflow workflow = baseMapper.queryWorkFlowAndStatus(workflowId);
        if (Objects.isNull(workflow)) {
            return;
        }
        // 校验是否有编辑权限
        checkEditPermission(workflow.getProjectId());
        List<WorkflowNode> workflowNodeList = workflowNodeService.queryByWorkflowIdAndVersion(workflowId, workflow.getEditVersion());
        if (null == workflowNodeList || workflowNodeList.size() == 0) {
            return;
        }
        // 校验工作流状态是否运行中
        if(workflow.getRunStatus() == WorkflowRunStatusEnum.RUNNING.getValue()){
            log.error("Workflow runStatus is running or workflow node exist runStatus is running,can not clear, workflowId:{}", workflowId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_CLEAR.getMsg());
        }

        // 更新工作流版本号
        workflow.setEditVersion(workflow.getEditVersion() + 1);
        updateById(workflow);
    }

    @Override
    public Workflow getWorkflowStatusById(Long id) {
        // 查询工作流配置信息
        Workflow workflow = baseMapper.queryWorkFlowAndStatus(id);
        // 查询工作流节点配置信息
        List<WorkflowNode> workflowNodeList = workflowNodeMapper.queryWorkflowNodeAndStatusList(workflow.getId(), workflow.getEditVersion());
        workflow.setGetNodeStatusVoList(workflowNodeList);
        return workflow;
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

    /**
     * 校验当前用户是否有访问当前项目权限
     */
    private void checkAccessPermission(Long projectId) {
        Byte role = projectService.getRoleByProjectId(projectId);
        if (null == role || !ArrayUtils.contains(SysConstant.ROLE_BYTE_ARR, role)) {
            log.error("您无权访问当前项目--checkAccessPermission, projectId:{}, role:{}", projectId, role);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_ACCESS_PERMISSION_ERROR.getMsg());
        }
    }

    /**
     * 保存工作流设置
     *
     * @param reqWorkflow
     * @return 工作流编辑版本
     */
    private Integer saveWorkflowDetailInnner(Workflow reqWorkflow) {
        // 节点参数校验
        if (null == reqWorkflow.getWorkflowNodeReqList() || reqWorkflow.getWorkflowNodeReqList().size() == 0) {
            log.error("saveWorkflowAllNodeData--工作流节点信息workflowNodeDtoList不能为空");
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_EXIST.getMsg());
        }
        // 编辑权限校验
        Workflow workflow = baseMapper.queryWorkFlowAndStatus(reqWorkflow.getWorkflowId());
        checkEditPermission(workflow.getProjectId());
        // 工作流运行状态校验
        if (workflow.getRunStatus() == WorkflowRunStatusEnum.RUNNING.getValue()) {
            log.error("saveWorkflowNode--工作流运行中:{}", JSON.toJSONString(workflow));
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_RUNNING_EXIST.getMsg());
        }
        // 节点输入元数据数据校验
        Set<String> tableIdList = reqWorkflow.getWorkflowNodeReqList().stream()
                .flatMap(workflowNode -> workflowNode.getWorkflowNodeInputReqList().stream())
                .map(WorkflowNodeInput::getDataTableId)
                .collect(Collectors.toSet());
        if(!userMetaDataService.isValid(tableIdList)){
            log.error("有授权数据已过期，请检查, userAuthDataIdSet:{}", JSON.toJSONString(tableIdList));
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.METADATA_USER_DATA_EXPIRE.getMsg());
        }
        // 算法校验 - 模型输入校验（只有一个算法节点）
        if(reqWorkflow.getWorkflowNodeReqList().get(0).getInputModel() == SysConstant.INT_1
                && (reqWorkflow.getWorkflowNodeReqList().get(0).getModelId() == null
                || reqWorkflow.getWorkflowNodeReqList().get(0).getModelId() == 0
                || Objects.isNull(modelService.queryById(reqWorkflow.getWorkflowNodeReqList().get(0).getModelId())))){
            log.error("当前节点需输入模型，请检查, workflowNodeDto:{}", JSON.toJSONString(reqWorkflow.getWorkflowNodeReqList().get(0)));
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_MODEL_NOT_EXIST.getMsg());
        }
        // 算法校验 - 算法步骤
        List<Long> algorithmId = reqWorkflow.getWorkflowNodeReqList().stream()
                .map(WorkflowNode::getAlgorithmId)
                .collect(Collectors.toList());
        algorithmService.isValid(algorithmId);
        // 组织校验
        Set<String> senderOrgId = reqWorkflow.getWorkflowNodeReqList().stream()
                .map(WorkflowNode::getWorkflowNodeSenderIdentityId)
                .collect(Collectors.toSet());
        Set<String> dataOrgId = reqWorkflow.getWorkflowNodeReqList().stream()
                .flatMap(item -> item.getWorkflowNodeInputReqList().stream())
                .map(WorkflowNodeInput::getIdentityId)
                .collect(Collectors.toSet());
        senderOrgId.addAll(dataOrgId);
        organizationService.isValid(senderOrgId);
        // 模型必须存在于发起方上面
        reqWorkflow.getWorkflowNodeReqList().stream().forEach(item-> {
            // 如果用户指定模型文件，任务的发起方必须和指定的模型在同一个组织。
            if(item.getInputModel() == SysConstant.INT_1 && item.getModelId() > 0 ){
                Model model = modelService.getById(item.getModelId());
                if(!model.getOrgIdentityId().equals(item.getWorkflowNodeSenderIdentityId())){
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_SENDER_MODEL_IDENTITY_STEP1_ERROR.getMsg());
                }
            }
            // 如果用户指定模型为上个任务的输出， 任务的发起方必须在上个任务输出列表中。
            if(item.getInputModel() == SysConstant.INT_1 && item.getModelId() == 0){
                Set<String> preWorkflowNodeOutputOrg = reqWorkflow.getWorkflowNodeReqList().stream()
                        .filter( o -> o.getNodeStep() == item.getNodeStep() - 1)
                        .flatMap( o -> o.getWorkflowNodeOutputReqList().stream())
                        .map(WorkflowNodeOutput::getIdentityId)
                        .collect(Collectors.toSet());
                if(!preWorkflowNodeOutputOrg.contains(item.getWorkflowNodeSenderIdentityId())){
                    throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_SENDER_MODEL_IDENTITY_STEP2_ERROR.getMsg());
                }
            }
        });

        // 更新工作流设置的版本号
        workflow.setEditVersion(workflow.getEditVersion() + 1);
        updateById(workflow);
        // 插入工作流节点信息
        List<WorkflowNode> workflowNodeList = reqWorkflow.getWorkflowNodeReqList().stream()
                .map(item -> {
                    item.setId(null);
                    item.setWorkflowId(workflow.getId());
                    item.setWorkflowEditVersion(workflow.getEditVersion());
                    item.setSenderIdentityId(item.getWorkflowNodeSenderIdentityId());
                    workflowNodeService.save(item);
                    return item;
                })
                .collect(Collectors.toList());
        // 插入工作流节点输入信息
        List<WorkflowNodeInput> workflowNodeInputList = new ArrayList<>();
        workflowNodeList.forEach(item -> {
            for (int i = 0; i < item.getWorkflowNodeInputReqList().size(); i++) {
                WorkflowNodeInput input = item.getWorkflowNodeInputReqList().get(i);
                input.setId(null);
                input.setWorkflowNodeId(item.getId());
                input.setPartyId("p" + i);
                workflowNodeInputList.add(input);
            }
        });
        if(workflowNodeInputList.size()>0){
            workflowNodeInputMapper.batchInsert(workflowNodeInputList);
        }
        // 插入工作流节点输出信息
        List<WorkflowNodeOutput> workflowNodeOutputList = new ArrayList<>();
        workflowNodeList.forEach(item -> {
            for (int i = 0; i < item.getWorkflowNodeOutputReqList().size(); i++) {
                WorkflowNodeOutput output = item.getWorkflowNodeOutputReqList().get(i);
                output.setId(null);
                output.setWorkflowNodeId(item.getId());
                output.setPartyId("q" + i);
                workflowNodeOutputList.add(output);
            }
        });
        if(workflowNodeOutputList.size()>0){
            workflowNodeOutputMapper.batchInsert(workflowNodeOutputList);
        }
        // 插入工作流节点代码信息
        List<WorkflowNodeCode> workflowNodeCodeList = new ArrayList<>();
        workflowNodeList.forEach(item -> {
            WorkflowNodeCode workflowNodeCode = item.getWorkflowNodeCodeReq();
            workflowNodeCode.setId(null);
            workflowNodeCode.setWorkflowNodeId(item.getId());
            workflowNodeCodeList.add(workflowNodeCode);
        });
        if(workflowNodeCodeList.size()>0){
            workflowNodeCodeMapper.batchInsert(workflowNodeCodeList);
        }
        // 插入工作流节点资源信息
        List<WorkflowNodeResource> workflowNodeResourceList = new ArrayList<>();
        workflowNodeList.forEach(item -> {
            WorkflowNodeResource workflowNodeResource = item.getWorkflowNodeResourceReq();
            workflowNodeResource.setId(null);
            workflowNodeResource.setWorkflowNodeId(item.getId());
            workflowNodeResourceList.add(workflowNodeResource);
        });
        if(workflowNodeResourceList.size()>0){
            workflowNodeResourceMapper.batchInsert(workflowNodeResourceList);
        }

        return workflow.getEditVersion();
    }

    private List<WorkflowNodeInput> getWorkflowNodeInputByNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeInput> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeInput::getWorkflowNodeId, workflowNodeId);
        wrapper.orderByAsc(WorkflowNodeInput::getPartyId);
        return workflowNodeInputMapper.selectList(wrapper);
    }

    private WorkflowNodeResource getWorkflowNodeResourceByNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeResource> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeResource::getWorkflowNodeId, workflowNodeId);
        return workflowNodeResourceMapper.selectOne(wrapper);
    }

    private WorkflowNodeCode getWorkflowNodeCodeByNodeId(Long workflowNodeId) {
        LambdaQueryWrapper<WorkflowNodeCode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeId);
        return workflowNodeCodeMapper.selectOne(wrapper);
    }
}
