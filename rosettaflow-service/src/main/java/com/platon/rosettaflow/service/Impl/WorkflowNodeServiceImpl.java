package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.*;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.mapper.WorkflowNodeMapper;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.service.*;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/31
 * @description 工作流节点服务实现类
 */
@Slf4j
@Service
public class WorkflowNodeServiceImpl extends ServiceImpl<WorkflowNodeMapper, WorkflowNode> implements IWorkflowNodeService {

    @Resource
    private IWorkflowService workflowService;

    @Resource
    private IAlgorithmService algorithmService;

    @Resource
    private IAlgorithmAuthService algorithmAuthService;

    @Resource
    private IWorkflowNodeCodeService workflowNodeCodeService;

    @Resource
    private IAlgorithmCodeService algorithmCodeService;

    @Resource
    private IWorkflowNodeInputService workflowNodeInputService;

    @Resource
    private IWorkflowNodeOutputService workflowNodeOutputService;

    @Resource
    private IWorkflowNodeVariableService workflowNodeVariableService;

    @Resource
    private IAlgorithmVariableService algorithmVariableService;

    @Resource
    private IWorkflowNodeResourceService workflowNodeResourceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long workflowId, Long algorithmId, Integer nodeStep) {
        //查看工作流是否存在
        Workflow workflow = workflowService.getById(workflowId);
        if (null == workflow) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }

        //查看算法是否授权
        LambdaQueryWrapper<AlgorithmAuth> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AlgorithmAuth::getAlgorithmId, algorithmId);
        Long userId = UserContext.get().getId();
        if (null == userId) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, RespCodeEnum.UN_LOGIN.getMsg());
        }
        wrapper.eq(AlgorithmAuth::getUserId, userId);
        wrapper.eq(AlgorithmAuth::getAuthStatus, AlgorithmAuthStatusEnum.AUTH.getValue());
        wrapper.le(AlgorithmAuth::getAuthBeginTime, DateUtil.date());
        wrapper.ge(AlgorithmAuth::getAuthEndTime, DateUtil.date());
        AlgorithmAuth algorithmAuth = algorithmAuthService.getOne(wrapper);
        if (null == algorithmAuth) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ALGORITHM_AUTH_NOT_EXIST.getMsg());
        }

        //查看运行步骤是否存在
        LambdaQueryWrapper<WorkflowNode> nodeWrapper = Wrappers.lambdaQuery();
        nodeWrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        nodeWrapper.eq(WorkflowNode::getNodeStep, nodeStep);
        WorkflowNode workflowNode = this.getOne(nodeWrapper);
        if (null == workflowNode) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_EXIST.getMsg());
        }
        //获取算法信息
        Algorithm algorithm = algorithmService.getById(algorithmId);
        //获取算法代码
        AlgorithmCode algorithmCode = algorithmCodeService.getByAlgorithmId(algorithmId);
        //获取算法自变量及因变量
        AlgorithmVariable algorithmVariable = algorithmVariableService.getByAlgorithmId(algorithmId);

        WorkflowNode newNode = new WorkflowNode();
        newNode.setWorkflowId(workflowId);
        newNode.setNodeName(algorithm.getAlgorithmName());
        newNode.setAlgorithmId(algorithmId);
        newNode.setNodeStep(nodeStep);
        newNode.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
        newNode.setTaskId("");
        newNode.setRunMsg("");
        newNode.setStatus(StatusEnum.VALID.getValue());
        this.save(newNode);

        //保存工作流节点代码
        WorkflowNodeCode workflowNodeCode = new WorkflowNodeCode();
        workflowNodeCode.setWorkflowNodeId(newNode.getId());
        workflowNodeCode.setEditType(AlgorithmCodeEditTypeEnum.NOTEBOOK.getValue());
        workflowNodeCode.setCalculateContractCode(algorithmCode.getCalculateContractCode());
        workflowNodeCode.setDataSplitContractCode(algorithmCode.getDataSplitContractCode());
        workflowNodeCode.setStatus(StatusEnum.VALID.getValue());
        workflowNodeCodeService.save(workflowNodeCode);

        //保存工作流节点变量
        WorkflowNodeVariable workflowNodeVariable = new WorkflowNodeVariable();
        workflowNodeVariable.setWorkflowNodeId(newNode.getId());
        workflowNodeVariable.setVarNodeType(algorithmVariable.getVarType());
        workflowNodeVariable.setVarNodeKey(algorithmVariable.getVarKey());
        workflowNodeVariable.setVarNodeValue(algorithmVariable.getVarValue());
        workflowNodeVariable.setVarNodeDesc(algorithmVariable.getVarDesc());
        workflowNodeVariable.setStatus(StatusEnum.VALID.getValue());
        workflowNodeVariableService.save(workflowNodeVariable);
    }

    @Override
    public WorkflowNode getByWorkflowIdAndStep(Long workflowId, Integer nodeStep) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getId, workflowId);
        wrapper.eq(WorkflowNode::getNodeStep, nodeStep);
        WorkflowNode workflowNode = this.getOne(wrapper);
        if (workflowNode == null) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_EXIST.getMsg());
        }
        return workflowNode;
    }

    @Override
    public List<WorkflowNode> getByWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        wrapper.orderByAsc(WorkflowNode::getNodeStep);
        return this.list(wrapper);
    }

    @Override
    public void rename(Long workflowId, Integer nodeStep, String nodeName) {
        WorkflowNode workflowNode = this.getByWorkflowIdAndStep(workflowId, nodeStep);
        if (null == workflowNode) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_EXIST.getMsg());
        }
        workflowNode.setNodeName(nodeName);
        this.save(workflowNode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //删除节点代码
        workflowNodeCodeService.deleteByWorkflowNodeId(id);

        //删除输入
        workflowNodeInputService.deleteByWorkflowNodeId(id);

        //删除节点变量
        workflowNodeVariableService.deleteByWorkflowNodeId(id);

        //删除输出
        workflowNodeOutputService.deleteByWorkflowNodeId(id);

        //删除节点资源（环境）
        workflowNodeResourceService.deleteByWorkflowNodeId(id);

        //删除节点
        this.removeById(id);
    }
}
