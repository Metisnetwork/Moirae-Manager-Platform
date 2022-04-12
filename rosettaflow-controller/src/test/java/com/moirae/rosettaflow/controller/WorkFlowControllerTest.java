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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


    /**
     * TRUNCATE TABLE `mo_workflow`;
     * TRUNCATE TABLE `mo_workflow_run_status`;
     * TRUNCATE TABLE `mo_workflow_run_task_result`;
     * TRUNCATE TABLE `mo_workflow_run_task_status`;
     * TRUNCATE TABLE `mo_workflow_setting_expert`;
     * TRUNCATE TABLE `mo_workflow_setting_wizard`;
     * TRUNCATE TABLE `mo_workflow_task`;
     * TRUNCATE TABLE `mo_workflow_task_code`;
     * TRUNCATE TABLE `mo_workflow_task_input`;
     * TRUNCATE TABLE `mo_workflow_task_output`;
     * TRUNCATE TABLE `mo_workflow_task_resource`;
     * TRUNCATE TABLE `mo_workflow_task_variable`;
     * TRUNCATE TABLE `mo_workflow_version`;
     *
     * @throws Exception
     */
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
    public void getWorkflowOfWizardModeCase1Step1() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(1L, 1L, 1));
    }

    @Test
    public void setWorkflowOfWizardModeCase1Step1() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 1,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\"step\": 1,\n" +
                "\t\t\"type\": 0\n" +
                "\t},\n" +
                "\t\"trainingInput\": {\n" +
                "\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\"isPsi\": true,\n" +
                "\t\t\"item\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\"dependentVariable\": 10,\n" +
                "\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\"keyColumn\": 10,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase1Step2() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(1L, 1L, 2));
    }

    @Test
    public void setWorkflowOfWizardModeCase1Step2() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 1,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\t\"step\": 2,\n" +
                "\t\t\t\"type\": 3\n" +
                "\t},\n" +
                "\t\"commonResource\": {\n" +
                "\t\t\"costMem\": 2048,\n" +
                "\t\t\"costCpu\": 2,\n" +
                "\t\t\"costGpu\": 4,\n" +
                "\t\t\"costBandwidth\": 6,\n" +
                "\t\t\"runTime\": 6\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase1Step3() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(1L, 1L, 3));
    }

    @Test
    public void setWorkflowOfWizardModeCase1Step3() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 1,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\t\"step\": 3,\n" +
                "\t\t\t\"type\": 5\n" +
                "\t},\n" +
                "\t\"commonOutput\": {\n" +
                "\t\t\"identityId\": [\"identity:403931f2e18c4be2915e229b9065a208\",\"identity:07c0119cb39e47f497ff581efd48e342\"],\n" +
                "\t\t\"storePattern\": 1\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
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
    public void getWorkflowOfWizardModeCase2Step1() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(2L, 1L, 1));
    }

    @Test
    public void setWorkflowOfWizardModeCase2Step1() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 2,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\"step\": 1,\n" +
                "\t\t\"type\": 1\n" +
                "\t},\n" +
                "\t\"predictionInput\": {\n" +
                "\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\"isPsi\": true,\n" +
                "\t\t\"inputModel\": true,\n" +
                "\t\t\"model\": {\n" +
                "\t\t\t\"metaDataId\": \"metadata:0x0e4693a97c213d057cbbb69ae18a217628d943776592996a87e59c594bd095a8\"\n" +
                "\t\t},\n" +
                "\t\t\"item\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\"keyColumn\": 10,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase2Step2() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(2L, 1L, 2));
    }

    @Test
    public void setWorkflowOfWizardModeCase2Step2() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 2,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\t\"step\": 2,\n" +
                "\t\t\t\"type\": 3\n" +
                "\t},\n" +
                "\t\"commonResource\": {\n" +
                "\t\t\"costMem\": 4096,\n" +
                "\t\t\"costCpu\": 4,\n" +
                "\t\t\"costGpu\": 8,\n" +
                "\t\t\"costBandwidth\": 12,\n" +
                "\t\t\"runTime\": 6\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase2Step3() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(2L, 1L, 3));
    }

    @Test
    public void setWorkflowOfWizardModeCase2Step3() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 2,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\t\"step\": 3,\n" +
                "\t\t\t\"type\": 5\n" +
                "\t},\n" +
                "\t\"commonOutput\": {\n" +
                "\t\t\"identityId\": [\"identity:403931f2e18c4be2915e229b9065a208\",\"identity:07c0119cb39e47f497ff581efd48e342\"],\n" +
                "\t\t\"storePattern\": 2\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
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
    public void getWorkflowOfWizardModeCase3Step1() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(3L, 1L, 1));
    }

    @Test
    public void setWorkflowOfWizardModeCase3Step1() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 3,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\"step\": 1,\n" +
                "\t\t\"type\": 0\n" +
                "\t},\n" +
                "\t\"trainingInput\": {\n" +
                "\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\"isPsi\": true,\n" +
                "\t\t\"item\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\"dependentVariable\": 10,\n" +
                "\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\"keyColumn\": 10,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase3Step2() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(3L, 1L, 2));
    }

    @Test
    public void setWorkflowOfWizardModeCase3Step2() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 3,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\"step\": 2,\n" +
                "\t\t\"type\": 1\n" +
                "\t},\n" +
                "\t\"predictionInput\": {\n" +
                "\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\"isPsi\": true,\n" +
                "\t\t\"inputModel\": true,\n" +
                "\t\t\"model\": {\n" +
                "\t\t\t\"metaDataId\": \"metadata:0x0e4693a97c213d057cbbb69ae18a217628d943776592996a87e59c594bd095a8\"\n" +
                "\t\t},\n" +
                "\t\t\"item\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\"keyColumn\": 10,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase3Step3() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(3L, 1L, 3));
    }

    @Test
    public void setWorkflowOfWizardModeCase3Step3() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 3,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\"step\": 3,\n" +
                "\t\t\"type\": 4\n" +
                "\t},\n" +
                "\t\"trainingAndPredictionResource\": {\n" +
                "\t\t\"training\": {\n" +
                "\t\t\t\"costMem\": 2048,\n" +
                "\t\t\t\"costCpu\": 3,\n" +
                "\t\t\t\"costGpu\": 3,\n" +
                "\t\t\t\"costBandwidth\": 6,\n" +
                "\t\t\t\"runTime\": 6\n" +
                "\t\t},\n" +
                "\t\t\"prediction\": {\n" +
                "\t\t\t\"costMem\": 2048,\n" +
                "\t\t\t\"costCpu\": 3,\n" +
                "\t\t\t\"costGpu\": 3,\n" +
                "\t\t\t\"costBandwidth\": 6,\n" +
                "\t\t\t\"runTime\": 6\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase3Step4() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(3L, 1L, 4));
    }

    @Test
    public void setWorkflowOfWizardModeCase3Step4() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 3,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\"step\": 4,\n" +
                "\t\t\"type\": 6\n" +
                "\t},\n" +
                "\t\"trainingAndPredictionOutput\": {\n" +
                "\t\t\"training\": {\n" +
                "\t\t\t\"identityId\": [\"identity:07c0119cb39e47f497ff581efd48e342-11\", \"identity:403931f2e18c4be2915e229b9065a208-12\"],\n" +
                "\t\t\t\"storePattern\": 1\n" +
                "\t\t},\n" +
                "\t\t\"prediction\": {\n" +
                "\t\t\t\"identityId\": [\"identity:07c0119cb39e47f497ff581efd48e342-21\", \"identity:403931f2e18c4be2915e229b9065a208-22\"],\n" +
                "\t\t\t\"storePattern\": 1\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
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

    @Test
    public void getWorkflowOfWizardModeCase4Step1() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(4L, 1L, 1));
    }

    @Test
    public void setWorkflowOfWizardModeCase4Step1() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 4,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\"step\": 1,\n" +
                "\t\t\"type\": 2\n" +
                "\t},\n" +
                "\t\"psiInput\": {\n" +
                "\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\"item\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250-1\",\n" +
                "\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07-2\",\n" +
                "\t\t\t\t\"keyColumn\": 10,\n" +
                "\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase4Step2() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(4L, 1L, 2));
    }

    @Test
    public void setWorkflowOfWizardModeCase4Step2() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 4,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\t\"step\": 2,\n" +
                "\t\t\t\"type\": 3\n" +
                "\t},\n" +
                "\t\"commonResource\": {\n" +
                "\t\t\"costMem\": 2048,\n" +
                "\t\t\"costCpu\": 2,\n" +
                "\t\t\"costGpu\": 4,\n" +
                "\t\t\"costBandwidth\": 6,\n" +
                "\t\t\"runTime\": 6\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void getWorkflowOfWizardModeCase4Step3() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(4L, 1L, 3));
    }

    @Test
    public void setWorkflowOfWizardModeCase4Step3() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 4\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"calculationProcessStep\": {\n" +
                "\t\t\t\"step\": 3,\n" +
                "\t\t\t\"type\": 5\n" +
                "\t},\n" +
                "\t\"commonOutput\": {\n" +
                "\t\t\"identityId\": [\"identity:403931f2e18c4be2915e229b9065a208-xx\",\"identity:07c0119cb39e47f497ff581efd48e342-xx\"],\n" +
                "\t\t\"storePattern\": 2\n" +
                "\t}\n" +
                "}";

        String result = mvc.perform(post("/workflow/wizard/settingWorkflowOfWizardMode")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }


    private String getWorkflowOfWizardMode(Long workflowId, Long workflowVersion, Integer step) throws Exception {
        String result = mvc.perform(get("/workflow/wizard/getWorkflowSettingOfWizardMode")
                        .param("workflowId", String.valueOf(workflowId))
                        .param("workflowVersion", String.valueOf(workflowVersion))
                        .param("step", String.valueOf(step))
                        .header("Accept-Language","zh")
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }
}
