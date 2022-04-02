package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
