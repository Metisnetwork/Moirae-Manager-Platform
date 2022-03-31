package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 计算流程配置步骤表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_calculation_process_step")
public class CalculationProcessStep implements Serializable {


    /**
     * 计算流程ID
     */
    @TableId("calculation_process_id")
    private Long calculationProcessId;

    /**
     * 步骤. 从0开始
     */
    private Integer step;

    /**
     * 部署说明. 0-选择训练输入数据, 1-选择预测输入数据, 2-选择PSI输入数据, 3-选择计算环境(通用), 4-选择计算环境(训练&预测), 5-选择结果接收方(通用), 6-选择结果接收方(训练&预测)
     */
    @TableField("`type`")
    private Integer type;


    private static final long serialVersionUID = 1L;

}
