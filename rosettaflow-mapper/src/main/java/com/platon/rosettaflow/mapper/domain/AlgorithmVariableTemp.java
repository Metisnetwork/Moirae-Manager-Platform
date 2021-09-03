package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_algorithm_variable_temp
 * @author houz
 */
@Data
@TableName(value = "t_algorithm_variable_temp")
public class AlgorithmVariableTemp implements Serializable {
    /**
     * 算法变量模板表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 算法模板表id
     */
    private Long algorithmTempId;

    /**
     * 变量类型: 1-自变量, 2-因变量
     */
    private Byte varType;

    /**
     * 变量key
     */
    private String varKey;

    /**
     * 变量值
     */
    private String varValue;

    /**
     * 变量描述
     */
    private String varDesc;

    /**
     * 状态: 0-无效，1- 有效
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}