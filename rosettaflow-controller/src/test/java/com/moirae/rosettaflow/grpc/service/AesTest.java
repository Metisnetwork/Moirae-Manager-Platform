package com.moirae.rosettaflow.grpc.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 功能描述
 */
public class AesTest {

    public static void main(String[] args) {
//        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
//        System.out.println(new String(key));
//        String keyStr = new String(key);
        String key = "hSnIi0XF4sYm/8MC";
        AES aes = SecureUtil.aes(key.getBytes());
        System.out.println(aes.encryptHex("hudenian"));
        System.out.println(aes.decryptStr("5eaeb6b5c0fdfbcef9d3fbaf1434dcb3"));
    }


}
