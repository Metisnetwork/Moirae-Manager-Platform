package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.DataTokenTemplate;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.crypto.Credentials;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class DataTokenTemplateTest extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0x7a50070c6bed66488c9a00c4668bdfa89cf6eb8e");

    @Test
    public void deploy() throws Exception{
        DataTokenTemplate contract = DataTokenTemplate.deploy(web3j, credentials, gasProvider, "TemplateName","TemplateSymbol", credentialsAddress, new BigInteger("500000000000000000000000000"), new BigInteger("100000000000000000000000000"), "dataId").send();
        System.out.println(contract.getContractAddress());
        // lat1ekf3l9j6awaccv894hqg2qcav3jntw0h0tgkxq
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
        contract.transfer(AddressUtils.hexToBech32("0x6f852ba98639a001a315065ecaf2069c7479f4cc"), new BigInteger("2000000000000000000000")).send();
    }

    @Test
    public void approve() throws Exception{
        DataTokenTemplate contract = load(Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f89"));
        contract.approve(AddressUtils.hexToBech32("0xef5bad1b4bc03df3b6d62fe914e145126a5ff80d"), new BigInteger("300000000000000000000")).send();
    }

    @Test
    public void allowance() throws Exception{
        DataTokenTemplate contract = load();
        System.out.println(contract.allowance(AddressUtils.hexToBech32("0x6f852ba98639a001a315065ecaf2069c7479f4cc"),AddressUtils.hexToBech32("0xef5bad1b4bc03df3b6d62fe914e145126a5ff80d") ).send());
    }


    private DataTokenTemplate load(){
        return DataTokenTemplate.load(address, web3j, credentials, gasProvider);
    }

    private DataTokenTemplate load(Credentials credentials){
        return DataTokenTemplate.load(address, web3j, credentials, gasProvider);
    }

}
