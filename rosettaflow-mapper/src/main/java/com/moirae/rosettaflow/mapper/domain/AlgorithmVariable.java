package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * mo_algorithm_variable
 *
 * @author admin
 */
@Data
@TableName(value = "mo_algorithm_variable")
public class AlgorithmVariable implements Serializable {

    /**
     * 算法id
     */
    @TableId
    private Long algorithmId;

    /**
     * 变量key
     */
    @TableId("var_key")
    private String varKey;

    /**
     * 变量类型. 1-boolean, 2-number, 3-string, 4-numberArray, 5-stringArray
     */
    @TableField("var_type")
    private Integer varType;

    /**
     * 变量默认值
     */
    @TableField("var_value")
    private String varValue;

    /**
     * 变量中文描述
     */
    @TableField("var_desc")
    private String varDesc;

    /**
     * 变量英文描述
     */
    @TableField("var_desc_en")
    private String varDescEn;

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
