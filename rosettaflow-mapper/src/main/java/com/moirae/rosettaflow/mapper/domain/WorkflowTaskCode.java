package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 工作流任务算法代码表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_task_code")
public class WorkflowTaskCode implements Serializable {

    /**
     * 工作流任务配置id
     */
    @TableId("workflow_task_id")
    private Long workflowTaskId;

    /**
     * 计算合约变量模板json格式结构
     */
    private String calculateContractStruct;

    /**
     * 计算合约
     */
    @TableField("calculate_contract_code")
    private String calculateContractCode;

    /**
     * 数据分片合约
     */
    @TableField("data_split_contract_code")
    private String dataSplitContractCode;

    /**
     * 编辑类型:1-sql, 2-noteBook
     */
    private Integer editType;

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
}
