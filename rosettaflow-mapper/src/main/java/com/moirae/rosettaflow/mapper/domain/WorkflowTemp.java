package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_temp
 * @author houz
 */
@Data
@TableName(value = "t_workflow_temp")
public class WorkflowTemp implements Serializable {
    /**
     * 工作流模板ID(自增长)
     */
    private Long id;

    /**
     * 项目模板表id
     */
    private Long projectTempId;

    /**
     * 中文工作流名称
     */
    private String workflowName;

    /**
     * 英文工作流名称
     */
    private String workflowNameEn;

    /**
     * 中文工作流描述
     */
    private String workflowDesc;

    /**
     * 英文工作流描述
     */
    private String workflowDescEn;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
