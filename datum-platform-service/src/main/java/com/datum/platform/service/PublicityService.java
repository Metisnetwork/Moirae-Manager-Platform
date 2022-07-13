package com.datum.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.Proposal;

public interface PublicityService {
    void subscribe();

    IPage<Proposal> listProposal(Long current, Long size);

    Proposal getProposalDetails(String id);
}
