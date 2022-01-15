package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * t_workflow
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node")
public class WorkflowNode implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工作流节点ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流id
     */
    private Long workflowId;
    /**
     * 工作流版本号
     */
    private Integer workflowEditVersion;
    /**
     * 工作流节点名称
     */
    private String nodeName;
    /**
     * 算法id
     */
    private Long algorithmId;
    /**
     * 节点在工作流中序号,从1开始
     */
    private Integer nodeStep;
    /**
     * 工作流节点需要的模型id,对应t_task_result表id
     */
    private Long modelId;
    /**
     * 任务发启放组织id
     */
    private String senderIdentityId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 任务ID,底层处理完成后返回
     */
    @TableField(exist = false)
    private String taskId;
    /**
     * 任务处理结果描述
     */
    @TableField(exist = false)
    private String runMsg;
    /**
     * 工作流节点配置id
     */
    @TableField(exist = false)
    private Long workflowNodeId;
    /**
     * 工作流节点运行状态:0-未开始,1-运行中,2-运行成功,3-运行失败
     */
    @TableField(exist = false)
    private Byte runStatus;
    /**
     * 任务发启放组织id
     */
    @TableField(exist = false)
    private String workflowNodeSenderIdentityId;
    /**
     * 算法对象
     */
    @TableField(exist = false)
    private Algorithm nodeAlgorithmVo;
    /**
     * 工作流节点输入列表
     */
    @TableField(exist = false)
    private List<WorkflowNodeInput> workflowNodeInputVoList;
    /**
     * 工作流节点输出列表
     */
    @TableField(exist = false)
    private List<WorkflowNodeOutput> workflowNodeOutputVoList;
    /**
     * 是否需要输入模型: 0-否，1:是
     */
    @TableField(exist = false)
    private Integer inputModel;
    /**
     * 输入请求列表
     */
    @TableField(exist = false)
    private List<WorkflowNodeInput> workflowNodeInputReqList = new ArrayList<>();
    /**
     * 输出请求列表
     */
    @TableField(exist = false)
    private List<WorkflowNodeOutput> workflowNodeOutputReqList = new ArrayList<>();
    /**
     * 工作流节点代码请求对象
     */
    @TableField(exist = false)
    private WorkflowNodeCode workflowNodeCodeReq;
    /**
     * 工作流节点资源请求对象
     */
    @TableField(exist = false)
    private WorkflowNodeResource workflowNodeResourceReq;
    /**
     * 工作流节点输入变量请求对象
     */
    @TableField(exist = false)
    private List<WorkflowNodeVariable> workflowNodeVariableReqList  = new ArrayList<>();
    /**
     * 当前输入模型
     */
    @TableField(exist = false)
    private Model model;

    @TableField(exist = false)
    private Map<String, Organization> organizationMap;
}
