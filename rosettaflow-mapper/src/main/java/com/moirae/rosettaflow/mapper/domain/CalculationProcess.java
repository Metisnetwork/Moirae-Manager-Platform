package com.moirae.rosettaflow.mapper.domain;

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
 * 计算流程配置表
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_calculation_process")
public class CalculationProcess implements Serializable {


    /**
     * 计算流程ID
     */
    @TableId(type = IdType.AUTO)
    private Long calculationProcessId;

    /**
     * 计算流程中文名字
     */
    @TableField("`name`")
    private String name;

    /**
     * 计算流程英文名字
     */
    private String nameEn;

    /**
     * 状态: 0-无效，1- 有效
     */
    @TableField("`status`")
    private Integer status;

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

    /**
     * 计算步骤
     */
    @TableField(exist = false)
    private List<CalculationProcessStep> stepItem;
}
