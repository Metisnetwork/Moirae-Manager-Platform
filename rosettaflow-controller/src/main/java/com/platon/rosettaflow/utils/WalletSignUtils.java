package com.platon.rosettaflow.utils;

import cn.hutool.core.util.StrUtil;
import com.platone.sdk.utlis.Bech32;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import javax.xml.bind.DatatypeConverter;
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

    /**
     * 对数据进行eth_signTypedData_v4签名
     *
     * @param jsonMessage 签名数据结构体
     * @param ecKeyPair   密钥对
     * @return 签名字符串
     */
    public static String signTypedDataV4(String jsonMessage, ECKeyPair ecKeyPair) throws IOException {
        StrUtil.replace(jsonMessage, "\\\"", "\"");
        StructuredDataEncoder encoder = new StructuredDataEncoder(jsonMessage);
        byte[] hash = encoder.hashStructuredData();
        Sign.SignatureData signatureData = Sign.signMessage(hash, ecKeyPair, false);
        byte[] bytesValue = ArrayUtils.addAll(signatureData.getR(), signatureData.getS());
        bytesValue = ArrayUtils.addAll(bytesValue, signatureData.getV());
        return "0x" + DatatypeConverter.printHexBinary(bytesValue).toLowerCase();
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "    \"domain\": {\n" +
                "        \"name\": \"Moirae\"\n" +
                "    },\n" +
                "    \"message\": {\n" +
                "        \"key\": \"7D27BD561BE149578D9FDA89D2B10BFE\",\n" +
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
                "}";

        try {
            Credentials credentials = Credentials.create("567762b8a66385de7bfc6fd96f5de618da1389b6974638c995c5e94a861b922b");
            System.out.println(credentials.getAddress());
            System.out.println(Bech32.addressDecodeHex(credentials.getAddress()));


            System.out.println("签名结果>>>" + signTypedDataV4(json, credentials.getEcKeyPair()));

            System.out.println("验证签名结果>>>" + verifyTypedDataV4(
                    json,
                    "0xc960e6505f5ace1e43b44653db5a769ba98983130008b5240b7eaa92260e042d4ab6cce1a1d450a2f1d291bf2e1841ecd60328bdd7b0d5c8c22346a6f8a54c161b",
                    "0x93c1e3b0e82fcb50d9c4b4568b3d892539668a20"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
