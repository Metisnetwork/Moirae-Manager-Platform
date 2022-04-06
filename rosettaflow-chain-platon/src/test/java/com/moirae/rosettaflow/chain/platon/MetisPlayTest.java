package com.moirae.rosettaflow.chain.platon;

import com.moirae.rosettaflow.chain.platon.contract.evm.DataTokenFactory;
import com.moirae.rosettaflow.chain.platon.contract.evm.MetisPay;
import com.moirae.rosettaflow.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import org.junit.jupiter.api.Test;

public class MetisPlayTest extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0x1e94d55639bcafd1bb55f1bad9ce9beaf8f6d749");

    @Test
    public void deploy() throws Exception{
        MetisPay contract = MetisPay.deploy(web3j, credentials, gasProvider, "lat1fcxnv52ejrtn4gc44m3gvd3hsg3aadp7j5pw74").send();
        System.out.println(contract.getContractAddress());
        System.out.println(Bech32.addressDecodeHex(contract.getContractAddress()));
        // lat1r62d243ehjharw647xadnn5matu0d46f3t8v9y
        // 0x1e94d55639bcafd1bb55f1bad9ce9beaf8f6d749
    }

    private DataTokenFactory load(){
        return DataTokenFactory.load(address, web3j, credentials, gasProvider);
    }
}
