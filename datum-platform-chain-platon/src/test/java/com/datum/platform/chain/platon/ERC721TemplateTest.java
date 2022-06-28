package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.ERC721Template;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

public class ERC721TemplateTest extends BaseContractTest {

    private String address = "lat120wduf4gjnswzrt8ae8lcs32yem5esqduqu0qj";

    @Test
    public void createToken() throws Exception{
        ERC721Template contract = load(address);
        TransactionReceipt transactionReceipt = contract.createToken("20000", false,  "https://ipfs.io/ipfs/QmXCamtKUQLfAkbmtPfmsGXEUCz88Mnsuxs4CzAH61VQKe/image1.json" ).send();

        List<ERC721Template.TransferEventResponse> transferEventResponseList = contract.getTransferEvents(transactionReceipt);
        transferEventResponseList.forEach(item ->{
            System.out.println("from = " + item.from + " to = " + item.to + " tokenId = " + item.tokenId );
        });
    }

    @Test
    public void queryTokenInfo() throws Exception{
        ERC721Template contract = load(address);
        System.out.println(contract.name().send());
        System.out.println(contract.symbol().send());
    }


    @Test
    public void queryTokenIdInfo() throws Exception{
        ERC721Template contract = load(address);
        BigInteger total = contract.totalSupply().send();

        for (int i = 0; i < total.intValue(); i++) {
            System.out.println(contract.getExtInfo(BigInteger.valueOf(0)).send());
            System.out.println(contract.tokenURI(BigInteger.valueOf(0)).send());
        }
    }

    @Test
    public void queryTokenIdInfo1() throws Exception{
        ERC721Template contract = load(address);
        BigInteger total = contract.balanceOf(credentials.getAddress()).send();
        for (int i = 0; i < total.intValue(); i++) {
            BigInteger tokenId = contract.tokenOfOwnerByIndex(credentials.getAddress(), BigInteger.valueOf(i)).send();

            System.out.println(contract.getExtInfo(tokenId).send());
            System.out.println(contract.tokenURI(tokenId).send());
        }
    }



    private ERC721Template load(String address){
        return ERC721Template.load(address, web3j, credentials, gasProvider);
    }
}
