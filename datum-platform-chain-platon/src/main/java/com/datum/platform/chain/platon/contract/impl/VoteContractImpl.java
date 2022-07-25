package com.datum.platform.chain.platon.contract.impl;

import com.datum.platform.chain.platon.PlatONClient;
import com.datum.platform.chain.platon.config.PlatONProperties;
import com.datum.platform.chain.platon.contract.VoteContract;
import com.datum.platform.chain.platon.contract.evm.Vote;
import com.datum.platform.chain.platon.dto.AuthorityDto;
import com.datum.platform.chain.platon.enums.CodeEnum;
import com.datum.platform.chain.platon.exception.AppException;
import com.datum.platform.chain.platon.function.ExceptionFunction;
import com.datum.platform.chain.platon.utils.AddressUtils;
import com.platon.abi.solidity.EventEncoder;
import com.platon.abi.solidity.EventValues;
import com.platon.bech32.Bech32;
import com.platon.protocol.core.DefaultBlockParameter;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.request.PlatonFilter;
import com.platon.protocol.core.methods.response.Log;
import com.platon.tuples.generated.Tuple2;
import com.platon.tuples.generated.Tuple3;
import com.platon.tx.Contract;
import com.platon.tx.ReadonlyTransactionManager;
import com.platon.tx.gas.ContractGasProvider;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class VoteContractImpl implements VoteContract {

    @Resource
    private PlatONClient platOnClient;
    @Resource
    private PlatONProperties platONProperties;
    private BigInteger rangeBeginVote;
    private BigInteger rangeVote;
    private BigInteger rangeQuit;

    public final static String newProposalSignature = EventEncoder.encode(Vote.NEWPROPOSAL_EVENT);
    public final static String proposalResultSignature = EventEncoder.encode(Vote.PROPOSALRESULT_EVENT);
    public final static String voteProposalSignature = EventEncoder.encode(Vote.VOTEPROPOSAL_EVENT);
    public final static String withdrawProposalSignature = EventEncoder.encode(Vote.WITHDRAWPROPOSAL_EVENT);
    public final static String authorityDeleteSignature = EventEncoder.encode(Vote.AUTHORITYDELETE_EVENT);
    public final static String authorityAddSignature = EventEncoder.encode(Vote.AUTHORITYADD_EVENT);

    @Override
    public void init() {
        rangeBeginVote = query(contract -> contract.getInterval(BigInteger.valueOf(1)), platONProperties.getVoteAddress());
        rangeVote = query(contract -> contract.getInterval(BigInteger.valueOf(2)), platONProperties.getVoteAddress());
        rangeQuit = query(contract -> contract.getInterval(BigInteger.valueOf(4)), platONProperties.getVoteAddress());
    }

    @Override
    public Tuple3<BigInteger, BigInteger, BigInteger> getConfig() {
        return new Tuple3<>(rangeBeginVote, rangeVote, rangeQuit);
    }

    @Override
    public List<AuthorityDto> getAllAuthority() {
        Tuple3<List<String>, List<String>, List<BigInteger>> result = query(contract -> contract.getAllAuthority(), platONProperties.getVoteAddress());
        List<AuthorityDto> resultList = new ArrayList<>();
        for (int i = 0; i < result.getValue1().size(); i++) {
            AuthorityDto authorityDto = new AuthorityDto();
            authorityDto.setAddress(Bech32.addressDecodeHex(result.getValue1().get(i)));
            authorityDto.setServiceUrl(result.getValue2().get(i));
            authorityDto.setJoinTime(new Date(result.getValue3().get(i).longValue()));
            resultList.add(authorityDto);
        }
        return resultList;
    }

    @Override
    public Observable<Optional<Tuple2<Log, Object>>> subscribe(BigInteger beginBN) {
        PlatonFilter filter = new PlatonFilter(DefaultBlockParameter.valueOf(beginBN), DefaultBlockParameterName.LATEST, platONProperties.getVoteAddress());
        filter.addOptionalTopics(newProposalSignature, proposalResultSignature, voteProposalSignature, withdrawProposalSignature, authorityAddSignature, authorityDeleteSignature);
        return platOnClient.getWeb3j().platonLogObservable(filter).map(log -> {
            List<String> topics = log.getTopics();
            if (topics == null || topics.size() == 0) {
                return Optional.empty();
            }
            if(topics.get(0).equals(newProposalSignature)){
                EventValues eventValues = Contract.staticExtractEventParameters(Vote.NEWPROPOSAL_EVENT, log);
                Vote.NewProposalEventResponse typedResponse = new Vote.NewProposalEventResponse();
                typedResponse.proposalId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.proposalType = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.submitter = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse.candidate = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.candidateServiceUrl = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.proposalUrl = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.submitBlockNo = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return Optional.of(new Tuple2<>(log, typedResponse));
            } else if(topics.get(0).equals(proposalResultSignature)){
                EventValues eventValues = Contract.staticExtractEventParameters(Vote.PROPOSALRESULT_EVENT, log);
                Vote.ProposalResultEventResponse typedResponse = new Vote.ProposalResultEventResponse();
                typedResponse.proposalId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.result = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return Optional.of(new Tuple2<>(log, typedResponse));
            } else if(topics.get(0).equals(voteProposalSignature)){
                EventValues eventValues = Contract.staticExtractEventParameters(Vote.VOTEPROPOSAL_EVENT, log);
                Vote.VoteProposalEventResponse typedResponse = new Vote.VoteProposalEventResponse();
                typedResponse.proposalId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.voter = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return Optional.of(new Tuple2<>(log, typedResponse));
            } else if(topics.get(0).equals(withdrawProposalSignature)){
                EventValues eventValues = Contract.staticExtractEventParameters(Vote.WITHDRAWPROPOSAL_EVENT, log);
                Vote.WithdrawProposalEventResponse typedResponse = new Vote.WithdrawProposalEventResponse();
                typedResponse.proposalId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.blockNo = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return Optional.of(new Tuple2<>(log, typedResponse));
            }  else if(topics.get(0).equals(authorityDeleteSignature)){
                EventValues eventValues = Contract.staticExtractEventParameters(Vote.AUTHORITYDELETE_EVENT, log);
                Vote.AuthorityDeleteEventResponse typedResponse = new Vote.AuthorityDeleteEventResponse();
                typedResponse.addr = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.serviceUrl = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.joinTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return Optional.of(new Tuple2<>(log, typedResponse));
            }  else if(topics.get(0).equals(authorityAddSignature)){
                EventValues eventValues = Contract.staticExtractEventParameters(Vote.AUTHORITYADD_EVENT, log);
                Vote.AuthorityAddEventResponse typedResponse = new Vote.AuthorityAddEventResponse();
                typedResponse.addr = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.serviceUrl = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.joinTime = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return Optional.of(new Tuple2<>(log, typedResponse));
            } else {
                return Optional.empty();
            }
        });
    }

    private <R> R query(ExceptionFunction<Vote, RemoteCall<R>> supplier, String contractAddress) {
        contractAddress = AddressUtils.hexToBech32(contractAddress);
        ReadonlyTransactionManager transactionManager = new ReadonlyTransactionManager(platOnClient.getWeb3j(), contractAddress);
        try {
            Vote dataTokenTemplate = Vote.load(contractAddress, platOnClient.getWeb3j(), transactionManager, new ContractGasProvider(BigInteger.ZERO, BigInteger.ZERO));
            return supplier.apply(dataTokenTemplate).send();
        }   catch (SocketTimeoutException e) {
            throw new AppException(CodeEnum.CALL_RPC_READ_TIMEOUT,CodeEnum.CALL_RPC_READ_TIMEOUT.getName(),e);
        }  catch (IOException e) {
            throw new AppException(CodeEnum.CALL_RPC_NET_ERROR,CodeEnum.CALL_RPC_NET_ERROR.getName(),e);
        } catch (Exception e) {
            throw new AppException(CodeEnum.CALL_RPC_ERROR,CodeEnum.CALL_RPC_ERROR.getName(),e);
        }
    }
}
