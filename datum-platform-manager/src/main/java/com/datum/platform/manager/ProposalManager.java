package com.datum.platform.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.mapper.domain.Proposal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 提案信息 服务类
 * </p>
 *
 * @author chendai
 * @since 2022-07-08
 */
public interface ProposalManager extends IService<Proposal> {

    IPage<Proposal> list(Page<Proposal> page);

    Proposal getDetailsById(String id);
}
