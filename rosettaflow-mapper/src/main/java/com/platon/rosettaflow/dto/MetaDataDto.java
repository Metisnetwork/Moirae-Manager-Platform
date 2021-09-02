package com.platon.rosettaflow.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * 授权状态:-1未知(未发起过数据授权申请) 0-等待审核中, 1-审核通过, 2-审核拒绝
     */
    Byte authStatus;

    IPage<MetaDataDetailsDto> metaDataDetailsDtoPageList;
}
