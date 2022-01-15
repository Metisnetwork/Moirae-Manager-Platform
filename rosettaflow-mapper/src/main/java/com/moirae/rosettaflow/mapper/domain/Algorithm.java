package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_algorithm
 *
 * @author admin
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
     * 算法编码（线性回归、逻辑回归、。。。）
     */
    private String algorithmCode;

    /**
     * 算法编码中步骤（如训练-1, 预测-2）
     */
    private Integer algorithmStep;

    /**
     * 算法名称
     */
    private String algorithmName;

    /**
     * 英文算法名称
     */
    private String algorithmNameEn;

    /**
     * 算法描述
     */
    private String algorithmDesc;

    /**
     * 英文算法描述
     */
    private String algorithmDescEn;

    /**
     * 算法作者
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
     * 是否需要输入模型: 0-否，1:是
     */
    private Byte inputModel;

    /**
     * 是否产生模型: 0-否，1:是
     */
    private Byte outputModel;

    /**
     * 所需的运行时长 (单位: ms)
     */
    private Byte storePattern;

    /**
     * 是否判断数据行数: 0-否，1-是
     */
    private Byte dataRowsFlag;

    /**
     * 是否判断数据列数: 0-否，1-是
     */
    private Byte dataColumnsFlag;

    /**
     * 是否是共有算法
     */
    private Byte publicFlag;

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

    /**
     * 算法id
     */
    @TableField(exist = false)
    private Long algorithmId;
    /**
     * 算法编辑类型:1-sql, 2-noteBook
     */
    @TableField(exist = false)
    private Byte editType;
    /**
     * 算法代码（计算合约）
     */
    @TableField(exist = false)
    private String calculateContractCode;
}
