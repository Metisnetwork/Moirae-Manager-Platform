package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_algorithm_variable
 * @author 
 */
@Data
public class AlgorithmVariable implements Serializable {
    /**
     * 算法变量表ID(自增长)
     */
    private Long id;

    /**
     * 算法表id
     */
    private Long algorithmId;

    /**
     * 变量key
     */
    private String varKey;

    /**
     * 变量值
     */
    private String varValue;

    /**
     * 变量类型: 1-自变量, 2-因变量
     */
    private Byte varType;

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