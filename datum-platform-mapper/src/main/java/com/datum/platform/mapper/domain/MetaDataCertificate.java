package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.datum.platform.mapper.enums.MetaDataCertificateTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 元数据凭证
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Data
@TableName("mo_meta_data_certificate")
public class MetaDataCertificate implements Serializable {


    /**
     * ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 元数据ID,hash
     */
    private String metaDataId;

    /**
     * 凭证类型: 0-无属性,1-有属性
     */
    private MetaDataCertificateTypeEnum type;

    /**
     * 凭证对应合约地址
     */
    private String tokenAddress;

    /**
     * 有属性凭证对应 token id
     */
    private String tokenId;

    /**
     * 是否支持明文算法
     */
    private Boolean isSupportPtAlg;

    /**
     * 是否支持密文算法
     */
    private Boolean isSupportCtAlg;

    /**
     * 有属性凭证有效期
     */
    private String characteristic;

    /**
     * 无属性凭证明文算法消耗量
     */
    private String erc20PtAlgConsume;

    /**
     * 无属性凭证密文算法消耗量
     */
    private String erc20CtAlgConsume;

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
    private String tokenName;
    @TableField(exist = false)
    private String tokenSymbol;
    @TableField(exist = false)
    private Long tokenDecimal;
    @TableField(exist = false)
    private String tokenBalance;
    @TableField(exist = false)
    private String authorizeBalance;
}
