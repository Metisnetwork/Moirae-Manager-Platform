package com.moirae.rosettaflow.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.common.utils.WalletSignUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
public class UserControllerTest extends BaseControllerTest {

    @Test
    public void getLoginNonce() throws Exception {
        System.out.println("result = " + getNonce());
    }

    @Test
    public void login() throws Exception {
        System.out.println("result = " + login(getNonce()));
    }

    @Test
    public void logout() throws Exception {
        JSONObject req = new JSONObject();
        System.out.println("result = "  + commonPostWithToken("/user/logout", req.toJSONString()));
    }

    @Test
    public void updateUserName() throws Exception {
        JSONObject req = new JSONObject();
        req.put("userName", "flow2");
        System.out.println("result = "  + commonPostWithToken("/user/updateUserName", req.toJSONString()));
    }

    private String login(String nonce) throws Exception{
        JSONObject req = new JSONObject();
        req.put("address", user.getAddress());
        req.put("signMessage", getLoginJson(nonce));
        req.put("sign", WalletSignUtils.signTypedDataV4(req.getString("signMessage"), user.getEcKeyPair()));
        return JSONObject.parseObject(commonPost("/user/login", req.toJSONString())).getJSONObject("data").getString("token");
    }

    private String getNonce() throws Exception{
        emptyParameters.add("address", user.getAddress());
        return JSONObject.parseObject(commonGet("/user/getLoginNonce", emptyParameters)).getJSONObject("data").getString("nonce");
    }

    private String getLoginJson(String nonce){
        String json = "{\"domain\":{\"name\":\"Moirae\"},\"message\":{\"key\":\"{}\",\"desc\":\"Welcome to Moirae!\"},\"primaryType\":\"Login\",\"types\":{\"EIP712Domain\":[{\"name\":\"name\",\"type\":\"string\"}],\"Login\":[{\"name\":\"key\",\"type\":\"string\"},{\"name\":\"desc\",\"type\":\"string\"}]}}";
        return StrUtil.format(json, nonce);
    }
}
