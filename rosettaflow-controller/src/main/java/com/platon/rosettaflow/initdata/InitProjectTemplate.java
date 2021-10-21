package com.platon.rosettaflow.initdata;

import com.platon.rosettaflow.common.constants.SysConfig;
import com.platon.rosettaflow.common.enums.WorkflowRunStatusEnum;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeTemp;
import com.platon.rosettaflow.mapper.domain.WorkflowTemp;
import com.platon.rosettaflow.service.IProjectTempService;
import com.platon.rosettaflow.service.IWorkflowNodeTempService;
import com.platon.rosettaflow.service.IWorkflowTempService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/9/7
 * @description 初始化项目模版
 */
@Slf4j
@Component
public class InitProjectTemplate {

    @Resource
    private SysConfig sysConfig;

    @Resource
    private IProjectTempService projectTempService;

    @Resource
    private IWorkflowTempService workflowTempService;

    @Resource
    private IWorkflowNodeTempService workflowNodeTempService;

//    @PostConstruct
    @Lock(keys = "InitProjectTemplate")
    public void init() {
        //清空原项目模板
        projectTempService.truncate();
        //清空工作流模板
        workflowTempService.truncate();
        //清空工作流节点模板
        workflowNodeTempService.truncate();

        //添加项目模板
        ProjectTemp projectTemp = new ProjectTemp();
        projectTemp.setProjectName("黑名单查询");
        projectTemp.setProjectDesc("黑名单查询模板");
        projectTempService.save(projectTemp);

        //添加项目对应工作流模板
        WorkflowTemp workflowTemp = new WorkflowTemp();
        workflowTemp.setProjectTempId(projectTemp.getId());
        workflowTemp.setWorkflowName("黑名单查询对应工作流");
        workflowTemp.setWorkflowDesc("黑名单查询对应工作流");
        workflowTemp.setNodeNumber(1);
        workflowTemp.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
        workflowTempService.save(workflowTemp);

        //添加工作流对应的节点模板
        WorkflowNodeTemp workflowNodeTemp = new WorkflowNodeTemp();
        workflowNodeTemp.setWorkflowTempId(workflowTemp.getId());
        // TODO 待确认
        workflowNodeTemp.setAlgorithmId(1L);
        workflowNodeTemp.setNodeName("黑名单工作流");
        workflowNodeTemp.setNodeStep(1);
        workflowNodeTemp.setRunStatus(WorkflowRunStatusEnum.UN_RUN.getValue());
        workflowNodeTempService.save(workflowNodeTemp);
    }
}
