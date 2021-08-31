package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_algorithm_variable
 *
 * @author admin
 */
@Data
@TableName(value = "t_algorithm_variable")
public class AlgorithmVariable implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 算法变量表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
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
    @TableField(value = "`status`")
    private Byte status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}