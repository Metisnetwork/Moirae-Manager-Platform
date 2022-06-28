package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.ERC721Factory;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

public class ERC721FactoryTest extends BaseContractTest {

    private String factoryAddress = "lat1jq7svadxwmn43c0a2p6ch5kuh6w6znqxshv35r";

    @Test
    public void creatToken() throws Exception {
        ERC721Factory dataTokenFactory = load();
        TransactionReceipt transactionReceipt = dataTokenFactory.deployERC721Contract("cd-721", "cd721", "metadata:0x0b708523ea411578e8cf16b0228836374a3a8282662f4ec099c43eb083ae343b", BigInteger.valueOf(3)).send();

        List<ERC721Factory.NFTContractCreatedEventResponse> eventList = dataTokenFactory.getNFTContractCreatedEvents(transactionReceipt);

        eventList.forEach(item -> {
            System.out.println("address = " + item.newTokenAddress);
        });
    }

    private ERC721Factory load(){
        return ERC721Factory.load(factoryAddress, web3j, credentials, gasProvider);
    }
}
