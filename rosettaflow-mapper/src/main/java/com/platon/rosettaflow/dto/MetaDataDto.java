package com.platon.rosettaflow.dto;

import com.platon.rosettaflow.mapper.domain.MetaData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/25
 * @description 功能描述
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MetaDataDto extends MetaData {

    List<MetaDataDetailsDto> metaDataDetailsDtoList;
}
