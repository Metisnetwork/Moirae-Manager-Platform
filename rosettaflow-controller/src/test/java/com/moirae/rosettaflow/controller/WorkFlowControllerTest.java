package com.moirae.rosettaflow.controller;

import com.alibaba.fastjson.JSONObject;
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

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
public class WorkFlowControllerTest {

    @Autowired
    private MockMvc mvc;

    private Credentials user1 = Credentials.create("68efa6466edaed4918f0b6c3b1b9667d37cad591482d672e8abcb4c5d1720f89");

    private String accessToken = "1649658496786C5F36DA324924472826DA92C352EC8C8";

    @Test
    public void createWorkflowOfWizardModeCase1() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 2010);
        req.put("calculationProcessId", 1);
        req.put("workflowName", "chendai-flow-1");
        req.put("workflowDesc", "chendai-desc-1");

        String result = mvc.perform(post("/workflow/wizard/createWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(req.toJSONString().getBytes())
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void createWorkflowOfWizardModeCase2() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 2010);
        req.put("calculationProcessId", 2);
        req.put("workflowName", "chendai-flow-2");
        req.put("workflowDesc", "chendai-desc-2");

        String result = mvc.perform(post("/workflow/wizard/createWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(req.toJSONString().getBytes())
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void createWorkflowOfWizardModeCase3() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 2010);
        req.put("calculationProcessId", 3);
        req.put("workflowName", "chendai-flow-3");
        req.put("workflowDesc", "chendai-desc-3");

        String result = mvc.perform(post("/workflow/wizard/createWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(req.toJSONString().getBytes())
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void createWorkflowOfWizardModeCase4() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 1001);
        req.put("calculationProcessId", 4);
        req.put("workflowName", "chendai-flow-4");
        req.put("workflowDesc", "chendai-desc-4");

        String result = mvc.perform(post("/workflow/wizard/createWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(req.toJSONString().getBytes())
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
}
