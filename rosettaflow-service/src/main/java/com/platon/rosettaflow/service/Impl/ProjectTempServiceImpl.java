package com.platon.rosettaflow.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platon.rosettaflow.mapper.*;
import com.platon.rosettaflow.mapper.domain.*;
import com.platon.rosettaflow.service.IProjectService;
import com.platon.rosettaflow.service.IProjectTempService;
import com.platon.rosettaflow.service.IWorkflowNodeService;
import com.platon.rosettaflow.service.IWorkflowService;
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
    AlgorithmMapper algorithmMapper;

    @Resource
    AlgorithmCodeMapper algorithmCodeMapper;

    @Resource
    AlgorithmVariableMapper algorithmVariableMapper;


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
        Project project = saveProject(projectTemp);

        // 查询工作流模板
        List<WorkflowTemp> workflowTempList = queryWorkflowTempList(projectTemp.getId());
        if (workflowTempList == null || workflowTempList.size() == 0) {
            return;
        }
        for (WorkflowTemp workflowTemp : workflowTempList) {
            // 保存工作流
            Workflow workflow = saveWorkflow(project.getId(), workflowTemp);

            // 查询工作流节点模板
            List<WorkflowNodeTemp> workflowNodeTempList = queryWorkflowNodeTempList(workflowTemp.getId());
            if (workflowNodeTempList == null || workflowNodeTempList.size() == 0) {
                continue;
            }
            // 保存工作流节点
            saveWorkflowNode(workflow.getId(), workflowNodeTempList);
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

    /** 查询工作流模板所使用算法 */
    private Algorithm queryAlgorithmObj(Long algorithmId){
        return algorithmMapper.selectById(algorithmId);
    }

//    /** 查询工作流节点变量模板列表 */
//    private List<WorkflowNodeVariableTemp> queryWorkflowNodeVariableTempList(Long workflowNodeTempId){
//        LambdaQueryWrapper<WorkflowNodeVariableTemp> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper.eq(WorkflowNodeVariableTemp::getWorkflowNodeTempId, workflowNodeTempId);
//        return workflowNodeVariableTempMapper.selectList(queryWrapper);
//    }

    /** 保存项目 */
    private Project saveProject(ProjectTemp projectTemp){
        Project project = new Project();
        project.setProjectName(projectTemp.getProjectName());
        project.setProjectDesc(projectTemp.getProjectDesc());
        projectService.addProject(project);
        return project;
    }

    /** 保存工作流 */
    private Workflow saveWorkflow(Long projectId, WorkflowTemp workflowTemp){
        return workflowService.addWorkflow(projectId,
                workflowTemp.getWorkflowName(),workflowTemp.getWorkflowDesc());
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
        // 查询工作流模板所使用算法
    }
}
