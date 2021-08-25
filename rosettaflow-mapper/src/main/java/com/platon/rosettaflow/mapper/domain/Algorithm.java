package com.platon.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_algorithm
 * @author 
 */
@Data
@TableName(value = "t_algorithm")
public class Algorithm implements Serializable {
    /**
     * 算法表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 算法名称
     */
    private String algorithmName;

    /**
     * 算法描述
     */
    private String algorithmDesc;

    /**
     * 支持协同方最大数量
     */
    private Long maxNumbers;

    /**
     * 支持协同方最小数量
     */
    private Long minNumbers;

    /**
     * 支持语言,多个以","进行分隔
     */
    private String supportLanguage;

    /**
     * 支持操作系统,多个以","进行分隔
     */
    private String supportOsSystem;

    /**
     * 算法所属大类:1-统计分析,2-特征工程,3-机器学习
     */
    private Byte algorithmType;

    /**
     * 所需的内存 (单位: byte)
     */
    private Long costMem;

    /**
     * 所需的核数 (单位: 个)
     */
    private Long costProcessor;

    /**
     * GPU核数(单位：核)
     */
    private Integer costGpu;

    /**
     * 所需的带宽 (单位: bps)
     */
    private Long costBandwidth;

    /**
     * 所需的运行时长 (单位: ms)
     */
    private Long duration;

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