package com.platon.rosettaflow.dto;

import com.platon.rosettaflow.mapper.domain.UserMetaData;
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
}
