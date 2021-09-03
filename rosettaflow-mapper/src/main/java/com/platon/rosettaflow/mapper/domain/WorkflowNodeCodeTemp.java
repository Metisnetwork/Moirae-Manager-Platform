package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_workflow_node_code_temp
 * @author houz
 */
@Data
@TableName(value = "t_workflow_node_code_temp")
public class WorkflowNodeCodeTemp implements Serializable {
    /**
     * 工作流节点算法代码模板表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工作流节点id
     */
    private Long workflowNodeTempId;

    /**
     * 编辑类型:1-sql, 2-noteBook
     */
    private Byte editType;

    /**
     * 计算合约
     */
    private String calculateContractCode;

    /**
     * 数据分片合约
     */
    private String dataSplitContractCode;

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