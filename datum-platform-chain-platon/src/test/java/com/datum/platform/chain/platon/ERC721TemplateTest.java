package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.ERC721Template;
import com.platon.bech32.Bech32;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

public class ERC721TemplateTest extends BaseContractTest {

    private String address = "lat1j5c3rhakmdkmfktxfx44w6ttrsspyvl7gy5vwe";


    /**
     * "ERC20Template": "0x14B26A38146532D317Af70905f227986A7308A7E",
     * "ERC20Factory": "0xF1cd24c3ebb7cDe2087371D67a6532dF4B1AbE71",
     * "ERC721Template": "0x648058A9eF81B490D7241A293426C5e1Fd7428e8",
     * "ERC721Factory": "0x9E9b9dCE1aa6a060e573936D8F9725F7044b090b"
     *
     * ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image1.json
     * ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image2.json
     * ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image3.json
     * https://ipfs.io/ipfs/QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image4.json
     * https://ipfs.io/ipfs/QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image5.json
     * https://ipfs.io/ipfs/QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image6.json
     * @throws Exception
     */

    @Test
    public void createToken() throws Exception{
//        createToken("1660492800000", true, "ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image1.json");
//        createToken("1660492800000", true, "ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image2.json");
//        createToken("1660492800000", true, "ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image3.json");
//        createToken("1660492800000", false, "https://ipfs.io/ipfs/QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image4.json");
//        createToken("1660492800000", false, "https://ipfs.io/ipfs/QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image5.json");
//        createToken("1660492800000", false, "https://ipfs.io/ipfs/QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image6.json");

        createToken("1660492800000", true, "ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image1.json");
        createToken("1660492800000", true, "ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image2.json");
        createToken("1660492800000", true, "ipfs://QmNyx35qjHgHsTpdpgr5juQHC8aELp9UiZ1bEGbRu2TxiL/image3.json");
    }

    private void createToken(String term, boolean cipher, String tokenUrl) throws Exception{
        ERC721Template contract = load(address);
        TransactionReceipt transactionReceipt = contract.createToken(term, cipher,  tokenUrl ).send();
        List<ERC721Template.TransferEventResponse> transferEventResponseList = contract.getTransferEvents(transactionReceipt);
        transferEventResponseList.forEach(item ->{
            System.out.println("from = " + item.from + " to = " + item.to + " tokenId = " + item.tokenId );
        });
    }

    @Test
    public void tranf() throws Exception{
        ERC721Template contract = load(address);
        contract.transferFrom(credentials.getAddress(), Bech32.addressEncode("lat", "0x07c1FF4Ef5c94A9680977cDf85bDfb5a234a85d7"), BigInteger.valueOf(0)).send();
        System.out.println(contract.name().send());
        System.out.println(contract.symbol().send());
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
        BigInteger totalSupply = contract.totalSupply().send();
//
//        for (int i = 0; i < total.intValue(); i++) {
//            System.out.println(contract.getExtInfo(BigInteger.valueOf(i)).send());
//            System.out.println(contract.tokenURI(BigInteger.valueOf(i)).send());
//        }

        for (BigInteger i = BigInteger.ZERO; i.compareTo(totalSupply) < 0; i = i.add(BigInteger.ONE)) {
            System.out.println(contract.getExtInfo(i).send());
            System.out.println(contract.tokenURI(i).send());
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
