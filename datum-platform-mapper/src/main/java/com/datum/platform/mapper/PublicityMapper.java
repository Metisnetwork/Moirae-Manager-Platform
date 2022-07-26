package com.datum.platform.mapper;

import com.datum.platform.mapper.domain.Publicity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 公示信息 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface PublicityMapper extends BaseMapper<Publicity> {

    List<Publicity> listNeedSync();
}
