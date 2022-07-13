package com.datum.platform.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.Proposal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 提案信息 Mapper 接口
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface ProposalMapper extends BaseMapper<Proposal> {

    IPage<Proposal> list(Page<Proposal> page);

    Proposal getDetailsById(String id);
}
