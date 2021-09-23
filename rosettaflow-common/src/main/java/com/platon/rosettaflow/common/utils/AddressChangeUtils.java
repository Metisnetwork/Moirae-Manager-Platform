package com.platon.rosettaflow.common.utils;

import cn.hutool.core.util.StrUtil;
import com.platone.sdk.utlis.Bech32;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author hudenian
 * @date 2021/9/2
 * @description
 */
public class AddressChangeUtils {

    public static final String HRP_ETH = "0x";

    /**
     * The Bech32 character set for encoding.
     */
    private static final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    /**
     * Find the polynomial with value coefficients mod the generator as 30-bit.
     */
    private static int polymod(final byte[] values) {
        int c = 1;
        for (byte v_i : values) {
            int c0 = (c >>> 25) & 0xff;
            c = ((c & 0x1ffffff) << 5) ^ (v_i & 0xff);
            if ((c0 & 1) != 0) {
                c ^= 0x3b6a57b2;
            }
            if ((c0 & 2) != 0) {
                c ^= 0x26508e6d;
            }
            if ((c0 & 4) != 0) {
                c ^= 0x1ea119fa;
            }
            if ((c0 & 8) != 0) {
                c ^= 0x3d4233dd;
            }
            if ((c0 & 16) != 0) {
                c ^= 0x2a1462b3;
            }
        }
        return c;
    }

    /**
     * Expand a HRP for use in checksum computation.
     */
    @SuppressWarnings("all")
    private static byte[] expandHrp(final String hrp) {
        int hrpLength = hrp.length();
        byte ret[] = new byte[hrpLength * 2 + 1];
        for (int i = 0; i < hrpLength; ++i) {
            int c = hrp.charAt(i) & 0x7f; // Limit to standard 7-bit ASCII
            ret[i] = (byte) ((c >>> 5) & 0x07);
            ret[i + hrpLength + 1] = (byte) (c & 0x1f);
        }
        ret[hrpLength] = 0;
        return ret;
    }

    /**
     * Create a checksum.
     */
    private static byte[] createChecksum(final String hrp, final byte[] values) {
        byte[] hrpExpanded = expandHrp(hrp);
        byte[] enc = new byte[hrpExpanded.length + values.length + 6];
        System.arraycopy(hrpExpanded, 0, enc, 0, hrpExpanded.length);
        System.arraycopy(values, 0, enc, hrpExpanded.length, values.length);
        int mod = polymod(enc) ^ 1;
        byte[] ret = new byte[6];
        for (int i = 0; i < 6; ++i) {
            ret[i] = (byte) ((mod >>> (5 * (5 - i))) & 31);
        }
        return ret;
    }

    /**
     * Encode a Bech32 string.
     */
    public static String encode(String hrp, final byte[] values) {
        hrp = hrp.toLowerCase(Locale.ROOT);
        byte[] checksum = createChecksum(hrp, values);
        byte[] combined = new byte[values.length + checksum.length];
        System.arraycopy(values, 0, combined, 0, values.length);
        System.arraycopy(checksum, 0, combined, values.length, checksum.length);
        StringBuilder sb = new StringBuilder(hrp.length() + 1 + combined.length);
        sb.append(hrp);
        sb.append('1');
        for (byte b : combined) {
            sb.append(CHARSET.charAt(b));
        }
        return sb.toString();
    }

    /**
     * Helper for re-arranging bits into groups.
     */
    public static byte[] convertBits(final byte[] in, final int fromBits,
                                     final int toBits, final boolean pad) {
        int acc = 0;
        int bits = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream(64);
        final int maxv = (1 << toBits) - 1;
        final int max_acc = (1 << (fromBits + toBits - 1)) - 1;
        for (byte b : in) {
            int value = b & 0xff;
            if ((value >>> fromBits) != 0) {
                throw new RuntimeException(
                        String.format("Input value '%X' exceeds '%d' bit size", value, fromBits));
            }
            acc = ((acc << fromBits) | value) & max_acc;
            bits += fromBits;
            while (bits >= toBits) {
                bits -= toBits;
                out.write((acc >>> bits) & maxv);
            }
        }
        if (pad) {
            if (bits > 0) {
                out.write((acc << (toBits - bits)) & maxv);
            }
        } else if (bits >= fromBits || ((acc << (toBits - bits)) & maxv) != 0) {
            throw new RuntimeException("Could not convert bits, invalid padding");
        }
        return out.toByteArray();
    }

    /**
     * hrp Conversion to 0x address
     *
     * @param hrpAddress hrpAddress
     * @return 0x address
     */
    public static String convert0XAddress(String hrpAddress) {
        if (!StrUtil.isNotBlank(hrpAddress)) {
            throw new RuntimeException("hrpAddress can not blank");
        }
        if (Numeric.containsHexPrefix(hrpAddress) && WalletUtils.isValidAddress(hrpAddress)) {
            return hrpAddress;
        }
        return "0x" + DataChangeUtils.bytesToHex(Bech32.addressDecode(hrpAddress));
    }

    /**
     * 合约前缀转换
     */
    public static void main(String[] args) {
        String addr = "0xb0EeA1eFd5F215278b420D21c0bF5Cd6451AA4c7";
        String latAddr = AddressChangeUtils.encode("lat", convertBits(Numeric.hexStringToByteArray(addr), 8, 5, true));
        System.out.println("lat地址为>>>" + latAddr);
        System.out.println(DataChangeUtils.bytesToHex(Bech32.addressDecode(latAddr)));
        System.out.println(convert0XAddress("atp1qpagtcerpdwed2c4ar3hc738m2h98ecrt04c2x"));

        List<String> addrList = new ArrayList<>();
        addrList.add("0x1000000000000000000000000000000000000001");
        addrList.add("0x1000000000000000000000000000000000000002");
        addrList.add("0x1000000000000000000000000000000000000003");
        addrList.add("0x1000000000000000000000000000000000000004");
        addrList.add("0x1000000000000000000000000000000000000005");
        addrList.add("0x0000000000000000000000000000000000000001");
        addrList.forEach(a -> System.out.println(a + "转换后的钱包地址>>>" + AddressChangeUtils.encode("lax", convertBits(Numeric.hexStringToByteArray(a), 8, 5, true))));
    }
}
