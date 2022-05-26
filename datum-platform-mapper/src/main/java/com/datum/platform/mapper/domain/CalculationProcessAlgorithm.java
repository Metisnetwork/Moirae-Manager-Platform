package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 计算流程配置和算法关系表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_calculation_process_algorithm")
public class CalculationProcessAlgorithm implements Serializable {

    /**
     * 计算流程ID
     */
    private Long calculationProcessId;

    /**
     * 算法ID
     */
    private Long algorithmId;

    private static final long serialVersionUID = 1L;

}
