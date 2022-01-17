package com.moirae.rosettaflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.*;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.dto.WorkflowNodeDto;
import com.moirae.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.moirae.rosettaflow.grpc.service.GrpcAuthService;
import com.moirae.rosettaflow.mapper.WorkflowNodeMapper;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 工作流节点服务实现类
 *
 * @author hudenian
 * @date 2021/8/31
 */
@Slf4j
@Service
public class WorkflowNodeServiceImpl extends ServiceImpl<WorkflowNodeMapper, WorkflowNode> implements IWorkflowNodeService {

    @Resource
    private IProjectService projectService;

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private IAlgorithmService algorithmService;


    @Resource
    private IWorkflowNodeCodeService workflowNodeCodeService;

    @Resource
    private IWorkflowNodeInputService workflowNodeInputService;

    @Resource
    private IWorkflowNodeOutputService workflowNodeOutputService;

    @Resource
    private IWorkflowNodeVariableService workflowNodeVariableService;

    @Resource
    private IWorkflowNodeResourceService workflowNodeResourceService;

    @Resource
    private GrpcAuthService grpcAuthService;

    @Resource
    private IOrganizationService organizationService;

    @Resource
    private IUserMetaDataService userMetaDataService;

    @Override
    public List<WorkflowNode> getWorkflowNodeList(Long workflowId) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
//        wrapper.eq(WorkflowNode::getStatus, StatusEnum.VALID.getValue());
        // 所有节点正序排序
        wrapper.orderByAsc(WorkflowNode::getNodeStep);
        return this.list(wrapper);
    }

    @Override
    public void saveCopyWorkflowNodeTemp(Long newWorkflowId, List<WorkflowNode> oldNodeList) {
        oldNodeList.forEach(oldNode -> this.copyWorkflowNode(oldNode, newWorkflowId));
    }

    /** 复制工作流节点 */
    private WorkflowNode copyWorkflowNode(WorkflowNode oldNode, Long newWorkflowId) {
        WorkflowNode newNode = new WorkflowNode();
        newNode.setWorkflowId(newWorkflowId);
        newNode.setAlgorithmId(oldNode.getAlgorithmId());
        newNode.setNodeName(oldNode.getNodeName());
        newNode.setNodeStep(oldNode.getNodeStep());
//        newNode.setNextNodeStep(oldNode.getNextNodeStep());
        this.save(newNode);
        return newNode;
    }

    /**
     * 保存节点相关数据
     */
    private void saveNodeData(List<WorkflowNodeInput> newNodeInputList,
                              List<WorkflowNodeOutput> newNodeOutputList,
                              List<WorkflowNodeCode> newNodeCodeList,
                              List<WorkflowNodeResource> newNodeResourceList,
                              List<WorkflowNodeVariable> newNodeVariableList) {
        // 保存节点输入数据
        if (newNodeInputList.size() > 0) {
            workflowNodeInputService.batchInsert(newNodeInputList);
        }
        // 保存节点输出数据
        if (newNodeOutputList.size() > 0) {
            workflowNodeOutputService.batchInsert(newNodeOutputList);
        }
        // 保存节点算法代码
        if (newNodeCodeList.size() > 0) {
            workflowNodeCodeService.batchInsert(newNodeCodeList);
        }
        // 保存节点环境资源
        if (newNodeResourceList.size() > 0) {
            workflowNodeResourceService.batchInsert(newNodeResourceList);
        }
        // 保存节点变量
        if (newNodeVariableList.size() > 0) {
            workflowNodeVariableService.batchInsert(newNodeVariableList);
        }
    }

    @Override
    public WorkflowNode getRunningNodeByWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        wrapper.eq(WorkflowNode::getRunStatus, WorkflowRunStatusEnum.RUNNING.getValue());
//        wrapper.eq(WorkflowNode::getStatus, StatusEnum.VALID.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public void updateRunStatusByWorkflowId(Long workflowId, Byte oldRunStatus, Byte newRunStatus) {
        LambdaUpdateWrapper<WorkflowNode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WorkflowNode::getRunStatus, newRunStatus);
        updateWrapper.eq(WorkflowNode::getRunStatus, oldRunStatus);
        updateWrapper.eq(WorkflowNode::getWorkflowId, workflowId);
//        updateWrapper.eq(WorkflowNode::getStatus, StatusEnum.VALID.getValue());
        this.update(updateWrapper);
    }

    @Override
    public void updateRunStatus(Object[] ids, Byte runStatus) {
        LambdaUpdateWrapper<WorkflowNode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WorkflowNode::getRunStatus, runStatus);
//        updateWrapper.set(WorkflowNode::getUpdateTime, now());
        updateWrapper.in(WorkflowNode::getId, ids);
        this.update(updateWrapper);
    }

    @Override
    public List<WorkflowNode> queryByWorkflowIdAndVersion(Long workflowId, Integer version) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        wrapper.eq(WorkflowNode::getWorkflowEditVersion, version);
        return this.list(wrapper);
    }

    /**
     * 校验是否有编辑权限
     */
    private void checkEditPermission(Long projectId) {
        Byte role = projectService.getRoleByProjectId(projectId);
        if (null == role || ProjectMemberRoleEnum.VIEW.getRoleId() == role) {
            log.error("Current not permission to edit current project");
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.USER_NOT_PERMISSION_ERROR.getMsg());
        }
    }


}
