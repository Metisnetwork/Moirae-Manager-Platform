package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.CalculationProcessTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工作流向导模式步骤表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_workflow_setting_wizard")
public class WorkflowSettingWizard implements Serializable {


    /**
     * 工作流步骤表ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工作流id
     */
    @TableField("workflow_id")
    private Long workflowId;

    /**
     * 工作流版本号
     */
    @TableField("workflow_version")
    private Long workflowVersion;

    /**
     * 当前步骤,从1开始
     */
    private Integer step;

    /**
     * 部署说明. 0-选择训练输入数据, 1-选择预测输入数据, 2-选择PSI输入数据, 3-选择计算环境(通用), 4-选择计算环境(训练&预测), 5-选择结果接收方(通用), 6-选择结果接收方(训练&预测)
     */
    @TableField("calculation_process_step_type")
    private CalculationProcessTypeEnum calculationProcessStepType;

    /**
     * 任务1对应的步骤
     */
    @TableField("`task_1_step`")
    private Integer task1Step;

    /**
     * 任务2对应的步骤
     */
    @TableField("`task_2_step`")
    private Integer task2Step;

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
