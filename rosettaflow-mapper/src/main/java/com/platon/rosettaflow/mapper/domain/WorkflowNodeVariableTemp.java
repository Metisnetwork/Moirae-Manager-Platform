package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_workflow_node_variable_temp
 * @author houz
 */
@Data
@TableName(value = "t_workflow_node_variable_temp")
public class WorkflowNodeVariableTemp implements Serializable {
    /**
     * 工作流节点变量表ID(自增长)
     */
    private Long id;

    /**
     * 工作流节点模板表id
     */
    private Long workflowNodeTempId;

    /**
     * 变量类型: 1-自变量, 2-因变量
     */
    private Byte varNodeType;

    /**
     * 变量key
     */
    private String varNodeKey;

    /**
     * 变量值
     */
    private String varNodeValue;

    /**
     * 变量描述
     */
    private String varNodeDesc;

    /**
     * 状态: 0-无效，1- 有效
     */
    private Byte status;

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