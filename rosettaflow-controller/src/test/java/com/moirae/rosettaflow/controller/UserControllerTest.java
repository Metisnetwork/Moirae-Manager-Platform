package com.moirae.rosettaflow.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.common.utils.WalletSignUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    private Credentials user1 = Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f89");

    private String accessToken = "16488952050141F9C69601BB84C1CAFBB6F7F140F9CD6";

    @Test
    public void xxxx() throws Exception {
        for (int i = 0; i < 10; i++) {
            user1 = Credentials.create(Keys.createEcKeyPair());
            login(getNonce());
        }
    }

    @Test
    public void getLoginNonce() throws Exception {
        System.out.println("nonce = " + getNonce());
    }

    @Test
    public void login() throws Exception {
        System.out.println("token = " + login(getNonce()));
    }

    @Test
    public void logout() throws Exception {
        String result = mvc.perform(post("/user/logout")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result); //
    }

    @Test
    public void updateNickName() throws Exception {
        JSONObject req = new JSONObject();
        req.put("address", user1.getAddress());
        req.put("nickName", "flow1");
        String result = mvc.perform(post("/user/updateNickName")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(req.toJSONString().getBytes())
                        .header("Accept-Language","zh")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }


    private String getRawJson(String nonce){
        String json = "{\"domain\":{\"name\":\"Moirae\"},\"message\":{\"key\":\"{}\",\"desc\":\"Welcome to Moirae!\"},\"primaryType\":\"Login\",\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"}],\"Login\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"desc\",\"type\":\"string\"}]}}";
        return StrUtil.format(json, nonce);
    }

    private String login(String nonce) throws Exception{
        JSONObject req = new JSONObject();
        req.put("address", user1.getAddress());
        req.put("signMessage", getRawJson(nonce));
        req.put("sign", WalletSignUtils.signTypedDataV4(req.getString("signMessage"), user1.getEcKeyPair()));
        String result = mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(req.toJSONString().getBytes())
                        .header("Accept-Language","zh")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result); //
        return JSONObject.parseObject(result).getJSONObject("data").getString("token");
    }

    private String getNonce() throws Exception{
        String result = mvc.perform(get("/user/getLoginNonce")
                        .param("address", user1.getAddress())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
        return JSONObject.parseObject(result).getJSONObject("data").getString("nonce");
    }
}
