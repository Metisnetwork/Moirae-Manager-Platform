package com.datum.platform.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.chain.platon.contract.VoteContract;
import com.datum.platform.manager.ProposalLogManager;
import com.datum.platform.mapper.domain.ProposalLog;
import com.datum.platform.service.ProposalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Slf4j
@Service
public class ProposalServiceImpl implements ProposalService {

    @Resource
    private VoteContract voteContract;
    @Resource
    private PlatONProperties platONProperties;
    @Resource
    private ProposalLogManager proposalLogManager;

    @Override
    public void subscribe() {
        BigInteger begin = platONProperties.getVoteDeployBlockNumber();
        ProposalLog latest = proposalLogManager.getLatestOne();
        if(ObjectUtil.isNotNull(latest)){
            begin = new BigInteger(latest.getBlockNumber());
        }
        voteContract.subscribe(begin).subscribe(oo -> {
            oo.ifPresent(tuple2 -> {
                if(!proposalLogManager.contain(tuple2.getValue1().getBlockNumber(), tuple2.getValue1().getTransactionHash(), tuple2.getValue1().getLogIndex())){
                    //TODO

                }
            });
        });
    }
}
