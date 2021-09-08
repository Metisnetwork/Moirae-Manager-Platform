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
        List<WorkflowNode> nodeBatchList = new ArrayList<>();
        int count = 0;
        for (WorkflowNode nodeReq : workflowNodeList) {
            if (idList.contains(nodeReq.getId())) {
                // 需要保存的节点按序号保存排序，并保持数据为生效状态
                WorkflowNode node = new WorkflowNode();
                node.setId(nodeReq.getId());
                node.setNodeStep(nodeReq.getNodeStep());
                node.setNextNodeStep(nodeReq.getNodeStep() + 1);
                if (++count == workflowNodeList.size()) {
                    // 将最后一个节点步骤的下一节点步骤字段值置空
                    node.setNextNodeStep(null);
                }
                node.setStatus((byte)1);
                nodeBatchList.add(node);
                // 去掉idList中需要保存的节点id，保留需要物理删除的节点
                idList.remove(nodeReq.getId());
            }
        }
        this.updateBatchById(nodeBatchList);
        // 将不需要保存的节点及所属数据物理删除
        removeWorkflowNode(idList);
        // 保存当前工作流节点数
        Workflow workflow = new Workflow();
        workflow.setId(workflowId);
        workflow.setNodeNumber(count);
        workflowService.updateById(workflow);
    }

    /** 物理删除不需要保存的工作流节点 */
    private void removeWorkflowNode(List<Long> nodeIdList) {
        if (nodeIdList == null || nodeIdList.size() == 0) {
            return;
        }
        for (Long nodeId : nodeIdList) {
            // 物理删除节点代码
            workflowNodeCodeService.deleteByWorkflowNodeId(nodeId);
            // 物理删除输入
            workflowNodeInputService.deleteByWorkflowNodeId(nodeId);
            // 物理删除节点变量
            workflowNodeVariableService.deleteByWorkflowNodeId(nodeId);
            // 物理删除输出
            workflowNodeOutputService.deleteByWorkflowNodeId(nodeId);
            // 物理删除节点资源（环境）
            workflowNodeResourceService.deleteByWorkflowNodeId(nodeId);
        }
        // 将不需要保存的节点物理删除
        this.removeByIds(nodeIdList);
    }

    @Override
    public void clearWorkflowNode(Long workflowId) {
        List<WorkflowNode> workflowNodeList = getAllWorkflowNodeList(workflowId);
        // 工作流节点id集合
        List<Long> nodeIdList = new ArrayList<>();
        // 输入id集合
        List<Long> inputList = new ArrayList<>();
        // 输出id集合
        List<Long> outputList = new ArrayList<>();
        for (WorkflowNode workflowNode : workflowNodeList) {
            Long nodeId = workflowNode.getId();
            nodeIdList.add(nodeId);
            List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.getByWorkflowNodeId(nodeId);
            if (workflowNodeInputList == null || workflowNodeInputList.size() == 0) {
                continue;
            }
            for (WorkflowNodeInput workflowNodeInput : workflowNodeInputList) {
                inputList.add(workflowNodeInput.getId());
            }
            List<WorkflowNodeOutput> workflowNodeOutputList  = workflowNodeOutputService.getByWorkflowNodeId(nodeId);
            if (workflowNodeOutputList == null || workflowNodeOutputList.size() == 0) {
                continue;
            }
            for(WorkflowNodeOutput workflowNodeOutput : workflowNodeOutputList) {
                outputList.add(workflowNodeOutput.getId());
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
            // 物理删除节点代码
            workflowNodeCodeService.deleteByWorkflowNodeId(nodeId);
            // 物理删除节点资源
            workflowNodeResourceService.deleteByWorkflowNodeId(nodeId);
        }
        // 物理删除节点输入
        workflowNodeInputService.removeByIds(inputList);
        // 物理删除节点输出
        workflowNodeOutputService.removeByIds(outputList);
        this.removeByIds(nodeIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addWorkflowNode(WorkflowNode workflowNode) {
        // 暂存数据，数据为失效状态
        workflowNode.setStatus((byte)0);
        this.save(workflowNode);
        return workflowNode.getId();
    }

    @Override
    public void renameWorkflowNode(Long workflowNodeId, String nodeName) {
        WorkflowNode workflowNode = getWorkflowNodeById(workflowNodeId);
        if (null == workflowNode) {
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_EXIST.getMsg());
        }
        workflowNode.setNodeName(nodeName);
        this.updateById(workflowNode);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteWorkflowNode(Long id) {
        // 物理删除节点代码
        workflowNodeCodeService.deleteByWorkflowNodeId(id);
        // 物理删除输入
        workflowNodeInputService.deleteByWorkflowNodeId(id);
        // 物理删除节点变量
        workflowNodeVariableService.deleteByWorkflowNodeId(id);
        // 物理删除输出
        workflowNodeOutputService.deleteByWorkflowNodeId(id);
        // 物理删除节点资源（环境）
        workflowNodeResourceService.deleteByWorkflowNodeId(id);
        // 物理删除当前工作流节点
        this.removeById(id);
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
    public WorkflowNode getWorkflowNodeById(Long id) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getId, id);
        wrapper.eq(WorkflowNode::getStatus, 1);
        return this.getOne(wrapper);
    }

    @Override
    public void saveWorkflowNodeInput(Long workflowNodeId, List<WorkflowNodeInput> workflowNodeInputList) {
        List<WorkflowNodeInput> nodeInputList =
                workflowNodeInputService.getByWorkflowNodeId(workflowNodeId);

        // 如果已存在则全部删除，并新增
        if (nodeInputList != null && nodeInputList.size() > 0) {
            List<Long> idList = new ArrayList<>();
            for (WorkflowNodeInput nodeInput : nodeInputList) {
                idList.add(nodeInput.getId());
            }
            // 物理删除
            workflowNodeInputService.removeByIds(idList);
        }
        // 新增
        workflowNodeInputService.saveBatch(workflowNodeInputList);
    }

    @Override
    public void saveWorkflowNodeOutput(Long workflowNodeId, List<WorkflowNodeOutput> workflowNodeOutputList) {
        List<WorkflowNodeOutput> nodeOutputList =
                workflowNodeOutputService.getByWorkflowNodeId(workflowNodeId);
        // 如果已存在则全部删除，并新增
        if (nodeOutputList != null && nodeOutputList.size() > 0) {
            List<Long> idList = new ArrayList<>();
            for (WorkflowNodeOutput nodeInput : nodeOutputList) {
                idList.add(nodeInput.getId());
            }
            // 物理删除
            workflowNodeOutputService.removeByIds(idList);
        }
        // 新增
        workflowNodeOutputService.saveBatch(workflowNodeOutputList);
    }

    @Override
    public void saveWorkflowNodeCode(WorkflowNodeCode workflowNodeCode) {
        WorkflowNodeCode workflowNodeCodeOld =
                workflowNodeCodeService.getByWorkflowNodeId(workflowNodeCode.getWorkflowNodeId());
        // 如果已存在则修改
        if (Objects.nonNull(workflowNodeCodeOld)) {
            workflowNodeCodeOld.setEditType(workflowNodeCode.getEditType());
            workflowNodeCodeOld.setCalculateContractCode(workflowNodeCode.getCalculateContractCode());
            workflowNodeCodeOld.setDataSplitContractCode(workflowNodeCode.getDataSplitContractCode());
            workflowNodeCodeService.updateById(workflowNodeCodeOld);
            return;
        }
        // 不存在算法代码则新增
        workflowNodeCodeService.save(workflowNodeCode);
    }

    @Override
    public void saveWorkflowNodeResource(WorkflowNodeResource workflowNodeResource) {
        WorkflowNodeResource resourceOld =
                workflowNodeResourceService.getByWorkflowNodeId(workflowNodeResource.getWorkflowNodeId());
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
