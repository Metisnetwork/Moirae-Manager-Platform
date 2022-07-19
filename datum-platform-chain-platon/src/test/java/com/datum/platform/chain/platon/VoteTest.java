package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.Vote;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

public class VoteTest extends BaseContractTest {

    private String address = "lat1qsswl5suqypwl9achnq8z363n5w8dglj5u8hef";


    @Test
    public void initialize() throws Exception{
        Vote contract = load();
        TransactionReceipt transactionReceipt = contract.initialize("lat1eqpf8vxz7m64j25mkmwk39t3xf8zeltxrr2nql",  "grpc://192.168.10.152:10033").send();
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

    private Vote load(){
        return Vote.load(address, web3j, credentials, gasProvider);
    }
}
