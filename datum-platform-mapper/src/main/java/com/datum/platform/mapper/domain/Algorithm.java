package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.AlgorithmTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * t_algorithm
 *
 * @author admin
 */
@Data
@TableName(value = "mo_algorithm")
public class Algorithm implements Serializable {

    /**
     * 算法表ID
     */
    @TableId
    private Long algorithmId;

    /**
     * 中文算法描述
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
     * 所需的运行时长,默认1小时 (单位: ms)
     */
    private Long runTime;

    /**
     * 是否需要输入模型: 0-否，1:是
     */
    private Boolean inputModel;

    /**
     * 是否产生模型: 0-否，1:是
     */
    private Boolean outputModel;

    /**
     * 是否支持默认的psi处理: 0-否，1:是
     */
    private Boolean supportDefaultPsi;

    /**
     * 是否产生psi: 0-否，1:是
     */
    private Boolean outputPsi;

    /**
     * 输出存储形式: 1-明文，2:密文
     */
    private Integer storePattern;

    /**
     * 是否判断数据行数: 0-否，1-是
     */
    private Integer dataRowsFlag;

    /**
     * 是否判断数据列数: 0-否，1-是
     */
    private Integer dataColumnsFlag;

    /**
     * 算法类别：0-密文算法，1-明文算法
     */
    private AlgorithmTypeEnum type;

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

    @TableField(exist = false)
    private AlgorithmCode algorithmCode;
    @TableField(exist = false)
    private List<AlgorithmVariable> algorithmVariableList;
    @TableField(exist = false)
    private String algorithmName;
    @TableField(exist = false)
    private String algorithmNameEn;
}
