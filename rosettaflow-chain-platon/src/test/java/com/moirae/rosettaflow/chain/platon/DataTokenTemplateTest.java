package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.DataTokenTemplate;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class DataTokenTemplateTest extends BaseContractTest {

    @Test
    public void deploy() throws Exception{
        DataTokenTemplate contract = DataTokenTemplate.deploy(web3j, credentials, gasProvider, "TemplateName","TemplateSymbol", credentialsAddress, new BigInteger("500000000000000000000000000"), new BigInteger("100000000000000000000000000"), "dataId").send();
        System.out.println(contract.getContractAddress());
        // lat1ekf3l9j6awaccv894hqg2qcav3jntw0h0tgkxq
    }
}
