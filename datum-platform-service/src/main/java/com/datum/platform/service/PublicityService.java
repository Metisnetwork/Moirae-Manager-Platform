package com.datum.platform.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.datum.platform.mapper.domain.Proposal;
import com.datum.platform.mapper.domain.ProposalLog;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PublicityService {
    void subscribe();

    IPage<Proposal> listProposal(Long current, Long size);

    Proposal getProposalDetails(String id);

    List<ProposalLog> listProposalLog(Long latestSynced, Long size);

    boolean saveOrUpdateBatchProposal(Collection<Proposal> proposalList, Set<String> publicityIdSet);

    Proposal getProposalById(String id);
}
