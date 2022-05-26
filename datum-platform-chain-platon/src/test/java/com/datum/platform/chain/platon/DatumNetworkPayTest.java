package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.DatumNetworkPay;
import com.datum.platform.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import org.junit.jupiter.api.Test;

public class DatumNetworkPayTest extends BaseContractTest {

    private String address = AddressUtils.hexToBech32("0x3979ca71ea6b4c0a7cf23a8bf216fd9fc37a4df9");

    @Test
    public void addWhitelist() throws Exception{
        DatumNetworkPay contract = load();
        TransactionReceipt transactionReceipt = contract.addWhitelist("lat1fgpxywxwaahhesxkqw77y9gtxrpmsyvj8l4fqs").send();
        System.out.println(transactionReceipt.getTransactionHash());
    }

    @Test
    public void deleteWhitelist() throws Exception{
        DatumNetworkPay contract = load();
        TransactionReceipt transactionReceipt = contract.deleteWhitelist("lat15nv9adgf5yljm3ccqrkk2tykpat3kfg25tr9g0").send();
        System.out.println(transactionReceipt.getTransactionHash());
    }

    @Test
    public void whitelist() throws Exception{
        DatumNetworkPay contract = load();
        System.out.println(contract.whitelist("lat1d7zjh2vx8xsqrgc4qe0v4usxn368naxvlpu70r").send());
    }

    @Test
    public void deploy() throws Exception{
        DatumNetworkPay contract = DatumNetworkPay.deploy(web3j, credentials, gasProvider).send();
        System.out.println(contract.getContractAddress());
        System.out.println(Bech32.addressDecodeHex(contract.getContractAddress()));
        // lat1r62d243ehjharw647xadnn5matu0d46f3t8v9y
        // 0x1e94d55639bcafd1bb55f1bad9ce9beaf8f6d749
    }

    private DatumNetworkPay load(){
        return DatumNetworkPay.load(address, web3j, user, gasProvider);
    }
}
