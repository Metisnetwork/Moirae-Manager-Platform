package com.platon.rosettaflow.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.*;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.service.*;
import com.platon.rosettaflow.service.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author admin
 * @date 2021/8/16
 */
@Slf4j
@Service
public class ProjectTempServiceImpl extends ServiceImpl<ProjectTempMapper, ProjectTemp> implements IProjectTempService {

    @Resource
    IProjectService projectService;

    @Resource
    ProjectTempMapper projectTempMapper;

    @Resource
    IWorkflowService workflowService;

    @Resource
    WorkflowTempMapper workflowTempMapper;

    @Resource
    IWorkflowNodeService workflowNodeService;

    @Resource
    WorkflowNodeTempMapper workflowNodeTempMapper;

    @Resource
    IAlgorithmService algorithmService;

    @Resource
    IAlgorithmCodeService algorithmCodeService;


    @Resource
    IAlgorithmVariableService algorithmVariableService;


    @Override
    public List<ProjectTemp> queryProjectTempList() {
        return projectTempMapper.selectList(null);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addProjTemp(Long projTempId) {
        // 查询项目模板
        ProjectTemp projectTemp = projectTempMapper.selectById(projTempId);
        if (Objects.isNull(projectTemp)) {
            return;
        }
        // 保存项目
        Long projectId = saveProject(projectTemp);

        // 查询工作流模板
        List<WorkflowTemp> workflowTempList = queryWorkflowTempList(projectTemp.getId());
        if (workflowTempList == null || workflowTempList.size() == 0) {
            return;
        }
        for (WorkflowTemp workflowTemp : workflowTempList) {
            // 保存工作流
            Long workflowId = saveWorkflow(projectId, workflowTemp);
            // 查询工作流节点模板
            List<WorkflowNodeTemp> nodeTempList = queryWorkflowNodeTempList(workflowTemp.getId());
            if (nodeTempList == null || nodeTempList.size() == 0) {
                continue;
            }
            // 保存工作流节点
            saveWorkflowNode(workflowId, nodeTempList);
            // 保存算法、算法代码、算法变量
            for (WorkflowNodeTemp nodeTemp : nodeTempList) {
                // 保存算法
                Long algorithmId = saveAlgorithm(nodeTemp);
                // 保存算法代码
                saveAlgorithmCode(algorithmId);
                // 保存算法变量
                saveAlgorithmVariable(algorithmId);
            }
        }
    }

    /** 查询工作流模板列表 */
    private List<WorkflowTemp> queryWorkflowTempList(Long projTempId){
        LambdaQueryWrapper<WorkflowTemp> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkflowTemp::getProjectTempId, projTempId);
        return workflowTempMapper.selectList(queryWrapper);
    }

    /** 查询工作流节点模板列表 */
    private List<WorkflowNodeTemp> queryWorkflowNodeTempList(Long workflowTempId){
        LambdaQueryWrapper<WorkflowNodeTemp> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkflowNodeTemp::getWorkflowTempId, workflowTempId);
        return workflowNodeTempMapper.selectList(queryWrapper);
    }

    /** 保存项目 */
    private Long saveProject(ProjectTemp projectTemp){
        Project project = new Project();
        project.setProjectName(projectTemp.getProjectName());
        project.setProjectDesc(projectTemp.getProjectDesc());
        projectService.addProject(project);
        return project.getId();
    }

    /** 保存工作流 */
    private Long saveWorkflow(Long projectId, WorkflowTemp workflowTemp){
        Workflow workflow = new Workflow();
        workflow.setProjectId(projectId);
        workflow.setWorkflowName(workflowTemp.getWorkflowName());
        workflow.setWorkflowDesc(workflowTemp.getWorkflowDesc());
        workflowService.addWorkflow(workflow);
        return workflow.getId();
    }

    /** 保存工作流节点 */
    private void saveWorkflowNode(Long workflowId, List<WorkflowNodeTemp> nodeTempList){
        List<WorkflowNode> nodeList = new ArrayList<>();
        for (WorkflowNodeTemp nodeTemp : nodeTempList) {
            WorkflowNode workflowNode = new WorkflowNode();
            workflowNode.setWorkflowId(workflowId);
            workflowNode.setAlgorithmId(nodeTemp.getAlgorithmId());
            workflowNode.setNodeName(nodeTemp.getNodeName());
            workflowNode.setNodeStep(nodeTemp.getNodeStep());
            workflowNode.setNextNodeStep(nodeTemp.getNextNodeStep());
            nodeList.add(workflowNode);
        }
        workflowNodeService.saveBatch(nodeList);
    }

    /** 保存算法 */
    private Long saveAlgorithm(WorkflowNodeTemp nodeTemp){
        Algorithm algorithmTemp = algorithmService.getById(nodeTemp.getId());
        if (Objects.isNull(algorithmTemp)) {
            return null;
        }
        Algorithm newAlgorithm = BeanUtil.toBean(algorithmTemp, Algorithm.class);
        newAlgorithm.setId(null);
        newAlgorithm.setAuthor(UserContext.get() == null ? "" : UserContext.get().getUserName());
        newAlgorithm.setCreateTime(null);
        newAlgorithm.setUpdateTime(null);
        algorithmService.save(newAlgorithm);
        return newAlgorithm.getId();
    }

    /** 保存算法代码 */
    private void saveAlgorithmCode(Long algorithmId){
        AlgorithmCode algorithmCodeTemp = algorithmCodeService.getByAlgorithmId(algorithmId);
        if (Objects.isNull(algorithmCodeTemp)) {
            return;
        }
        AlgorithmCode newAlgorithmCode = new AlgorithmCode();
        newAlgorithmCode.setAlgorithmId(algorithmId);
        newAlgorithmCode.setEditType(algorithmCodeTemp.getEditType());
        newAlgorithmCode.setCalculateContractCode(algorithmCodeTemp.getCalculateContractCode());
        newAlgorithmCode.setDataSplitContractCode(algorithmCodeTemp.getDataSplitContractCode());
        algorithmCodeService.save(newAlgorithmCode);
    }

    /** 保存算法变量 */
    private void saveAlgorithmVariable(Long algorithmId){
        List<AlgorithmVariable> variableTempList = algorithmVariableService.getByAlgorithmId(algorithmId);
        if (variableTempList == null || variableTempList.size() == 0) {
            return;
        }
        List<AlgorithmVariable> newAlgorithmVariableList = new ArrayList<>();
        for (AlgorithmVariable algorithmVariable : variableTempList) {
            AlgorithmVariable newAlgorithmVariable = new AlgorithmVariable();
            newAlgorithmVariable.setAlgorithmId(algorithmId);
            newAlgorithmVariable.setVarType(algorithmVariable.getVarType());
            newAlgorithmVariable.setVarKey(algorithmVariable.getVarKey());
            newAlgorithmVariable.setVarValue(algorithmVariable.getVarValue());
            newAlgorithmVariable.setVarDesc(algorithmVariable.getVarDesc());
            newAlgorithmVariableList.add(newAlgorithmVariable);
        }
        algorithmVariableService.saveBatch(newAlgorithmVariableList);
    }
}
