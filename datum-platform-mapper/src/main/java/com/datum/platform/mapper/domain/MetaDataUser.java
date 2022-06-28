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
 * 用户可见的元数据
 * </p>
 *
 * @author chendai
 * @since 2022-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mo_meta_data_user")
public class MetaDataUser implements Serializable {


    /**
     * 用户地址
     */
    private String address;

    /**
     * 元数据ID,hash
     */
    @TableId("meta_data_id")
    private String metaDataId;

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
