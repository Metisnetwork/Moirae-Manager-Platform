package com.datum.platform.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.datum.platform.chain.platon.contract.VoteContract;
import com.datum.platform.chain.platon.dto.VoteConfigDto;
import com.datum.platform.mapper.domain.Proposal;
import com.datum.platform.mapper.domain.ProposalLog;
import com.datum.platform.mapper.enums.DataSyncTypeEnum;
import com.datum.platform.mapper.enums.ProposalLogTypeEnum;
import com.datum.platform.mapper.enums.ProposalStatusEnum;
import com.datum.platform.mapper.enums.ProposalTypeEnum;
import com.datum.platform.service.PublicityService;
import com.datum.platform.service.SysService;
import com.zengtengpeng.annotation.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

/**
 * 同步元数据定时任务, 多久同步一次待确认
 *
 * @author hudenian
 * @date 2021/8/23
 */
@ConditionalOnProperty(name="dev.quartz", havingValue="true")
@Slf4j
@Component
public class AnalyzeProposalTask {
    @Resource
    private SysService sysService;
    @Resource
    private VoteContract voteContract;
    @Resource
    private PublicityService publicityService;

    @Scheduled(fixedDelay = 5 * 1000)
    @Lock(keys = "AnalyzeProposalTask")
    public void run() {
        long begin = DateUtil.current();
        try {
            sysService.sync(DataSyncTypeEnum.ANALYZE_PROPOSAL.getDataType(),DataSyncTypeEnum.ANALYZE_PROPOSAL.getDesc(),//1.根据dataType同步类型获取新的同步时间DataSync
                    (latestSynced, size) -> {//2.根据新的同步时间latestSynced获取分页列表grpcResponseList
                        return publicityService.listProposalLog(latestSynced, size);
                    },
                    (proposalLogList) -> {//3.根据分页列表grpcResponseList实现实际业务逻辑
                        // 批量更新
                        return this.analyzeProposalLog(proposalLogList);
                    });
        } catch (Exception e) {
            log.error("任务分析,失败原因：{}", e.getMessage(), e);
        }
        log.info("任务分析结束，总耗时:{}ms", DateUtil.current() - begin);
    }

    private long analyzeProposalLog(List<ProposalLog> proposalLogList) {
        Map<String, Proposal> saveMap = new HashMap<>();
        VoteConfigDto voteConfigDto =  voteContract.getConfig();
        Set<String> publicityIdSet = new HashSet<>();

        proposalLogList.forEach(proposalLog -> {
            JSONObject contentJsonObject = JSONObject.parseObject(proposalLog.getContent());
            // 提交提案
            if(proposalLog.getType() == ProposalLogTypeEnum.NEWPROPOSAL_EVENT){
                Proposal proposal = new Proposal();
                proposal.setId(contentJsonObject.getString("proposalId"));
                proposal.setSubmitter(contentJsonObject.getString("submitter"));
                proposal.setCandidate(contentJsonObject.getString("candidate"));

                proposal.setPublicityId(contentJsonObject.getString("proposalUrl"));
                proposal.setSubmissionBn(contentJsonObject.getString("submitBlockNo"));
                proposal.setStatus(ProposalStatusEnum.HAS_NOT_STARTED);
                // 增加委员会成员
                if(contentJsonObject.getIntValue("proposalType") == 1){
                    proposal.setType(ProposalTypeEnum.ADD_AUTHORITY);
                    proposal.setVoteBeginBn(new BigInteger(proposal.getSubmissionBn()).add(voteConfigDto.getBeginVote()).toString());
                    proposal.setVoteEndBn(new BigInteger(proposal.getSubmissionBn()).add(voteConfigDto.getBeginVote()).add(voteConfigDto.getVote()).toString());
                    proposal.setVoteAgreeNumber(0);
                }
                // 踢出委员会成员
                if(contentJsonObject.getIntValue("proposalType") == 2){
                    proposal.setType(ProposalTypeEnum.KICK_OUT_AUTHORITY);
                    proposal.setVoteBeginBn(new BigInteger(proposal.getSubmissionBn()).add(voteConfigDto.getBeginVote()).toString());
                    proposal.setVoteEndBn(new BigInteger(proposal.getSubmissionBn()).add(voteConfigDto.getBeginVote()).add(voteConfigDto.getVote()).toString());
                    proposal.setVoteAgreeNumber(0);
                }
                // 自动退出
                if(contentJsonObject.getIntValue("proposalType") == 3){
                    proposal.setType(ProposalTypeEnum.AUTO_QUIT_AUTHORITY);
                    proposal.setAutoQuitBn(new BigInteger(proposal.getSubmissionBn()).add(voteConfigDto.getQuit()).toString());
                }
                publicityIdSet.add(proposal.getPublicityId());
                saveMap.put(proposal.getId(), proposal);
            }

            // 撤销提案
            if(proposalLog.getType() == ProposalLogTypeEnum.WITHDRAWPROPOSAL_EVENT){
                Proposal proposal = saveMap.computeIfAbsent(contentJsonObject.getString("proposalId"),id -> publicityService.getProposalById(id));
                proposal.setStatus(ProposalStatusEnum.REVOKED);
            }

            // 对提案投票
            if(proposalLog.getType() == ProposalLogTypeEnum.VOTEPROPOSAL_EVENT){
                Proposal proposal = saveMap.computeIfAbsent(contentJsonObject.getString("proposalId"),id -> publicityService.getProposalById(id));
                proposal.setVoteAgreeNumber(proposal.getVoteAgreeNumber() + 1);
            }

            // 投票结果
            if(proposalLog.getType() == ProposalLogTypeEnum.PROPOSALRESULT_EVENT){
                Proposal proposal = saveMap.computeIfAbsent(contentJsonObject.getString("proposalId"),id -> publicityService.getProposalById(id));
                proposal.setStatus(contentJsonObject.getBoolean("result")? ProposalStatusEnum.VOTE_PASS : ProposalStatusEnum.VOTE_NOT_PASS );
                proposal.setAuthorityNumber(voteContract.sizeOfAllAuthority(new BigInteger(proposalLog.getBlockNumber()).subtract(BigInteger.ONE)));
            }
        });
        publicityService.saveOrUpdateBatchProposal(saveMap.values(), publicityIdSet);
        return proposalLogList.get(proposalLogList.size() - 1).getId();
    }
}
