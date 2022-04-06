package com.moirae.rosettaflow.chain.platon.utils;

import com.platon.bech32.Bech32;
import com.platon.parameters.NetworkParameters;

public class AddressUtils {

    public static String hexToBech32(String hex){
        return Bech32.addressEncode(NetworkParameters.getHrp(), hex);
    }
}
