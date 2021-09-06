package com.platon.rosettaflow.utils;

import cn.hutool.core.util.StrUtil;
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
        String uuid = "f00bda43c8e841389f38943f2b667e25";
        String json = "{\n" +
                "    \"domain\": {\n" +
                "        \"name\": \"Moirae\"\n" +
                "    },\n" +
                "    \"message\": {\n" +
                "        \"key\": \"{}\",\n" +
                "        \"desc\": \"Welcome to Moirae!\"\n" +
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
            json = StrUtil.format(json, uuid);
            System.out.println("加密的json字符串为>>>" + json);
            Credentials credentials = Credentials.create("567762b8a66385de7bfc6fd96f5de618da1389b6974638c995c5e94a861b922b");
            System.out.println(credentials.getAddress());

            System.out.println("签名结果>>>" + signTypedDataV4(json, credentials.getEcKeyPair()));

            System.out.println("验证签名结果>>>" + verifyTypedDataV4(
                    json,
                    "0x78555f07148ba6fba51412e8a6cf44fd58a0fa643ee0605c2c229691f12bed2e3eda3034cb3af4f79e17f21f4757c4dd616a4c3814a4bfd20cd8db39c4baad0f1b",
                    "0x93c1e3b0e82fcb50d9c4b4568b3d892539668a20"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
