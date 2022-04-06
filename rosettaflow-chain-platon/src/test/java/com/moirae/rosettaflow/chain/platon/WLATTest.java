package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.WLAT;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import org.junit.jupiter.api.Test;

public class WLATTest extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0x4e0d36515990d73aa315aee28636378223deb43e");

    @Test
    public void deploy() throws Exception{
        WLAT contract = WLAT.deploy(web3j, credentials, gasProvider).send();
        System.out.println(contract.getContractAddress());
        System.out.println(Bech32.addressDecodeHex(contract.getContractAddress()));
        // lat1fcxnv52ejrtn4gc44m3gvd3hsg3aadp7j5pw74
        // 0x4e0d36515990d73aa315aee28636378223deb43e
    }

    private WLAT load(){
        return WLAT.load(address, web3j, credentials, gasProvider);
    }
}
