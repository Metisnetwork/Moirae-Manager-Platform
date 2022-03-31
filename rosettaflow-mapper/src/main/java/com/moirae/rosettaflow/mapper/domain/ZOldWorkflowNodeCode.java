package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_code")
public class ZOldWorkflowNodeCode implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点代码表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
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
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;
}
