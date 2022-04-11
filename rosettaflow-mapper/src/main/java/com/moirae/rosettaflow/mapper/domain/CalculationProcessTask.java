package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.moirae.rosettaflow.mapper.enums.CalculationProcessTaskAlgorithmSelectEnum;
import jnr.ffi.annotations.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 计算流程任务步骤表
 * </p>
 *
 * @author chendai
 * @since 2022-04-11
 */
@Data
@TableName("mo_calculation_process_task")
public class CalculationProcessTask implements Serializable {


    /**
     * 计算流程ID
     */
    @TableId("calculation_process_id")
    private Long calculationProcessId;

    /**
     * 任务步骤
     */
    private Integer step;

    /**
     * 算法选择方式. 0-用户输入母算法; 1-用户输入子训练算法, 2-用户输入子预测算法, 3-内置算法
     */
    private CalculationProcessTaskAlgorithmSelectEnum algorithmSelect;

    /**
     * 内置算法id
     */
    private Long builtInAlgorithmId;


    private static final long serialVersionUID = 1L;

}
