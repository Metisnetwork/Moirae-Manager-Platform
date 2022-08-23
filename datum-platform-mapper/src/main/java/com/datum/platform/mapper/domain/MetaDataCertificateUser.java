package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户持有的元数据凭证
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Data
@TableName("mo_meta_data_certificate_user")
public class MetaDataCertificateUser implements Serializable {

    /**
     * 用户地址
     */
    @TableId
    private String address;

    /**
     * 元数据凭证ID
     */
    private Long metaDataCertificateId;

    /**
     * 账户余额, ERC20为数量, ERC721时 0-未持有 1-持有
     */
    private String balance;

    /**
     * 授权支付助手合约数量, ERC20为数量， ERC721时 0-未授权 1-已授权
     */
    private String authorizeBalance;

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
    private Integer noAttrNumber;
    @TableField(exist = false)
    private Integer haveAttrNumber;
    @TableField(exist = false)
    private Integer effHaveAttrNumber;
}
