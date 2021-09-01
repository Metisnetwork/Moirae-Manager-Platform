package com.platon.rosettaflow.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.web3j.crypto.*;
import org.web3j.utils.Assertions;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;

/**
 * @author hudenian
 * @date 2021/8/10
 * @description 钱包签名验证签名工具类
 */
public class EthWalletSignUtils {
    /**
     * 签名
     *
     * @param message   签名消息
     * @param ecKeyPair 密钥对
     * @return 签名值
     */
    public static String sign(String message, ECKeyPair ecKeyPair) {
        Sign.SignatureData signatureData = signPrefixedMessage(message.getBytes(StandardCharsets.UTF_8), ecKeyPair);

        byte[] sigData = new byte[65];
        sigData[0] = signatureData.getV()[0];
        System.arraycopy(signatureData.getR(), 0, sigData, 1, 32);
        System.arraycopy(signatureData.getS(), 0, sigData, 33, 32);
        return new String(Base64.encodeBase64(sigData), StandardCharsets.UTF_8);
    }

    /**
     * byte数组转十六进制字符串
     *
     * @param bytes byte数组
     * @return 十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
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
            bigInteger = signedPrefixedMessageToKey(message.getBytes(StandardCharsets.UTF_8), signatureData);
        } catch (SignatureException e) {
            e.printStackTrace();
            return false;
        }
        String tmpAddress = Keys.getAddress(bigInteger);
        return Numeric.cleanHexPrefix(address).equalsIgnoreCase(Numeric.cleanHexPrefix(tmpAddress));
    }

    public static Sign.SignatureData signPrefixedMessage(byte[] message, ECKeyPair keyPair) {
        return Sign.signMessage(getEthereumMessageHash(message), keyPair, false);
    }

    static byte[] getEthereumMessageHash(byte[] message) {
        byte[] prefix = getEthereumMessagePrefix(message.length);
        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);
        return Hash.sha3(result);
    }

    static byte[] getEthereumMessagePrefix(int messageLength) {
        return "\u0019Ethereum Signed Message:\n".concat(String.valueOf(messageLength)).getBytes();
    }

    public static BigInteger signedPrefixedMessageToKey(byte[] message, Sign.SignatureData signatureData) throws SignatureException {
        return signedMessageHashToKey(getEthereumMessageHash(message), signatureData);
    }

    static BigInteger signedMessageHashToKey(byte[] messageHash, Sign.SignatureData signatureData) throws SignatureException {
        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        Assertions.verifyPrecondition(r != null && r.length == 32, "r must be 32 bytes");
        Assertions.verifyPrecondition(s != null && s.length == 32, "s must be 32 bytes");
        int header = signatureData.getV()[0] & 255;
        if (header >= 27 && header <= 34) {
            ECDSASignature sig = new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS()));
            int recId = header - 27;
            BigInteger key = Sign.recoverFromSignature(recId, sig, messageHash);
            if (key == null) {
                throw new SignatureException("Could not recover public key from signature");
            } else {
                return key;
            }
        } else {
            throw new SignatureException("Header byte out of range: " + header);
        }
    }

    public static void main(String[] args) {
        //匹配以太坊的验证签名
        try {
            Credentials credentials = Credentials.create("b4b282bf890abfc11e9b1832a2735f4c77fb3267978723034f84beb4e71fdf79");
            System.out.println(credentials.getAddress());

            byte[] hash = "hudenian".getBytes(StandardCharsets.UTF_8);
            Sign.SignatureData signature = signPrefixedMessage(hash, credentials.getEcKeyPair());
            String r = Numeric.toHexString(signature.getR());
            String s = Numeric.toHexString(signature.getS()).substring(2);
            String v = Numeric.toHexString(signature.getV()).substring(2);
            System.out.println(r + "    " + s + "    " + v);
            System.out.println(r + s + v);

            byte[] sigDatarsv = new byte[65];
            String signResult = "0x7299fcd8b7284c201c95ae378094752d44963809c263437a9fb1cd37ea0a6ea40055dfcdf3c62ed9518ef236e56edf3f60f806c664b5cc8d790b75aa4fa35c451b";

            byte[] vStr = DataChangeUtils.hexToByteArray(signResult.substring(130, 132));
            System.arraycopy(vStr, 0, sigDatarsv, 0, 1);

            byte[] rStr = DataChangeUtils.hexToByteArray(signResult.substring(0, 66).substring(2));
            System.arraycopy(rStr, 0, sigDatarsv, 1, 32);

            byte[] sStr = DataChangeUtils.hexToByteArray(signResult.substring(66, 130));
            System.arraycopy(sStr, 0, sigDatarsv, 33, 32);

            System.out.println("钱包地址签名前的内容为>>>" + new String(Base64.encodeBase64(sigDatarsv), StandardCharsets.UTF_8));

            String msg = "hudenian";
            String signStr = sign(msg, credentials.getEcKeyPair());
            System.out.println("钱包地址签名后的内容为>>>" + signStr);
            System.out.println(verifySign(msg, signStr, "0xb0eea1efd5f215278b420d21c0bf5cd6451aa4c7"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
