package com.platon.rosettaflow.dto;

import com.platon.rosettaflow.mapper.domain.MetaData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 功能描述
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetaDataDto extends MetaData {
    /**
     * 授权状态:0: 未知; 1: 还未发布的新表; 2: 已发布的表; 3: 已撤销的表
     */
    private Byte authStatus;
    /**
     * 授权方式: 1-按时间, 2-按次数, 3-永久
     */
    private Byte authType;
}
