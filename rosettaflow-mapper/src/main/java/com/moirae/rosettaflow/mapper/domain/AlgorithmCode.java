package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * mo_algorithm_code
 *
 * @author admin
 */
@Data
@TableName(value = "mo_algorithm_code")
public class AlgorithmCode implements Serializable {
    /**
     * 算法代码表ID
     */
    @TableId
    private Long algorithmId;

    /**
     * 编辑类型:1-sql,2-noteBook
     */
    private Integer editType;

    /**
     * 计算合约变量模板json格式结构
     */
    private String calculateContractStruct;

    /**
     * 计算合约
     */
    private String calculateContractCode;

    /**
     * 数据分片合约
     */
    private String dataSplitContractCode;

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
