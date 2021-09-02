package com.platon.rosettaflow.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.StructuredDataEncoder;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 钱包签名验证签名工具类
 */
public class WalletSignUtils {
    /**
     * 签名
     *
     * @param message   签名消息
     * @param ecKeyPair 密钥对
     * @return 签名值
     */
    public static String sign(String message, ECKeyPair ecKeyPair) {
        Sign.SignatureData signatureData = Sign.signMessage(message.getBytes(StandardCharsets.UTF_8), ecKeyPair);

        byte[] sigData = new byte[65];
        sigData[0] = signatureData.getV()[0];
        System.arraycopy(signatureData.getR(), 0, sigData, 1, 32);
        System.arraycopy(signatureData.getS(), 0, sigData, 33, 32);
        return new String(Base64.encodeBase64(sigData), StandardCharsets.UTF_8);
    }

    /**
     * 验签
     *
     * @param message 签名明文
     * @param signMsg 签名信息
     * @param address 签名人地址
     * @return 验证成功失败标识
     */
    public static boolean verifySign(String message, String signMsg, String address) {
        byte[] bs = Base64.decodeBase64(signMsg);
        Sign.SignatureData signatureData = new Sign.SignatureData(bs[0], Arrays.copyOfRange(bs, 1, 33), Arrays.copyOfRange(bs, 33, 65));

        BigInteger bigInteger;
        try {
            bigInteger = Sign.signedMessageToKey(message.getBytes(StandardCharsets.UTF_8), signatureData);
        } catch (SignatureException e) {
            e.printStackTrace();
            return false;
        }
        String tmpAddress = Keys.getAddress(bigInteger);
        return Numeric.cleanHexPrefix(address).equals(Numeric.cleanHexPrefix(tmpAddress));
    }

    /**
     * 验证Sign Typed Data v4 的签名
     *
     * @param jsonMessage json格式签名明文
     * @param signMsg     签名数据
     * @param address     钱包地址
     * @return 是否成功标识
     */
    public static boolean verifyTypedDataV4(String jsonMessage, String signMsg, String address) throws IOException {
        StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessage);
        Map<Integer, String> addresses = CryptoUtils.ecrecover(signMsg, dataEncoder.hashStructuredData());
        return addresses.toString().contains(address.toLowerCase());
    }

    public static void main(String[] args) {
        try {
            System.out.println("验证签名结果>>>" + verifyTypedDataV4("{\n" +
                            "    \"domain\": {\n" +
                            "        \"name\": \"Moirae\"\n" +
                            "    },\n" +
                            "    \"message\": {\n" +
                            "        \"key\": \"uuid\",\n" +
                            "        \"desc\": \"Login to Moirae\"\n" +
                            "    },\n" +
                            "    \"primaryType\": \"Login\",\n" +
                            "    \"types\": {\n" +
                            "        \"EIP712Domain\": [\n" +
                            "            {\n" +
                            "                \"name\": \"name\",\n" +
                            "                \"type\": \"string\"\n" +
                            "            }\n" +
                            "        ],\n" +
                            "        \"Login\": [\n" +
                            "            {\n" +
                            "                \"name\": \"key\",\n" +
                            "                \"type\": \"string\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "                \"name\": \"desc\",\n" +
                            "                \"type\": \"string\"\n" +
                            "            }\n" +
                            "        ]\n" +
                            "    }\n" +
                            "}",
                    "0xb231d71fd53950d6473373a0eeff7591810b20cf437208c26e3286cfefd03de625a653e9f73e01d67e60efb55ce477cb8f6754ae731a2e8652278f16fd3f2c741b",
                    "0x93c1e3b0e82fcb50d9c4b4568b3d892539668a20"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
