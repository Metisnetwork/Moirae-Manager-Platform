package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.mapper.WorkflowNodeMapper;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private IAlgorithmVariableService algorithmVariableService;

    @Resource
    private IWorkflowNodeResourceService workflowNodeResourceService;

    @Override
    public void saveWorkflowNode(Long workflowId, List<WorkflowNode> workflowNodeList) {
        if (workflowNodeList.size() == 0) {
            return;
        }
        // 查询工作流节点，并获取所有节点的id
        List<Long> idList = new ArrayList<>();
        List<WorkflowNode> nodeList = getAllWorkflowNodeList(workflowId);
        for (WorkflowNode nodeObj : nodeList) {
            idList.add(nodeObj.getId());
        }
        // 过滤并删除不需要保存的节点，将需要保存的节点排序保存
        List<Long> delIdList = new ArrayList<>();
        for (WorkflowNode nodeReq : workflowNodeList) {
            if (idList.contains(nodeReq.getId())) {
                // 需要保存的节点按序号保存排序，并保持数据为生效状态
                WorkflowNode node = new WorkflowNode();
                node.setId(nodeReq.getId());
                node.setNodeStep(nodeReq.getNodeStep());
                node.setStatus((byte) 1);
                this.updateById(node);
                // 去掉需要保存的节点，保留需要删除的节点
                idList.remove(nodeReq.getId());
            }
        }
        // 将不需要保存的节点及所属数据物理删除
        removeWorkflowNode(idList);
    }

    /**
     * 删除不需要保存的工作流节点
     */
    private void removeWorkflowNode(List<Long> nodeIdList) {
        for (Long nodeId : nodeIdList) {
            // 删除节点代码
            workflowNodeCodeService.deleteByWorkflowNodeId(nodeId);
            // 删除输入
            workflowNodeInputService.deleteByWorkflowNodeId(nodeId);
            // 删除节点变量
            workflowNodeVariableService.deleteByWorkflowNodeId(nodeId);
            // 删除输出
            workflowNodeOutputService.deleteByWorkflowNodeId(nodeId);
            // 删除节点资源（环境）
            workflowNodeResourceService.deleteByWorkflowNodeId(nodeId);
        }
        // 将不需要保存的节点物理删除
        this.removeByIds(nodeIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWorkflowNode(WorkflowNode workflowNode) {
        // 查看工作流是否存在
        Workflow workflow = workflowService.getById(workflowNode.getWorkflowId());
        if (Objects.isNull(workflow)) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }

        /* 判断新增节点的上一节点是否存在 */
        // 上一节点序号
        int previous = workflowNode.getNodeStep() - 1;
        // 当前节点为第一节点
        if (previous <= 0) {
            // 当前节点为第一节点，直接插入即可
            this.save(workflowNode);
            return;
        }
        // 当前节点不是第一节点，查询当前节点的上一节点
        LambdaQueryWrapper<WorkflowNode> nodeWrapper = Wrappers.lambdaQuery();
        nodeWrapper.eq(WorkflowNode::getWorkflowId, workflowNode.getWorkflowId());
        nodeWrapper.eq(WorkflowNode::getNodeStep, previous);
        WorkflowNode preWorkflowNode = this.getOne(nodeWrapper);
        // 无上一节点，抛出异常
        if (Objects.isNull(preWorkflowNode)) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_EXIST.getMsg());
        }
        // 上一节点无后续节点，尾部直接插入即可
        if (preWorkflowNode.getNextNodeStep() == null || preWorkflowNode.getNextNodeStep() == 0) {
            this.save(workflowNode);
            return;
        }
        // 上一节点有后续节点，需将插入节点位置的所有后续节点向后移一位
        // 查询当前工作流中所有节点
        List<WorkflowNode> workflowNodeList = getWorkflowNodeList(workflowNode.getWorkflowId());
        for (WorkflowNode node : workflowNodeList) {
            // 找出插入节点位及之后的所有节点，将后续所有节点依次后移1位
            if (node.getNodeStep() >= workflowNode.getNodeStep()) {
                // 修改节点和后续节点序号值
                node.setNodeStep(node.getNodeStep() + 1);
                if (node.getNextNodeStep() == null || node.getNextNodeStep() == 0) {
                    // 最后一个节点，后续节点字段置空
                    node.setNextNodeStep(null);
                    this.updateById(node);
                    continue;
                }
                node.setNextNodeStep(node.getNextNodeStep() + 1);
                this.updateById(node);
            }
        }
        // 中间插入，设置后续节点字段的值
        workflowNode.setNextNodeStep(workflowNode.getNodeStep() + 1);
        this.save(workflowNode);

        // 设置当前工作流节点数
        workflow.setNodeNumber(workflow.getNodeNumber() + 1);
        workflowService.updateById(workflow);
    }

    @Override
    public void renameWorkflowNode(Long nodeId, String nodeName) {
        WorkflowNode workflowNode = this.getById(nodeId);
        if (null == workflowNode) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_EXIST.getMsg());
        }
        workflowNode.setNodeName(nodeName);
        this.updateById(workflowNode);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteWorkflowNode(Long id) {
        // 删除节点代码
        workflowNodeCodeService.deleteByWorkflowNodeId(id);
        // 删除输入
        workflowNodeInputService.deleteByWorkflowNodeId(id);
        // 删除节点变量
        workflowNodeVariableService.deleteByWorkflowNodeId(id);
        // 删除输出
        workflowNodeOutputService.deleteByWorkflowNodeId(id);
        // 删除节点资源（环境）
        workflowNodeResourceService.deleteByWorkflowNodeId(id);
        /* 删除当前节点 如果当前节点为最后一个节点直接删除即可，
           如果当前节点不是最后节点，则需将处理当前节点的后续所有节点，
           将当前节点所有后的节点序号和下一节点序号字段向前移动一位 */
        // 查询当前节点
        WorkflowNode workflowNode = this.getById(id);
        // 当前节点为空，直接结束方法
        if (Objects.isNull(workflowNode)) {
            return;
        }
        // 查询当前工作流所有节点
        List<WorkflowNode> workflowNodeList = getWorkflowNodeList(workflowNode.getWorkflowId());
        // 只有一个节点直接删除即可
        if (workflowNodeList.size() == 1) {
            this.removeById(id);
            return;
        }
        // 多个节点，是最后节点，直接删除，并将上一节点的下一节点序号字段置空
        if (workflowNode.getNextNodeStep() == null || workflowNode.getNextNodeStep() == 0) {
            // 修改上一节点的下一节点序号
            WorkflowNode lastNode = workflowNodeList.get(workflowNodeList.size() - 1);
            lastNode.setNextNodeStep(null);
            this.updateById(lastNode);
            // 删除当前节点
            this.removeById(id);
            return;
        }
        // 多个节点，不是最后节点，删除当前节点并处理后续节点
        for (WorkflowNode node : workflowNodeList) {
            // 将删除节点后的所有节点，向前移动一位
            if (node.getNodeStep() > workflowNode.getNodeStep()) {
                node.setNodeStep(node.getNodeStep() - 1);
                if (node.getNextNodeStep() == null || node.getNextNodeStep() == 0) {
                    node.setNextNodeStep(null);
                    this.updateById(node);
                    continue;
                }
                node.setNextNodeStep(node.getNextNodeStep() - 1);
                this.updateById(node);
            }
        }
        this.removeById(id);
        // 修改工作流节点数
        Workflow workflow = workflowService.getById(workflowNode.getWorkflowId());
        workflow.setNodeNumber(workflow.getNodeNumber() - 1);
        workflowService.updateById(workflow);
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
    public List<WorkflowNode> getAllWorkflowNodeList(Long workflowId) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        // 所有节点正序排序
        wrapper.orderByAsc(WorkflowNode::getNodeStep);
        return this.list(wrapper);
    }

    @Override
    public List<WorkflowNode> getWorkflowNodeList(Long workflowId) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        wrapper.eq(WorkflowNode::getStatus, 1);
        // 所有节点正序排序
        wrapper.orderByAsc(WorkflowNode::getNodeStep);
        return this.list(wrapper);
    }

    @Override
    public void addWorkflowNodeCode(WorkflowNodeCode workflowNodeCode) {
        LambdaQueryWrapper<WorkflowNodeCode> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkflowNodeCode::getWorkflowNodeId, workflowNodeCode.getWorkflowNodeId());
        WorkflowNodeCode workflowNodeCodeOld = workflowNodeCodeService.getOne(queryWrapper);
        // 如果已存在节点算法代码则修改
        if (Objects.nonNull(workflowNodeCodeOld)) {
            workflowNodeCodeOld.setEditType(workflowNodeCode.getEditType());
            workflowNodeCodeOld.setCalculateContractCode(workflowNodeCode.getCalculateContractCode());
            workflowNodeCodeOld.setDataSplitContractCode(workflowNodeCode.getDataSplitContractCode());
            workflowNodeCodeService.updateById(workflowNodeCodeOld);
            return;
        }
        // 不存在算法代码新增数据
        workflowNodeCodeService.save(workflowNodeCode);
    }

    @Override
    public void addWorkflowNodeResource(WorkflowNodeResource workflowNodeResource) {
        LambdaQueryWrapper<WorkflowNodeResource> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkflowNodeResource::getWorkflowNodeId, workflowNodeResource.getWorkflowNodeId());
        WorkflowNodeResource resourceOld = workflowNodeResourceService.getOne(queryWrapper);
        // 如果已存在节点算法资源，则修改
        if (Objects.nonNull(resourceOld)) {
            resourceOld.setCostMem(workflowNodeResource.getCostMem());
            resourceOld.setCostCpu(workflowNodeResource.getCostCpu());
            resourceOld.setCostGpu(workflowNodeResource.getCostGpu());
            resourceOld.setCostBandwidth(workflowNodeResource.getCostBandwidth());
            resourceOld.setRunTime(workflowNodeResource.getRunTime());
            workflowNodeResourceService.updateById(resourceOld);
            return;
        }
        // 不存在算法代码新增数据
        workflowNodeResourceService.save(workflowNodeResource);
    }

    @Override
    public void copySaveWorkflowNode(Long newWorkflowId, List<WorkflowNode> workflowNodeOldList) {
        List<WorkflowNode> newNodeList = new ArrayList<>();
        workflowNodeOldList.forEach(oldNode -> {
            WorkflowNode newNode = new WorkflowNode();
            newNode.setWorkflowId(newWorkflowId);
            newNode.setAlgorithmId(oldNode.getAlgorithmId());
            newNode.setNodeName(oldNode.getNodeName());
            newNode.setNodeStep(oldNode.getNodeStep());
            newNode.setNextNodeStep(oldNode.getNextNodeStep());
            newNodeList.add(newNode);
        });
        this.saveBatch(newNodeList);
    }

    @Override
    public void addWorkflowNodeByTemplate(Long workflowId, List<WorkflowNodeTemp> workflowNodeTempList) {
        WorkflowNode workflowNode;
        for (WorkflowNodeTemp workflowNodeTemp : workflowNodeTempList) {
            workflowNode = new WorkflowNode();
            workflowNode.setWorkflowId(workflowId);
            workflowNode.setNodeName(workflowNodeTemp.getNodeName());
            workflowNode.setAlgorithmId(workflowNodeTemp.getAlgorithmId());
            workflowNode.setNodeStep(workflowNodeTemp.getNodeStep());
            workflowNode.setNextNodeStep(workflowNodeTemp.getNextNodeStep());
            //保存工作流节点
            this.save(workflowNode);
            //添加工作流节点代码
            workflowNodeCodeService.addByAlgorithmIdAndWorkflowNodeId(workflowNodeTemp.getAlgorithmId(), workflowNode.getId());
            //查询节点代码对应的算法列表
            List<AlgorithmVariable> algorithmVariableList = algorithmVariableService.listByAlgorithmId(workflowNodeTemp.getAlgorithmId());
            //保存工作流输入变量
            workflowNodeVariableService.addByAlgorithmVariableList(workflowNode.getId(), algorithmVariableList);
        }
    }
}
