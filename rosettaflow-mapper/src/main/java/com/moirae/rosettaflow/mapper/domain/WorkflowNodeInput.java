package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * t_workflow_node_input
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_input")
public class WorkflowNodeInput implements Serializable {
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
     * 组织的身份标识Id
     */
    private String identityId;
    /**
     * 数据表ID
     */
    private String dataTableId;
    /**
     * ID列(列索引)
     */
    private Long keyColumn;
    /**
     * 因变量(标签)
     */
    private Long dependentVariable;
    /**
     * 数据字段ID
     */
    private String dataColumnIds;
    /**
     * 任务里面定义的 (p0 -> pN 方 ...)
     */
    private String partyId;
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
