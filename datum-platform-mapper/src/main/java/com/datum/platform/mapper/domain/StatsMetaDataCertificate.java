package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "mo_stats_meta_data_certificate")
public class StatsMetaDataCertificate implements Serializable {

    /**
     * 数据凭证ID
     */
    @TableId
    private Long metaDataCertificateId;

    /**
     * 使用次数
     */
    private Long usageCount;

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    private String tokenAddress;
    @TableField(exist = false)
    private String tokenName;
}
