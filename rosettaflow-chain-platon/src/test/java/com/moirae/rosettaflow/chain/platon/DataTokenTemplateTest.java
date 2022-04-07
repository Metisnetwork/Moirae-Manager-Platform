package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.DataTokenTemplate;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import com.platon.crypto.Credentials;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class DataTokenTemplateTest extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0xfc4239a8dfe02c0d6db4a2b0c9ce8d11fd80b73c");

    @Test
    public void deploy() throws Exception{
        DataTokenTemplate contract = DataTokenTemplate.deploy(web3j, credentials, gasProvider, "TemplateName","TemplateSymbol", credentialsAddress, new BigInteger("500000000000000000000000000"), new BigInteger("100000000000000000000000000"), "dataId").send();
        System.out.println(contract.getContractAddress());
        System.out.println(Bech32.addressDecodeHex(contract.getContractAddress()));
        // lat1hhfwhezjwdlfx7tv7ss6a7g94r2sgpvpy53cmd
        // 0xbdd2ebe452737e93796cf421aef905a8d5040581
    }

    @Test
    public void balanceOf() throws Exception{
        DataTokenTemplate contract = load();
        System.out.println(contract.balanceOf(AddressUtils.hexToBech32("0x6f852ba98639a001a315065ecaf2069c7479f4cc")).send());
        System.out.println(contract.balanceOf(credentials.getAddress()).send());
    }

    @Test
    public void transfer() throws Exception{
        DataTokenTemplate contract = load();
        contract.transfer(AddressUtils.hexToBech32("0x6f852ba98639a001a315065ecaf2069c7479f4cc"), new BigInteger("5000000000000000000000")).send();
    }

    @Test
    public void approve() throws Exception{
        DataTokenTemplate contract = load(Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f89"));
        contract.approve(AddressUtils.hexToBech32("0x1e94d55639bcafd1bb55f1bad9ce9beaf8f6d749"), new BigInteger("20000000000000000000")).send();
    }

    @Test
    public void approveForDex() throws Exception{
        String tokenList = "0x3a3b85bfc6b7b8435d713dca5ce308b9c4abe430\n" +
                "0x282875fd9579367c44a8b2666d65f75f8c589fdb\n" +
                "0xfc23df226ca45f9a2c527d8313e27395964060fa\n" +
                "0xb74c8525dfb15598687c80be6615fb243c2eeb25\n" +
                "0x148d92fc65b04c60840793be11b9f1b558e6eb9e\n" +
                "0xbb54ec09ec1433fa48716effc94bbe9a950aecef\n" +
                "0x90d308f5e6ab2d087983ac4d5c8ced533ed7681a\n" +
                "0xdd4f9e77c206c64343140e25f5bf56dec6d81750\n" +
                "0xfc4239a8dfe02c0d6db4a2b0c9ce8d11fd80b73c\n" +
                "0x33f919a0f6312bfd225f797a4808683958660e3a\n" +
                "0xab94a2338ca24d05e2cc6318ecbd083eabbe473c\n" +
                "0x4ab735a85751534ce7d2bf733bc083232573fb06\n" +
                "0xe1b66c3ef7ca24e9b37b0ec38868fdb67881696f\n" +
                "0x1981e3ab9dd60eae70ae83d3d1b236c2662eae8a\n" +
                "0xb9ef5fd080839d3eb04809c0f69db709dd9b5f69\n" +
                "0xfa0bea7347b9b98e068528da5826cf45a9c6075e\n" +
                "0xcacc4ca37e0cd5d162455a753cf07bcdbc26281b\n" +
                "0x1e19040bae09c01d06e82ebd83c801308959daed\n" +
                "0xc91ef95ac29cfafce0b0b714051e5add99b64c87\n" +
                "0x8e801527c44929da704b7cd3406142f5391fe1f7\n" +
                "0x2fa1f9217fead87874ff63715c29af10d78f741e\n" +
                "0x4be1ae0984ddb224c863c1e20fec05c0b86c7d3d\n" +
                "0xa4f375aa5b51de1510709c8fdfbe20cc4aa8790c\n" +
                "0xb861eea15a711c19574dde3c582278e4e06a6482\n" +
                "0x2d0602fd33ff61e24a886060c13a3c619aa6b61e\n" +
                "0x15950be327f74885f64ec3c5f0367ea643a4a6c9";

        String[] tokenArray = tokenList.split("\n");

        for (String token: tokenArray) {
            DataTokenTemplate contract = load(AddressUtils.hexToBech32(token));
            contract.approve(AddressUtils.hexToBech32("0x26d637e206cc39942628421e7b0d6fb41db0bc06"), new BigInteger("10000000000000000000")).send();
        }
    }

    @Test
    public void allowance() throws Exception{
        DataTokenTemplate contract = load();
        System.out.println(contract.allowance(AddressUtils.hexToBech32("0x6f852ba98639a001a315065ecaf2069c7479f4cc"),AddressUtils.hexToBech32("0xef5bad1b4bc03df3b6d62fe914e145126a5ff80d") ).send());
    }

    private DataTokenTemplate load(String address){
        return DataTokenTemplate.load(address, web3j, credentials, gasProvider);
    }

    private DataTokenTemplate load(){
        return DataTokenTemplate.load(address, web3j, credentials, gasProvider);
    }

    private DataTokenTemplate load(Credentials credentials){
        return DataTokenTemplate.load(address, web3j, credentials, gasProvider);
    }

}
