package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.enums.WorkflowRunStatusEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.common.utils.BeanCopierUtils;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.dto.WorkflowDto;
import com.platon.rosettaflow.dto.WorkflowNodeDto;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.OrganizationIdentityInfoDto;
import com.platon.rosettaflow.grpc.service.GrpcTaskService;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDataSupplierDeclareDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskMetaDataDeclareDto;
import com.platon.rosettaflow.grpc.task.req.dto.TaskResourceCostDeclareDto;
import com.platon.rosettaflow.mapper.WorkflowMapper;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.service.*;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private IAlgorithmService algorithmService;

    @Resource
    private IAlgorithmCodeService algorithmCodeService;

    @Resource
    private IAlgorithmVariableService algorithmVariableService;

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

    @Override
    public IPage<WorkflowDto> queryWorkFlowList(Long projectId, String workflowName, Long current, Long size) {
        IPage<WorkflowDto> page = new Page<>(current, size);
        return this.baseMapper.queryWorkFlowList(projectId, workflowName, page);
    }

    @Override
    public WorkflowDto queryWorkflowDetail(Long id) {
        // 获取工作流
        WorkflowDto workflowDto = BeanUtil.toBean(this.getById(id), WorkflowDto.class);
        // 获取工作流节点列表
        List<WorkflowNode> workflowNodeList = workflowNodeService.getWorkflowNodeList(id);
        if (workflowNodeList == null || workflowNodeList.size() == 0) {
            return workflowDto;
        }
        List<WorkflowNodeDto> workflowNodeDtoList = new ArrayList<>();
        for (WorkflowNode workflowNode : workflowNodeList) {
            // 工作流节点dto
            WorkflowNodeDto workflowNodeDto = BeanUtil.toBean(workflowNode, WorkflowNodeDto.class);
            // 算法对象
            AlgorithmDto algorithmDto = algorithmService.queryAlgorithmDetails(workflowNode.getAlgorithmId());
            if (Objects.nonNull(algorithmDto)) {
                // 算法代码, 如果可查询出算法代码，表示算法代码已修改，否则算法代码没有变动
                WorkflowNodeCode workflowNodeCode = workflowNodeCodeService.getByWorkflowNodeId(workflowNode.getId());
                if (Objects.nonNull(workflowNodeCode)) {
                    algorithmDto.setEditType(workflowNodeCode.getEditType());
                    algorithmDto.setAlgorithmCode(workflowNodeCode.getCalculateContractCode());
                    workflowNodeDto.setAlgorithmDto(algorithmDto);
                }
            }
            //工作流节点输入列表
            List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.getByWorkflowNodeId(workflowNode.getId());
            workflowNodeDto.setWorkflowNodeInputList(workflowNodeInputList);
            //工作流节点输出列表
            List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.getByWorkflowNodeId(workflowNode.getId());
            workflowNodeDto.setWorkflowNodeOutputList(workflowNodeOutputList);
            // 环境
            WorkflowNodeResource workflowNodeResource = workflowNodeResourceService.getByWorkflowNodeId(workflowNode.getId());
            if (Objects.nonNull(workflowNodeResource)) {
                workflowNodeDto.setWorkflowNodeResource(workflowNodeResource);
            }
            workflowNodeDtoList.add(workflowNodeDto);
        }
        workflowDto.setWorkflowNodeDtoList(workflowNodeDtoList);
        return workflowDto;
    }

    @Override
    public void addWorkflow(Workflow workflow) {
        try {
            workflow.setUserId(UserContext.get().getId());
            this.save(workflow);
        } catch (DuplicateKeyException dke) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
        }
    }

    @Override
    public void editWorkflow(Long id, String workflowName, String workflowDesc) {
        Workflow workflow = this.getById(id);
        if (Objects.isNull(workflow)) {
            log.error("workflow not found by id:{}", id);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }
        try {
            workflow.setWorkflowName(workflowName);
            workflow.setWorkflowDesc(workflowDesc);
            this.updateById(workflow);
        } catch (DuplicateKeyException dke) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
        }
    }

    @Override
    public void deleteWorkflow(Long id) {
        // 逻辑删除工作流，并修改版本标识
        Workflow workflow = new Workflow();
        workflow.setId(id);
        workflow.setDelVersion(id);
        workflow.setStatus((byte) 0);
        this.updateById(workflow);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void copyWorkflow(Long originId, String workflowName, String workflowDesc) {
        try {
            // 将复制的工作流数据id置空，新增一条新的工作流数据
            Long newWorkflowId = saveCopyWorkflow(originId, workflowName, workflowDesc);
            // 查询原工作流节点
            List<WorkflowNode> workflowNodeOldList = workflowNodeService.getWorkflowNodeList(originId);
            if (workflowNodeOldList == null || workflowNodeOldList.size() == 0) {
                return;
            }
            // 保存为新工作流节点
            workflowNodeService.copySaveWorkflowNode(newWorkflowId, workflowNodeOldList);
            // 复制算法、算法代码、算法变量
            for (WorkflowNode oldNode : workflowNodeOldList) {
                // 保存算法
                Long newAlgorithmId = algorithmService.copySaveAlgorithm(oldNode);
                // 保存算法代码(参数：源算法id、目的算法id)
                algorithmCodeService.copySaveAlgorithmCode(oldNode.getAlgorithmId(), newAlgorithmId);
                // 保存算法变量(参数：源算法id、目的算法id)
                algorithmVariableService.saveAlgorithmVariable(oldNode.getAlgorithmId(), newAlgorithmId);
            }
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
            }
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_EXIST.getMsg());
        }
    }

    /**
     * 将复制的工作流数据id置空，新增一条新的工作流数据
     */
    private Long saveCopyWorkflow(Long originId, String workflowName, String workflowDesc) {
        Workflow originWorkflow = this.getById(originId);
        if (Objects.isNull(originWorkflow)) {
            log.error("Origin workflow not found by id:{}", originId);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_ORIGIN_NOT_EXIST.getMsg());
        }
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
    public void start(WorkflowDto workflowDto) {
        Workflow orgWorkflow = this.getById(workflowDto.getId());
        if (orgWorkflow == null) {
            log.error("workflow not found by id:{}", workflowDto.getId());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }
        //截止节点必须
        if (orgWorkflow.getNodeNumber() < workflowDto.getEndNode()) {
            log.error("endNode is:{} can not more than workflow nodeNumber:{}", workflowDto.getEndNode(), orgWorkflow.getNodeNumber());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_END_NODE_OVERFLOW.getMsg());
        }

        //TODO 当有多个任务节点时，需要将第一个节点执行成功后，并获取执行结果数据做为下个节点入参，才可以继续执行后面节点
        //TODO 所以此处先执行第一个节点，后继节点在定时任务中，待第一个节点执行成功后再执行
        TaskDto taskDto = assemblyTaskDto(orgWorkflow.getId(), workflowDto.getStartNode());
        grpcTaskService.asyncPublishTask(taskDto, publishTaskDeclareResponse -> {
            //更新工作流节点
            WorkflowNode workflowNode = workflowNodeService.getById(taskDto.getWorkFlowNodeId());
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
                    //当前工作流执行成功，继续执行下一个节点工作流
                    //TODO 执行下一个工作流节点
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
        });
    }

    @Override
    public Long addWorkflowByTemplate(Long projectId, WorkflowTemp workflowTemp) {
        Workflow workflow = new Workflow();
        workflow.setProjectId(projectId);
        workflow.setWorkflowName(workflowTemp.getWorkflowName());
        workflow.setWorkflowDesc(workflowTemp.getWorkflowDesc());
        workflow.setNodeNumber(workflowTemp.getNodeNumber());
        workflow.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
        this.save(workflow);
        return workflow.getId();
    }

    /**
     * 组装发布任务请求对象
     *
     * @param workFlowId  工作流id
     * @param currentNode 工作流节点序号
     * @return 发布任务请求对象
     */
    public TaskDto assemblyTaskDto(Long workFlowId, Integer currentNode) {
        WorkflowNode workflowNode = workflowNodeService.getByWorkflowIdAndStep(workFlowId, currentNode);

        //获取工作流代码输入信息
        WorkflowNodeCode workflowNodeCode = workflowNodeCodeService.getByWorkflowNodeId(workflowNode.getId());
        if (workflowNodeCode == null) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_CODE_NOT_EXIST.getMsg());
        }

        //获取工作流节点输入信息
        List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.getByWorkflowNodeId(workflowNode.getId());

        //获取工作流节点输出信息
        List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.getByWorkflowNodeId(workflowNode.getId());

        //获取工作流节点自变量及因变量
        List<WorkflowNodeVariable> workflowNodeVariableList = workflowNodeVariableService.getByWorkflowNodeId(workflowNode.getId());

        //工作流节点资源表
        WorkflowNodeResource workflowNodeResource = workflowNodeResourceService.getByWorkflowNodeId(workflowNode.getId());

        TaskDto taskDto = new TaskDto();
        //TODO 拼装请求参数(待底层处理完成后完善)
        taskDto.setWorkFlowNodeId(workflowNode.getId());
        //任务名称
        taskDto.setTaskName(commonService.generateTaskName(workflowNode.getId()));

        //TODO 任务发起方（待底层接口调整）

        //算力提供方
        int i = 0;
        List<String> powerPartyIds = new ArrayList<>();

        //数据提供方
        List<TaskDataSupplierDeclareDto> taskDataSupplierDeclareDtoList = new ArrayList<>();
        TaskDataSupplierDeclareDto taskDataSupplierDeclareDto;

        OrganizationIdentityInfoDto taskOrganizationIdentityInfoDto;
        TaskMetaDataDeclareDto taskMetaDataDeclareDto;
        for (WorkflowNodeInput input : workflowNodeInputList) {
            taskOrganizationIdentityInfoDto = new OrganizationIdentityInfoDto();
            taskOrganizationIdentityInfoDto.setPartyId(input.getPartyId());
            taskOrganizationIdentityInfoDto.setNodeName(input.getIdentityName());
            taskOrganizationIdentityInfoDto.setNodeId(input.getNodeId());
            taskOrganizationIdentityInfoDto.setIdentityId(input.getIdentityId());

            taskMetaDataDeclareDto = new TaskMetaDataDeclareDto();
            taskMetaDataDeclareDto.setMetaDataId(input.getDataTableId());

            List<Integer> columnIndexList = new ArrayList<>();
            String[] columnIdsArr = input.getDataColumnIds().split(",");
            for (String s : columnIdsArr) {
                columnIndexList.add(Integer.valueOf(s.trim()));
            }
            taskMetaDataDeclareDto.setColumnIndexList(columnIndexList);

            taskDataSupplierDeclareDto = new TaskDataSupplierDeclareDto();
            taskDataSupplierDeclareDto.setTaskOrganizationIdentityInfoDto(taskOrganizationIdentityInfoDto);
            taskDataSupplierDeclareDto.setTaskMetaDataDeclareDto(taskMetaDataDeclareDto);

            taskDataSupplierDeclareDtoList.add(taskDataSupplierDeclareDto);
            //算力提供方标签 TODO 待确认
            powerPartyIds.add("p" + (++i));
        }
        taskDto.setTaskDataSupplierDeclareDtoList(taskDataSupplierDeclareDtoList);

        //设置魏算力提供方标签
        taskDto.setPowerPartyIds(powerPartyIds);

        //任务结果接受者(flow暂定)
//        List<TaskResultReceiverDeclareDto> taskResultReceiverDeclareDtoList = new ArrayList<>();
//        TaskResultReceiverDeclareDto taskResultReceiverDeclareDto = new TaskResultReceiverDeclareDto();
        OrganizationIdentityInfoDto organizationIdentityInfoDto;
        for (WorkflowNodeOutput output : workflowNodeOutputList) {
            organizationIdentityInfoDto = new OrganizationIdentityInfoDto();
            organizationIdentityInfoDto.setPartyId(output.getPartyId());
            organizationIdentityInfoDto.setNodeName(output.getIdentityName());
            organizationIdentityInfoDto.setNodeId(output.getNodeId());
            organizationIdentityInfoDto.setIdentityId(output.getIdentityId());

//            taskResultReceiverDeclareDto.setMemberInfo(organizationIdentityInfoDto);

//            taskResultReceiverDeclareDtoList.add(taskResultReceiverDeclareDto);
        }
//        taskDto.setTaskResultReceiverDeclareDtoList(taskResultReceiverDeclareDtoList);

        //任务的所需操作成本
        TaskResourceCostDeclareDto taskOperationCostDeclareDto = new TaskResourceCostDeclareDto();
        taskOperationCostDeclareDto.setCostMem(workflowNodeResource.getCostMem());
//        taskOperationCostDeclareDto.setCostProcessor(workflowNodeResource.getCostProcessor());
        taskOperationCostDeclareDto.setCostBandwidth(workflowNodeResource.getCostBandwidth());
        taskOperationCostDeclareDto.setDuration(workflowNodeResource.getRunTime());
//        taskDto.setTaskOperationCostDeclareDto(taskOperationCostDeclareDto);

        //算法代码（python代码）
        taskDto.setCalculateContractCode(workflowNodeCode.getCalculateContractCode());
        taskDto.setDataSplitContractCode(workflowNodeCode.getDataSplitContractCode());

        //算法额外参数
        JSONObject jsonObject = JSONUtil.createObj();
        for (WorkflowNodeVariable variable : workflowNodeVariableList) {
            jsonObject.set(variable.getVarNodeKey(), variable.getVarNodeValue());
        }
        taskDto.setContractExtraParams(jsonObject.toString());

        return taskDto;
    }

    IPage<WorkflowDto> convertToPageDto(Page<?> page) {
        List<WorkflowDto> records = new ArrayList<>();
        page.getRecords().forEach(r -> {
            WorkflowDto m = new WorkflowDto();
            BeanCopierUtils.copy(r, m);
            records.add(m);
        });

        IPage<WorkflowDto> pageDto = new Page<>();
        pageDto.setCurrent(page.getCurrent());
        pageDto.setRecords(records);
        pageDto.setSize(page.getSize());
        pageDto.setTotal(page.getTotal());
        return pageDto;
    }
}
