package com.moirae.rosettaflow.common.utils;

import cn.hutool.core.util.StrUtil;
import com.moirae.rosettaflow.common.utils.AddressChangeUtils;
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
     * @param address     hrp钱包地址
     * @return 是否成功标识
     */
    public static boolean verifyTypedDataV4(String jsonMessage, String signMsg, String address) throws IOException {
        StructuredDataEncoder dataEncoder = new StructuredDataEncoder(jsonMessage);
        Map<Integer, String> addresses = CryptoUtils.ecRecover(signMsg, dataEncoder.hashStructuredData());
        return addresses.toString().contains(AddressChangeUtils.convert0xAddress(address).toLowerCase());
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

//     public static void main(String[] args) {
//         String uuid = "ec1ab67261cd4a0387223c9952a3f455";
//         //登录签名
//         String json = "{\"domain\":{\"name\":\"Moirae\"},\"message\":{\"key\":\"{ec1ab67261cd4a0387223c9952a3f455}\",\"desc\":\"Welcome to Moirae!\"},\"primaryType\":\"Login\",\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"}],\"Login\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"desc\",\"type\":\"string\"}]}}";
//         //启动工作流签名
// //        String json = "{\"domain\":{\"name\":\"Moirae\"},\"message\":{\"address\":\"d6a151c0703d47e6baa068700e8c5381\"},\"primaryType\":\"sign\",\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"}],\"sign\":[{\"name\":\"key\",\"type\":\"string\"}]}}";

//         try {
//             json = StrUtil.format(json, uuid);
//             System.out.println("加密的json字符串为>>>" + json);
//             Credentials credentials = Credentials.create("f366d751f2b18ee39404e6be657dab7cd0e31da62ff7dbb49ddb8da15521e8ac");
//             System.out.println("钱包hrp地址>>>" + credentials.getAddress());
//             System.out.println("钱包0x地址>>>" + AddressChangeUtils.convert0xAddress(credentials.getAddress()));

//             System.out.println("签名结果>>>" + signTypedDataV4(json, credentials.getEcKeyPair()));

//             System.out.println("验证签名结果>>>" + verifyTypedDataV4(
//                     json,
//                     "0xfb71e85fb91026933d0057c263b549197e4d93e8a955728892f3ae9cd4cc6c4973c235ecc4dc2bf6927006b53f42bc3f56110649f040357489f41b3cd9c10af71c",
//                     "lax1j0q78v8g9l94pkwyk3tgk0vfy5ukdz3q2e7wqa"));

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
}
