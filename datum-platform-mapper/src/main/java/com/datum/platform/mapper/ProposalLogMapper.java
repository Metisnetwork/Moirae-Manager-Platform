package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datum.platform.mapper.domain.ProposalLog;

import java.util.List;

/**
 * <p>
 * 提案日志 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface ProposalLogMapper extends BaseMapper<ProposalLog> {

    List<ProposalLog> listByPage(Long latestSynced, Long size);
}
