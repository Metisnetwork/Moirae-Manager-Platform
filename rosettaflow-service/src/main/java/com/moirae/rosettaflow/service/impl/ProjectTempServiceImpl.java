package com.moirae.rosettaflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moirae.rosettaflow.common.constants.SysConstant;
import com.moirae.rosettaflow.common.enums.ErrorMsg;
import com.moirae.rosettaflow.common.enums.RespCodeEnum;
import com.moirae.rosettaflow.common.enums.StatusEnum;
import com.moirae.rosettaflow.common.exception.BusinessException;
import com.moirae.rosettaflow.mapper.ProjectTempMapper;
import com.moirae.rosettaflow.mapper.WorkflowNodeTempMapper;
import com.moirae.rosettaflow.mapper.WorkflowTempMapper;
import com.moirae.rosettaflow.mapper.domain.*;
import com.moirae.rosettaflow.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    private IWorkflowTempService workflowTempService;
    @Resource
    private IWorkflowNodeTempService workflowNodeTempService;

    @Override
    public List<ProjectTemp> projectTempList(String language) {
        LambdaQueryWrapper<ProjectTemp> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProjectTemp::getStatus, StatusEnum.VALID.getValue());
        List<ProjectTemp> returnList = this.list(wrapper);
        //添加空白模板
        ProjectTemp emptyTemp = new ProjectTemp();
        emptyTemp.setId(0L);
        // 处理国际化
        if (SysConstant.EN_US.equals(language)) {
            for (ProjectTemp ProjectTemp : returnList) {
                ProjectTemp.setProjectName(ProjectTemp.getProjectNameEn());
                ProjectTemp.setProjectDesc(ProjectTemp.getProjectDescEn());
            }
            return returnList;
        }
        return returnList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addProjectTemplate(Long workflowId) {

        Workflow workflow = workflowService.getById(workflowId);
        if (null == workflow) {
            log.error("addProjectTemplate fail,reason:{}", ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.WORKFLOW_NOT_EXIST.getMsg());
        }

        Project project = projectService.getById(workflow.getProjectId());
        if (null == project) {
            log.error("addProjectTemplate fail,reason:{}", ErrorMsg.PROJECT_NOT_EXIST.getMsg());
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.PROJECT_NOT_EXIST.getMsg());
        }

        // 添加项目模板
        long projectTemplateId = addProjectTemplate(project);

        // 添加工作流模板
        long workflowTemplateId = workflowTempService.addWorkflowTemplate(projectTemplateId, workflow);

        // 添加工作流对应的节点模板
        List<WorkflowNode> workflowNodeList = workflowNodeService.getWorkflowNodeList(workflow.getId());
        workflowNodeTempService.addWorkflowNodeList(workflowTemplateId, workflowNodeList);
    }

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }

    /**
     * 查询工作流模板列表
     */
    private List<WorkflowTemp> queryWorkflowTempList(Long projTempId) {
        LambdaQueryWrapper<WorkflowTemp> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkflowTemp::getProjectTempId, projTempId);
        queryWrapper.eq(WorkflowTemp::getStatus, StatusEnum.VALID.getValue());
        return workflowTempMapper.selectList(queryWrapper);
    }

    /**
     * 查询工作流节点模板列表
     */
    private List<WorkflowNodeTemp> queryWorkflowNodeTempList(Long workflowTempId) {
        LambdaQueryWrapper<WorkflowNodeTemp> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(WorkflowNodeTemp::getWorkflowTempId, workflowTempId);
        queryWrapper.eq(WorkflowNodeTemp::getStatus, StatusEnum.VALID.getValue());
        return workflowNodeTempMapper.selectList(queryWrapper);
    }

    /**
     * 保存项目模板
     *
     * @param project 项目模板
     * @return projectTemplateId
     */
    private Long addProjectTemplate(Project project) {
        ProjectTemp projectTemp = new ProjectTemp();
        projectTemp.setProjectName(project.getProjectName());
        projectTemp.setProjectDesc(project.getProjectDesc());
        try {
            this.save(projectTemp);
        } catch (Exception e) {
            log.error("addProjectTemplate fail,reason:{}", e.getMessage(), e);
            if (e instanceof DuplicateKeyException) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.PROJECT_TEMPLATE_NAME_EXISTED.getMsg());
            }
            throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.ADD_PROJECT_TEMPLATE_ERROR.getMsg());
        }
        return projectTemp.getId();
    }

    /**
     * 保存工作流
     */
    private Long saveWorkflow(Long projectId, WorkflowTemp workflowTemp) {
        Workflow workflow = new Workflow();
        workflow.setProjectId(projectId);
        workflow.setWorkflowName(workflowTemp.getWorkflowName());
        workflow.setWorkflowDesc(workflowTemp.getWorkflowDesc());
        workflowService.addWorkflow(workflow);
        return workflow.getId();
    }

    /**
     * 保存算法
     */
    private Long saveAlgorithm(WorkflowNodeTemp nodeTemp) {
        return algorithmService.copySaveAlgorithm(BeanUtil.toBean(nodeTemp, WorkflowNode.class));
    }
}
