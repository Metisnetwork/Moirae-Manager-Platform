package com.datum.platform.chain.platon;

import com.datum.platform.chain.platon.contract.evm.DataTokenTemplate;
import com.datum.platform.chain.platon.contract.evm.ERC721Template;
import com.datum.platform.chain.platon.utils.AddressUtils;
import com.platon.bech32.Bech32;
import com.platon.crypto.Credentials;
import org.junit.jupiter.api.Test;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class ERC721TemplateTest extends BaseContractTest {

    private String address = "lat1x682p2y4ag5l4c90za209e7dm7tv2ylpqe555z"; //

    @Test
    public void balanceOf() throws Exception{
        ERC721Template contract = load(address);
        System.out.println(contract.supportsInterface(Numeric.hexStringToByteArray("0x01ffc9a7")).send());

//        System.out.println(contract.balanceOf(credentials.getAddress()).send());
    }


    private ERC721Template load(String address){
        return ERC721Template.load(address, web3j, user, gasProvider);
    }
}
