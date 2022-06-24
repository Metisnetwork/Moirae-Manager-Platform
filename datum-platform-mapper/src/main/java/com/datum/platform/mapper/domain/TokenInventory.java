package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * tokenId持有者信息
 * </p>
 *
 * @author chendai
 * @since 2022-03-28
 */
@Data
@TableName("mo_token_inventory")
public class TokenInventory implements Serializable {


    /**
     * 合约地址
     */
    @TableId
    private String tokenAddress;

    /**
     * token id
     */
    private String tokenId;

    /**
     * token id 对应持有者地址
     */
    private String owner;

    /**
     * 对应元数据ID,hash
     */
    private String metaDataId;

    /**
     * token id 对应特性值，如有效期
     */
    private String characteristic;

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
