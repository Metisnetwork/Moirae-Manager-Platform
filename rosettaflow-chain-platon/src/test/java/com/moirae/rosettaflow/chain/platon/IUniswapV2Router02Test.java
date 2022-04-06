package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.DataTokenTemplate;
import com.moirae.rosettaflow.chain.platon.contract.evm.IUniswapV2Router02;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.crypto.Credentials;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class IUniswapV2Router02Test extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0x26d637e206cc39942628421e7b0d6fb41db0bc06");


    /**
     * WLAT at: 0xa0C63FAC4e5425F4721FF3258c3FA5B381152F73
     *
     * DipoleFactory at: 0xA44D7cdf71f53f2bcB4cb31618fbe532BD9A2d5c
     * DipoleRouter at: 0x26D637E206Cc39942628421e7B0D6Fb41dB0bC06
     */
    @Test
    public void balanceOf() throws Exception{
        IUniswapV2Router02 contract = load();

        System.out.println(contract.factory().send());
    }


    private IUniswapV2Router02 load(){
        return IUniswapV2Router02.load(address, web3j, credentials, gasProvider);
    }

}
