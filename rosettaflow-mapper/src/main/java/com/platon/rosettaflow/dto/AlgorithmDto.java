package com.platon.rosettaflow.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author houz
 */
@Data
public class AlgorithmDto {

    /**
     * 算法id
     */
    private Long algorithmId;

    /**
     * 算法名称
     */
    private String algorithmName;

    /**
     * 算法描述
     */
    private String algorithmDesc;

    /**
     * 作者
     */
    private String author;

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
    private Integer costCpu;

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
    private Long runTime;

    /**
     * 代码类型
     */
    private Byte editType;

    /**
     * 算法代码
     */
    private String calculateContractCode;

}
