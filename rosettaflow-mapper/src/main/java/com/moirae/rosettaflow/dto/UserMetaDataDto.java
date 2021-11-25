package com.moirae.rosettaflow.dto;

import com.moirae.rosettaflow.mapper.domain.UserMetaData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hudenian
 * @date 2021/8/26
 * @description 功能描述
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMetaDataDto extends UserMetaData {

    private String dataName;

    private Byte userType;

    private String sign;
    /**
     * t_meta_data表主键id
     */
    private Long metaDataPkId;

    /**
     * 转换后展示状态: 0-申请中, 1-已授权, 2-已拒绝, 3-已失效
     */
    private Byte authStatusShow;

}
