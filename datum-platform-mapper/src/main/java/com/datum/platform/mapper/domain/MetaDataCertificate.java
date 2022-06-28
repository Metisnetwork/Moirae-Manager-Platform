package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 元数据凭证
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mo_meta_data_certificate")
public class MetaDataCertificate implements Serializable {


    /**
     * ID(自增长)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 元数据ID,hash
     */
    @TableField("meta_data_id")
    private String metaDataId;

    /**
     * 凭证类型: 0-无属性,1-有属性
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 凭证对应合约地址
     */
    @TableField("token_address")
    private String tokenAddress;

    /**
     * 有属性凭证对应 token id
     */
    @TableField("token_id")
    private String tokenId;

    /**
     * 是否支持明文算法
     */
    @TableField("is_support_pt_alg")
    private Integer isSupportPtAlg;

    /**
     * 是否支持密文算法
     */
    @TableField("is_support_ct_alg")
    private Integer isSupportCtAlg;

    /**
     * token id 对应特性值，有效期时间戳
     */
    private String characteristic;

    /**
     * 无属性对应DEX中的价格
     */
    private String price;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
