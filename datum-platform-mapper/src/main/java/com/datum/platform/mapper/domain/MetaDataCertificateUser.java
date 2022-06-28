package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户持有的元数据凭证
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mo_meta_data_certificate_user")
public class MetaDataCertificateUser implements Serializable {


    /**
     * 用户地址
     */
    private String address;

    /**
     * 元数据凭证ID
     */
    @TableId("meta_data_certificate_id")
    private Long metaDataCertificateId;

    /**
     * 账户余额, ERC20为金额, ERC721时 0-未持有 1-持有
     */
    private String balance;

    /**
     * 授权支付助手合约金额, ERC20为金额， ERC721时 0-未授权 1-已授权
     */
    @TableField("authorize_balance")
    private String authorizeBalance;

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
