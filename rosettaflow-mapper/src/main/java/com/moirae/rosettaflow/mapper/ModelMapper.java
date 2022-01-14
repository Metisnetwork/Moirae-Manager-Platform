package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moirae.rosettaflow.mapper.domain.Model;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author admin
 * @date 2021/7/20
 */
public interface ModelMapper extends BaseMapper<Model> {

    List<Model> queryAvailableModel(@Param(value = "address") String address, @Param(value = "algorithmId") Long algorithmId, @Param(value = "identityId") String identityId, @Param(value = "language") String language);
}
