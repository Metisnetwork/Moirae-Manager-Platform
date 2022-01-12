package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_output
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_output")
public class WorkflowNodeOutput implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点输出表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
    /**
     * 协同方组织的身份标识Id
     */
    private String identityId;
    /**
     * 存储形式: 1-明文，2:密文
     */
    private Byte storePattern;
    /**
     * 存储路径
     */
    private String storePath;
    /**
     * 输出内容
     */
    private String outputContent;
    /**
     * 任务里面定义的 (q0 -> qN 方 ...)
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


    /**
     * 协同方组织名称
     */
    @TableField(exist = false)
    private String identityName;

}
