package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_variable
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_variable")
public class ZOldWorkflowNodeVariable implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;
}
