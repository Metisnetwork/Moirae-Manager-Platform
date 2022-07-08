package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 公示信息
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
@Data
@TableName("mo_publicity")
public class Publicity implements Serializable {


    /**
     * 公示的id, ipfs path
     */
    private String id;

    /**
     * 图片url
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

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
