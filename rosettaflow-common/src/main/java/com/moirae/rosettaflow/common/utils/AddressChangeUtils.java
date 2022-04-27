package com.moirae.rosettaflow.common.utils;

import cn.hutool.core.util.StrUtil;
import com.platon.bech32.Bech32;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

/**
 * @author hudenian
 * @date 2021/9/2
 * @description
 */
public class AddressChangeUtils {

    /**
     * hrp Conversion to 0x address
     *
     * @param hrpAddress hrpAddress
     * @return 0x address
     */
    public static String convert0xAddress(String hrpAddress) {
        if (!StrUtil.isNotBlank(hrpAddress)) {
            throw new RuntimeException("hrpAddress can not blank");
        }
        if (Numeric.containsHexPrefix(hrpAddress) && WalletUtils.isValidAddress(hrpAddress)) {
            return hrpAddress;
        }
        return Numeric.toHexString(Bech32.addressDecode(hrpAddress));
    }
}
