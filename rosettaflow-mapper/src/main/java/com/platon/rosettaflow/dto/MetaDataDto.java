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

    IPage<MetaDataDetailsDto> metaDataDetailsDtoPageList;
}
