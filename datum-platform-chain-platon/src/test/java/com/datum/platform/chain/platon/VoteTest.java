package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.Vote;
import com.platon.crypto.Credentials;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class VoteTest extends BaseContractTest {

    private String address = "lat1u5j3rr5a6kydwhpfyhuduqd0cn7z9su35epap2";
    private static final String org135 = "0x68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f80";
    private static final String org152 = "0x68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f81";
    private static final String org153 = "0x68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f82";
    private static final String org154 = "0x68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f83";
    private static final String org155 = "0x68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f84";
    private static final String org156 = "0x68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f85";

    private static final String org135Url = "grpc://192.168.120.135:10033";
    private static final String org152Url = "grpc://192.168.10.152:10033";
    private static final String org153Url = "grpc://192.168.10.153:10033";
    private static final String org154Url = "grpc://192.168.10.154:10033";
    private static final String org155Url = "grpc://192.168.10.155:10033";
    private static final String org156Url = "grpc://192.168.10.156:10033";


    /**
     * 组织 org152 加入成功
     * @throws Exception
     */
    @Test
    public void case1() throws Exception{
        Vote contract = load(org135);
        // org135 发起提案
        TransactionReceipt submitTransactionReceipt = contract.submitProposal(BigInteger.valueOf(1L), "ipfs://QmTvnBq5t3EKosTC3Nf53frG4KFL33qYkeWhdsTJLaddnz/org152-p.json", Credentials.create(org152).getAddress(),  org152Url).send();
        BigInteger proposalId = contract.getNewProposalEvents(submitTransactionReceipt).get(0).proposalId;

        // 等待2分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 提案投票
        contract.voteProposal(proposalId).send();

        // 等待4分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 根据投票结果生效提案
        contract.effectProposal(proposalId).send();
    }


    /**
     * 组织 org153 加入成功
     * @throws Exception
     */
    @Test
    public void case2() throws Exception{
        Vote contract = load(org135);
        // org135 发起提案
        TransactionReceipt submitTransactionReceipt = contract.submitProposal(BigInteger.valueOf(1L), "ipfs://QmTvnBq5t3EKosTC3Nf53frG4KFL33qYkeWhdsTJLaddnz/org153-p.json", Credentials.create(org153).getAddress(),  org153Url).send();
        BigInteger proposalId = contract.getNewProposalEvents(submitTransactionReceipt).get(0).proposalId;

        // 等待2分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 提案投票
        contract.voteProposal(proposalId).send();
        load(org152).voteProposal(proposalId).send();

        // 等待4分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 根据投票结果生效提案
        contract.effectProposal(proposalId).send();
    }

    /**
     * 组织 org154 加入投票失败
     * @throws Exception
     */
    @Test
    public void case3() throws Exception{
        Vote contract = load(org135);
        // org135 发起提案
        TransactionReceipt submitTransactionReceipt = contract.submitProposal(BigInteger.valueOf(1L), "ipfs://QmTvnBq5t3EKosTC3Nf53frG4KFL33qYkeWhdsTJLaddnz/org154-p.json", Credentials.create(org154).getAddress(),  org154Url).send();
        BigInteger proposalId = contract.getNewProposalEvents(submitTransactionReceipt).get(0).proposalId;

        // 等待4分钟后
        TimeUnit.MINUTES.sleep(4);

        // org135 根据投票结果生效提案
        contract.effectProposal(proposalId).send();
    }

    /**
     * 组织 org154 加入成功
     * @throws Exception
     */
    @Test
    public void case4() throws Exception{
        Vote contract = load(org135);
        // org135 发起提案
        TransactionReceipt submitTransactionReceipt = contract.submitProposal(BigInteger.valueOf(1L), "ipfs://QmTvnBq5t3EKosTC3Nf53frG4KFL33qYkeWhdsTJLaddnz/org154-p.json", Credentials.create(org154).getAddress(),  org154Url).send();
        BigInteger proposalId = contract.getNewProposalEvents(submitTransactionReceipt).get(0).proposalId;

        // 等待2分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 提案投票
        contract.voteProposal(proposalId).send();
        load(org152).voteProposal(proposalId).send();
        load(org153).voteProposal(proposalId).send();

        // 等待4分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 根据投票结果生效提案
        contract.effectProposal(proposalId).send();
    }

    /**
     * 组织 org154 主动退出
     * @throws Exception
     */
    @Test
    public void case5() throws Exception{
        Vote contract = load(org154);
        // org154 发起退出
        TransactionReceipt submitTransactionReceipt = contract.submitProposal(BigInteger.valueOf(3L), "ipfs://QmTvnBq5t3EKosTC3Nf53frG4KFL33qYkeWhdsTJLaddnz/org154-p.json", Credentials.create(org154).getAddress(),  org154Url).send();
        BigInteger proposalId = contract.getNewProposalEvents(submitTransactionReceipt).get(0).proposalId;

        // 等待2分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 根据投票结果生效提案
        contract.effectProposal(proposalId).send();
    }

    /**
     * 组织 org153 被踢出
     * @throws Exception
     */
    @Test
    public void case6() throws Exception{
        Vote contract = load(org153);
        // org135 发起提案
        TransactionReceipt submitTransactionReceipt = contract.submitProposal(BigInteger.valueOf(2L), "ipfs://QmTvnBq5t3EKosTC3Nf53frG4KFL33qYkeWhdsTJLaddnz/org153-p.json", Credentials.create(org153).getAddress(),  org153Url).send();
        BigInteger proposalId = contract.getNewProposalEvents(submitTransactionReceipt).get(0).proposalId;

        // 等待2分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 提案投票
        load(org135).voteProposal(proposalId).send();
        load(org152).voteProposal(proposalId).send();
        load(org153).voteProposal(proposalId).send();

        // 等待4分钟后
        TimeUnit.MINUTES.sleep(2);

        // org135 根据投票结果生效提案
        contract.effectProposal(proposalId).send();
    }

    /**
     * 组织 org153 被踢出
     * @throws Exception
     */
    @Test
    public void case7() throws Exception{
        Vote contract = load(org152);
        // org135 发起提案
        TransactionReceipt submitTransactionReceipt = contract.submitProposal(BigInteger.valueOf(1L), "ipfs://QmTvnBq5t3EKosTC3Nf53frG4KFL33qYkeWhdsTJLaddnz/org154-p.json", Credentials.create(org154).getAddress(),  org154Url).send();
        BigInteger proposalId = contract.getNewProposalEvents(submitTransactionReceipt).get(0).proposalId;

        // org135 根据投票结果生效提案
        contract.withdrawProposal(proposalId).send();
    }



    @Test
    public void initialize() throws Exception{
        Vote contract = load();
        TransactionReceipt transactionReceipt = contract.initialize(Credentials.create(org135).getAddress(),  "org135Url").send();
        System.out.println(transactionReceipt.getTransactionHash());
    }

    @Test
    public void getAdmin() throws Exception{
        Vote contract = load();
        System.out.println(contract.getAdmin().send());
    }

    @Test
    public void getAllAuthority() throws Exception{
        Vote contract = load();
        System.out.println(contract.getAllAuthority().send());
    }

    @Test
    public void getInterval() throws Exception{
        Vote contract = load();
        // 投票准备期
        System.out.println(contract.getInterval(BigInteger.valueOf(1)).send());
        // 投票准备期
        System.out.println(contract.getInterval(BigInteger.valueOf(2)).send());
        // 退出期
        System.out.println(contract.getInterval(BigInteger.valueOf(4)).send());
    }

    @Test
    public void setInterval() throws Exception{
        Vote contract = load(org135);
        System.out.println(contract.setInterval(BigInteger.valueOf(1), BigInteger.valueOf(1 * 60)).send());
        System.out.println(contract.setInterval(BigInteger.valueOf(2), BigInteger.valueOf(2 * 60)).send());
        System.out.println(contract.setInterval(BigInteger.valueOf(4), BigInteger.valueOf(1 * 60)).send());
    }

    private Vote load(){
        return Vote.load(address, web3j, credentials, gasProvider);
    }

    private Vote load(String sk){
        return Vote.load(address, web3j, Credentials.create(sk), gasProvider);
    }
}
