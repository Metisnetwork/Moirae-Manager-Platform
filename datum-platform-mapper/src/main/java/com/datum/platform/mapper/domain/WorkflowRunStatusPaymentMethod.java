package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工作流运行状态对应支付方式
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_run_status_payment_method")
public class WorkflowRunStatusPaymentMethod implements Serializable {


    /**
     * ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工作流运行状态ID
     */
    private Long workflowRunId;

    /**
     * 对应元数据ID,hash
     */
    private String metaDataId;

    /**
     * 合约类型: 0-erc20,1-erc721
     */
    private Integer type;

    /**
     * 合约地址
     */
    private String tokenAddress;

    /**
     * 721下合约token id
     */
    private String tokenId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private List<WorkflowRunStatusTask> workflowRunTaskStatusList;
    @TableField(exist = false)
    private Workflow workflow;
}
