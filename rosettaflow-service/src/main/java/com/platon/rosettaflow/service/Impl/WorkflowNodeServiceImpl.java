package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.enums.StatusEnum;
import com.platon.rosettaflow.common.enums.WorkflowRunStatusEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.dto.AlgorithmDto;
import com.platon.rosettaflow.dto.WorkflowNodeDto;
import com.platon.rosettaflow.mapper.WorkflowNodeMapper;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.service.*;
import io.swagger.util.Json;
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
    public List<WorkflowNodeDto> queryNodeDetailsList(Long id) {
        // 获取工作流节点列表
        List<WorkflowNode> workflowNodeList = getWorkflowNodeList(id);
        if (workflowNodeList == null || workflowNodeList.size() == 0) {
            return new ArrayList<>();
        }
        List<WorkflowNodeDto> workflowNodeDtoList = new ArrayList<>();
        for (WorkflowNode workflowNode : workflowNodeList) {
            // 工作流节点dto
            WorkflowNodeDto workflowNodeDto = BeanUtil.toBean(workflowNode, WorkflowNodeDto.class);
            // 算法对象
            AlgorithmDto algorithmDto = algorithmService.queryAlgorithmDetails(workflowNode.getAlgorithmId());
            if (Objects.nonNull(algorithmDto)) {
                // 工作流节点算法代码, 如果可查询出，表示已修改，否则没有变动
                WorkflowNodeCode workflowNodeCode = workflowNodeCodeService.getByWorkflowNodeId(workflowNode.getId());
                if (Objects.nonNull(workflowNodeCode)) {
                    algorithmDto.setEditType(workflowNodeCode.getEditType());
                    algorithmDto.setCalculateContractCode(workflowNodeCode.getCalculateContractCode());
                }
                // 工作流节点算法资源环境, 如果可查询出，表示已修改，否则没有变动
                WorkflowNodeResource nodeResource = workflowNodeResourceService.getByWorkflowNodeId(workflowNode.getId());
                if (Objects.nonNull(nodeResource)) {
                    algorithmDto.setCostCpu(nodeResource.getCostCpu());
                    algorithmDto.setCostGpu(nodeResource.getCostGpu());
                    algorithmDto.setCostMem(nodeResource.getCostMem());
                    algorithmDto.setCostBandwidth(nodeResource.getCostBandwidth());
                    algorithmDto.setRunTime(nodeResource.getRunTime());
                }
            }
            workflowNodeDto.setAlgorithmDto(algorithmDto);
            //工作流节点输入列表
            List<WorkflowNodeInput> workflowNodeInputList = workflowNodeInputService.getByWorkflowNodeId(workflowNode.getId());
            workflowNodeDto.setWorkflowNodeInputList(workflowNodeInputList);
            //工作流节点输出列表
            List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.getByWorkflowNodeId(workflowNode.getId());
            workflowNodeDto.setWorkflowNodeOutputList(workflowNodeOutputList);
            workflowNodeDtoList.add(workflowNodeDto);
        }
        return workflowNodeDtoList;
    }

    @Override
    public void saveWorkflowNode(Long workflowId, List<WorkflowNode> workflowNodeList) {
        Workflow workflow = workflowService.queryWorkflowDetail(workflowId);
        if (Objects.isNull(workflow)) {
            log.info("saveWorkflowNode--工作流不存在:{}", JSON.toJSONString(workflowId));
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }
        if (WorkflowRunStatusEnum.RUNNING.getValue() == workflow.getRunStatus()) {
            log.info("saveWorkflowNode--工作流运行中:{}", JSON.toJSONString(workflow));
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_RUNNING_EXIST.getMsg());
        }
        // 第一期项目只允许保存一个节点
        if (workflowNodeList.size() > 1) {
            log.info("saveWorkflowNode--工作流节点超出范围:{}", JSON.toJSONString(workflowNodeList));
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_COUNT_CHECK.getMsg());
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
                node.setNodeName(nodeReq.getNodeName());
                node.setNodeStep(nodeReq.getNodeStep());
                node.setNextNodeStep(nodeReq.getNodeStep() + 1);
                if (++count == workflowNodeList.size()) {
                    // 将最后一个节点步骤的下一节点步骤字段值置空
                    node.setNextNodeStep(null);
                }
                node.setStatus((byte) 1);
                nodeBatchList.add(node);
                // 去掉idList中需要保存的节点id，保留需要物理删除的节点
                idList.remove(nodeReq.getId());
            } else {
                log.error("workflow node id:{},nodeName:{},nodeStep:{},have not save,please save first", nodeReq.getId(), nodeReq.getNodeName(), nodeReq.getNodeStep());
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_CACHE.getMsg());
            }
        }
        this.updateBatchById(nodeBatchList);
        // 将不需要保存的节点及所属数据物理删除
        removeWorkflowNode(idList);
        // 保存当前工作流节点数
        workflow.setNodeNumber(count);
        workflowService.updateById(workflow);
    }

    /**
     * 物理删除不需要保存的工作流节点
     */
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
        if (workflowNodeList == null || workflowNodeList.size() == 0) {
            return;
        }
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
            if (workflowNodeInputList != null && workflowNodeInputList.size() > 0) {
                for (WorkflowNodeInput workflowNodeInput : workflowNodeInputList) {
                    inputList.add(workflowNodeInput.getId());
                }
            }
            List<WorkflowNodeOutput> workflowNodeOutputList = workflowNodeOutputService.getByWorkflowNodeId(nodeId);
            if (workflowNodeOutputList != null && workflowNodeOutputList.size() > 0) {
                for (WorkflowNodeOutput workflowNodeOutput : workflowNodeOutputList) {
                    outputList.add(workflowNodeOutput.getId());
                }
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
    public Map<String, Object> addWorkflowNode(WorkflowNode workflowNode) {
        Map<String, Object> respMap = new HashMap<>(4);
        // 暂存数据，数据为失效状态
        workflowNode.setStatus(StatusEnum.UN_VALID.getValue());
        this.save(workflowNode);
        // 查询算法详情并返回
        AlgorithmDto algorithmDto = algorithmService.queryAlgorithmDetails(workflowNode.getAlgorithmId());
        respMap.put("workflowNodeId", workflowNode.getId());
        // 节点运行状态默认为未开始0
        respMap.put("runStatus", WorkflowRunStatusEnum.UN_RUN.getValue());
        respMap.put("algorithmDto", algorithmDto == null ? new AlgorithmDto() : algorithmDto);
        return respMap;
    }

    @Override
    public void renameWorkflowNode(Long workflowNodeId, String nodeName) {
        WorkflowNode workflowNode = getWorkflowNodeById(workflowNodeId);
        if (null == workflowNode) {
            log.info("renameWorkflowNode--工作流节点为空, workflowNodeId:{}, nodeName:{}", workflowNodeId, nodeName);
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
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        wrapper.eq(WorkflowNode::getNodeStep, nodeStep);
        wrapper.eq(WorkflowNode::getStatus, StatusEnum.VALID.getValue());
        WorkflowNode workflowNode = this.getOne(wrapper);
        if (workflowNode == null) {
            log.error("workflow node not found by workflowId:{},nodeStep:{}", workflowId, nodeStep);
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NODE_NOT_EXIST.getMsg());
        }
        return workflowNode;
    }

    @Override
    public List<WorkflowNode> getAllWorkflowNodeList(Long workflowId) {
        // 查询所有节点（包含失效数据）
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
        wrapper.eq(WorkflowNode::getStatus, StatusEnum.VALID.getValue());
        // 所有节点正序排序
        wrapper.orderByAsc(WorkflowNode::getNodeStep);
        return this.list(wrapper);
    }

    @Override
    public WorkflowNode getWorkflowNodeById(Long id) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getId, id);
        wrapper.eq(WorkflowNode::getStatus, StatusEnum.VALID.getValue());
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
//            workflowNodeCodeService.addByAlgorithmIdAndWorkflowNodeId(workflowNodeTemp.getAlgorithmId(), workflowNode.getId());
            //查询节点代码对应的算法列表
//            List<AlgorithmVariable> algorithmVariableList = algorithmVariableService.getByAlgorithmId(workflowNodeTemp.getAlgorithmId());
//            if (algorithmVariableList != null && algorithmVariableList.size() > 0) {
//                //保存工作流输入变量
//                workflowNodeVariableService.addByAlgorithmVariableList(workflowNode.getId(), algorithmVariableList);
//            }
        }
    }

    @Override
    public WorkflowNode getRunningNodeByWorkflowId(Long workflowId) {
        LambdaQueryWrapper<WorkflowNode> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WorkflowNode::getWorkflowId, workflowId);
        wrapper.eq(WorkflowNode::getRunStatus, WorkflowRunStatusEnum.RUNNING.getValue());
        return this.getOne(wrapper);
    }

    @Override
    public void updateRunStatusByWorkflowId(Long workflowId, Byte oldRunStatus, Byte newRunStatus) {
        LambdaUpdateWrapper<WorkflowNode> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(WorkflowNode::getRunStatus,newRunStatus);
        updateWrapper.eq(WorkflowNode::getRunStatus,oldRunStatus);
        updateWrapper.eq(WorkflowNode::getWorkflowId,workflowId);
        this.update(updateWrapper);
    }
}
