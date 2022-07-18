package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户可见的元数据
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Data
@TableName("mo_meta_data_user")
public class MetaDataUser implements Serializable {


    /**
     * 用户地址
     */
    @TableId
    private String address;

    /**
     * 元数据ID,hash
     */
    private String metaDataId;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 更新时间
     */
    @TableField(update = "now()")
    private Date updateTime;

}
