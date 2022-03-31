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
@TableName(value = "t_workflow")
public class ZOldWorkflow implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 项目工作流ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 用户id(创建方id)
     */
    private Long userId;

    /**
     * 工作流名称
     */
    private String workflowName;

    /**
     * 工作流描述
     */
    private String workflowDesc;

    /**
     * 发起任务的用户钱包地址
     */
    private String address;
    /**
     * 发起任务的账户的签名
     */
    private String sign;

    /**
     * 编辑版本标识，每次编辑后递增，从1开始
     */
    private Integer editVersion;

    /**
     * 状态: 0-无效，1- 有效
     */
    @TableField(value = "`status`")
    private Byte status;

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
     * 运行状态: 0-未开始 1-运行中,2-运行成功,3-运行失败
     */
    @TableField(exist = false)
    private Byte runStatus;
    /**
     * 取消状态: 0-未开始,1-取消中,2-取消成功,3-取消失败
     */
    @TableField(exist = false)
    private Byte cancelStatus;

    @TableField(exist = false)
    private List<ZOldWorkflowNode> getNodeStatusVoList = new ArrayList<>();

    @TableField(exist = false)
    private List<ZOldWorkflowNode> workflowNodeVoList = new ArrayList<>();
    /**
     * 工作流ID
     */
    @TableField(exist = false)
    private Long workflowId;
    /**
     * 工作流节点列表
     */
    @TableField(exist = false)
    List<ZOldWorkflowNode> workflowNodeReqList;
    /**
     * 是否需要保存（0：不需要，1：需要）
     */
    @TableField(exist = false)
    private String saveFlag;

    @TableField(exist = false)
    Map<Integer, ZOldWorkflowNode> workflowNodeMap;

    @TableField(exist = false)
    private Long workflowRunStatusId;
}
