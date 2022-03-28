package com.moirae.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("mo_user_login")
public class UserLogin implements Serializable {

    /**
     * 日志表id(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录地址
     */
    private String address;

    /**
     * 是否成功: 0-否，1-是
     */
    private Boolean isSuccess;

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
