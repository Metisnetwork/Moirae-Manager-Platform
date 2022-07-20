package com.datum.platform.chain.platon.contract;

import com.datum.platform.chain.platon.dto.AuthorityDto;
import com.platon.protocol.core.methods.response.Log;
import com.platon.tuples.generated.Tuple2;
import com.platon.tuples.generated.Tuple3;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface VoteContract {

    /**
     * 初始化合约，用户一次性保存合约的设置
     *
     */
    void init();

    /**
     * 查询投票合约的设置
     *
     * @return 1-提交提案到投票开始时间间隔  2-投票的时间间隔  3-退出候选人犹豫期的时间间隔
     */
    Tuple3<BigInteger, BigInteger, BigInteger> getConfig();

    /**
     * 查询已生效的委员会列表
     *
     * @return 1-委员会地址  2-委员会服务url
     */
    List<AuthorityDto> getAllAuthority();

    /**
     * 添加对投票合约事件监听
     *
     * @param beginBN
     * @return Object in [ProposalResultEventResponse, VoteProposalEventResponse, NewProposalEventResponse, WithdrawProposalEventResponse]
     */
    Observable<Optional<Tuple2<Log, Object>>> subscribe(BigInteger beginBN);

}
