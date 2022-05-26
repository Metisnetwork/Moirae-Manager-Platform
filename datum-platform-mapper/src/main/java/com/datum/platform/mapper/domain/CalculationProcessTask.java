package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.CalculationProcessTaskAlgorithmSelectEnum;
import lombok.Data;

import java.io.Serializable;

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
     * 算法选择方式. 0-用户输入母算法; 1-用户输入子训练算法, 2-用户输入子预测算法, 3-内置PSI算法
     */
    private CalculationProcessTaskAlgorithmSelectEnum algorithmSelect;

    /**
     * 工作流节点需要的模型产生的步骤
     */
    private Integer inputModelStep;

    /**
     * 工作流节点需要的psi产生步骤
     */
    private Integer inputPsiStep;

    private static final long serialVersionUID = 1L;

}
