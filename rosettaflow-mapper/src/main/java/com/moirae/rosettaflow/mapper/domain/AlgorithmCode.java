package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_algorithm_code
 *
 * @author admin
 */
@Data
@TableName(value = "t_algorithm_code")
public class AlgorithmCode implements Serializable {
    /**
     * 算法代码表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 算法id
     */
    private Long algorithmId;

    /**
     * 编辑类型:1-sql,2-noteBook
     */
    private Byte editType;

    /**
     * 计算合约
     */
    private String calculateContractCode;

    /**
     * 数据分片合约
     */
    private String dataSplitContractCode;

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

    private static final long serialVersionUID = 1L;
}