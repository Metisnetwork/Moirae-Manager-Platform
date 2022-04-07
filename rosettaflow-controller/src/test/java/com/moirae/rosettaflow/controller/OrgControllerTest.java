package com.moirae.rosettaflow.controller;

import com.alibaba.fastjson.JSONObject;
import com.moirae.rosettaflow.common.enums.OrgOrderByEnum;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
public class OrgControllerTest {

    @Autowired
    private MockMvc mvc;

    private String accessToken = "16488952050141F9C69601BB84C1CAFBB6F7F140F9CD6";

    @Test
    public void getOrgStats() throws Exception {
        mvc.perform(get("/org/getOrgStats")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getOrgList() throws Exception {
        Long current = 1L;
        Long size = 10L;
        // 搜索关键字(身份标识、组织名称关键字)
        String keyword = "";
        String orderBy = OrgOrderByEnum.NAME.name();

        mvc.perform(get("/org/getOrgList")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("current", String.valueOf(current))
                        .param("size", String.valueOf(size))
                        .param("keyword", keyword)
                        .param("orderBy", orderBy)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getOrgDetails() throws Exception {
        // 搜索关键字(身份标识、组织名称关键字)
        String identityId = "identity:07c0119cb39e47f497ff581efd48e342";
        mvc.perform(get("/org/getOrgDetails")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("identityId", identityId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getUserOrgList() throws Exception {
        mvc.perform(get("/org/getUserOrgList")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void joinOrg() throws Exception {
        JSONObject req = new JSONObject();
        req.put("identityIp", "192.168.10.154");
        req.put("identityPort", 10033);

        mvc.perform(post("/org/joinOrg")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .content(req.toJSONString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void quitOrg() throws Exception {
        JSONObject req = new JSONObject();
        req.put("identityId", "identity:403931f2e18c4be2915e229b9065a208");

        mvc.perform(post("/org/quitOrg")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .content(req.toJSONString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
