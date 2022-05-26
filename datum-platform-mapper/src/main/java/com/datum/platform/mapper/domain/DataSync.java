package com.datum.platform.mapper.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author liushuyu
 * @Date 2022/1/5 12:23
 * @Version
 * @Desc
 */

@Getter
@Setter
@ToString
@TableName(value = "mo_data_sync")
public class DataSync {

    @TableField("data_type")
    private String dataType;

    @TableField("latest_synced")
    private long latestSynced;

    @TableField("info")
    private String info;
}
