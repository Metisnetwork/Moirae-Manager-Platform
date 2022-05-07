package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.DataTokenTemplate;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import com.platon.crypto.Credentials;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class DataTokenTemplateTest extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0x355b39ad02068e7e0189b5df2df1818ad72dc64b");

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
        contract.transfer(AddressUtils.hexToBech32("0xD1B3D3CDc28A07FBb265ad6b0c3B77f65fE2FD49"), new BigInteger("5000000000000000000000")).send();
    }

    @Test
    public void approve() throws Exception{
        DataTokenTemplate contract = load(user);
        contract.approve(AddressUtils.hexToBech32("0x3979ca71ea6b4c0a7cf23a8bf216fd9fc37a4df9"), new BigInteger("100000000000000000000")).send();
    }

    @Test
    public void approveForDex() throws Exception{
        String tokenList = "0x355b39ad02068e7e0189b5df2df1818ad72dc64b\n" +
                "0xad716b2d1adb6d8a508326a7c2e328db8b154da0\n" +
                "0xe19cfd8f9173155c26149818abd5decaa6f705f3\n" +
                "0xe88695d3a3ba03ee6bb2130ffd7869a8e368a0b4";

        String[] tokenArray = tokenList.split("\n");

        for (String token: tokenArray) {
            DataTokenTemplate contract = load(AddressUtils.hexToBech32(token));
            contract.approve(AddressUtils.hexToBech32("0x3979ca71ea6b4c0a7cf23a8bf216fd9fc37a4df9"), new BigInteger("100000000000000000000")).send();
        }
    }

    @Test
    public void allowance() throws Exception{
        DataTokenTemplate contract = load();
        System.out.println(contract.allowance(AddressUtils.hexToBech32("0x6f852ba98639a001a315065ecaf2069c7479f4cc"),AddressUtils.hexToBech32("0xef5bad1b4bc03df3b6d62fe914e145126a5ff80d") ).send());
    }

    private DataTokenTemplate load(String address){
        return DataTokenTemplate.load(address, web3j, user, gasProvider);
    }

    private DataTokenTemplate load(){
        return DataTokenTemplate.load(address, web3j, credentials, gasProvider);
    }

    private DataTokenTemplate load(Credentials credentials){
        return DataTokenTemplate.load(address, web3j, credentials, gasProvider);
    }

}
