package com.datum.platform.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.chain.platon.contract.VoteContract;
import com.datum.platform.chain.platon.contract.evm.Vote;
import com.datum.platform.manager.ProposalLogManager;
import com.datum.platform.manager.ProposalManager;
import com.datum.platform.manager.PublicityManager;
import com.datum.platform.mapper.domain.OrgVc;
import com.datum.platform.mapper.domain.Proposal;
import com.datum.platform.mapper.domain.ProposalLog;
import com.datum.platform.mapper.domain.Publicity;
import com.datum.platform.mapper.enums.ProposalLogTypeEnum;
import com.datum.platform.mapper.enums.ProposalStatusEnum;
import com.datum.platform.mapper.enums.ProposalTypeEnum;
import com.datum.platform.service.OrgService;
import com.datum.platform.service.PublicityService;
import com.platon.protocol.core.methods.response.Log;
import com.platon.tuples.generated.Tuple3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Slf4j
@Service
public class PublicityServiceImpl implements PublicityService {

    @Resource
    private VoteContract voteContract;
    @Resource
    private PlatONProperties platONProperties;
    @Resource
    private ProposalLogManager proposalLogManager;
    @Resource
    private ProposalManager proposalManager;
    @Resource
    private PublicityManager publicityManager;
    @Resource
    private OrgService orgService;

    @Override
    public void subscribe() {
        BigInteger begin = platONProperties.getVoteDeployBlockNumber();
        ProposalLog latest = proposalLogManager.getLatestOne();
        if(ObjectUtil.isNotNull(latest)){
            begin = new BigInteger(latest.getBlockNumber());
        }
        voteContract.subscribe(begin).subscribe(oo -> oo.ifPresent(tuple2 -> {
            if(!proposalLogManager.contain(tuple2.getValue1().getBlockNumber(), tuple2.getValue1().getTransactionHash(), tuple2.getValue1().getLogIndex())){
                ProposalLog save = new ProposalLog();
                Log log = tuple2.getValue1();
                save.setBlockNumber(log.getBlockNumber().toString());
                save.setTxHash(log.getTransactionHash());
                save.setLogIndex(log.getLogIndex().toString());
                if(tuple2.getValue2() instanceof Vote.NewProposalEventResponse ){
                    Vote.NewProposalEventResponse newProposalEventResponse = (Vote.NewProposalEventResponse) tuple2.getValue2();
                    save.setProposalId(newProposalEventResponse.proposalId.toString());
                    save.setType(ProposalLogTypeEnum.NEWPROPOSAL_EVENT);
                    save.setContent(JSON.toJSONString(newProposalEventResponse));
                    // 新增提案
                    Proposal proposal = new Proposal();
                    proposal.setId(newProposalEventResponse.proposalId.toString());
                    proposal.setSubmitter(newProposalEventResponse.submitter);
                    proposal.setCandidate(newProposalEventResponse.candidate);
                    proposal.setType(ProposalTypeEnum.find(newProposalEventResponse.proposalType));
                    proposal.setPublicityId(newProposalEventResponse.proposalUrl);
                    proposal.setSubmissionBn(log.getBlockNumber().toString());
                    Tuple3<BigInteger, BigInteger, BigInteger> config = voteContract.getConfig();
                    proposal.setVoteBeginBn(log.getBlockNumber().add(config.getValue1()).toString());
                    proposal.setVoteEndBn(log.getBlockNumber().add(config.getValue1()).add(config.getValue2()).toString());
                    proposal.setVoteAgreeNumber(0);
                    proposal.setStatus(ProposalStatusEnum.HAS_NOT_STARTED);
                    proposal.setRemark("");
                    proposalManager.save(proposal);
                    Publicity publicity = new Publicity();
                    publicity.setId(newProposalEventResponse.proposalUrl);
                    publicityManager.save(publicity);
                }
                if(tuple2.getValue2() instanceof Vote.VoteProposalEventResponse ){
                    Vote.VoteProposalEventResponse newProposalEventResponse = (Vote.VoteProposalEventResponse) tuple2.getValue2();
                    save.setProposalId(newProposalEventResponse.proposalId.toString());
                    save.setType(ProposalLogTypeEnum.VOTEPROPOSAL_EVENT);
                    save.setContent(JSON.toJSONString(newProposalEventResponse));
                    // 提案投票
                    Proposal proposal = proposalManager.getById(newProposalEventResponse.proposalId.toString());
                    proposal.setVoteAgreeNumber(proposal.getVoteAgreeNumber() + 1);
                    proposalManager.updateById(proposal);
                }
                if(tuple2.getValue2() instanceof Vote.ProposalResultEventResponse ){
                    Vote.ProposalResultEventResponse newProposalEventResponse = (Vote.ProposalResultEventResponse) tuple2.getValue2();
                    save.setProposalId(newProposalEventResponse.proposalId.toString());
                    save.setType(ProposalLogTypeEnum.PROPOSALRESULT_EVENT);
                    save.setContent(JSON.toJSONString(newProposalEventResponse));
                    // 投票统计结果
                    Proposal proposal = proposalManager.getById(newProposalEventResponse.proposalId.toString());
                    proposal.setStatus(newProposalEventResponse.result ? ProposalStatusEnum.VOTE_PASS : ProposalStatusEnum.VOTE_NOT_PASS);
                    proposalManager.updateById(proposal);
                }
                if(tuple2.getValue2() instanceof Vote.WithdrawProposalEventResponse ){
                    Vote.WithdrawProposalEventResponse newProposalEventResponse = (Vote.WithdrawProposalEventResponse) tuple2.getValue2();
                    save.setProposalId(newProposalEventResponse.proposalId.toString());
                    save.setType(ProposalLogTypeEnum.WITHDRAWPROPOSAL_EVENT);
                    save.setContent(JSON.toJSONString(newProposalEventResponse));
                    // 撤销提案
                    Proposal proposal = proposalManager.getById(newProposalEventResponse.proposalId.toString());
                    proposal.setStatus(ProposalStatusEnum.REVOKED);
                    proposalManager.updateById(proposal);
                }
                proposalLogManager.save(save);
            }
        }));
    }

    @Override
    public IPage<Proposal> listProposal(Long current, Long size) {
        Page<Proposal> page = new Page<>(current, size);
        IPage<Proposal> iPage = proposalManager.list(page);


        return iPage;
    }

}
