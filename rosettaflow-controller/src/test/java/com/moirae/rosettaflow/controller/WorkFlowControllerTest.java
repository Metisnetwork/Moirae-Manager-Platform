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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    private String lang = "zh";


    // ----------------------向导模式创建计算流程为训练的工作流----------------------------------------------
    @Test
    public void createWorkflowOfWizardModeCase1() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 2010);
        req.put("calculationProcessId", 1);
        req.put("workflowName", "chendai-flow-1");
        req.put("workflowDesc", "chendai-desc-1");

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/createWorkflowOfWizardMode", req.toJSONString()));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
    }

    // ----------------------向导模式创建计算流程为预测的工作流----------------------------------------------

    @Test
    public void createWorkflowOfWizardModeCase2() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 2010);
        req.put("calculationProcessId", 2);
        req.put("workflowName", "chendai-flow-2");
        req.put("workflowDesc", "chendai-desc-2");

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/createWorkflowOfWizardMode", req.toJSONString()));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
    }

    // ----------------------向导模式创建计算流程为训练，并预测的工作流----------------------------------------------

    @Test
    public void createWorkflowOfWizardModeCase3() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 2010);
        req.put("calculationProcessId", 3);
        req.put("workflowName", "chendai-flow-3");
        req.put("workflowDesc", "chendai-desc-3");

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/createWorkflowOfWizardMode", req.toJSONString()));
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
        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));

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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
    }

    // ----------------------向导模式创建计算流程为PSI的工作流----------------------------------------------
    @Test
    public void createWorkflowOfWizardModeCase4() throws Exception {
        JSONObject req = new JSONObject();
        req.put("algorithmId", 1001);
        req.put("calculationProcessId", 4);
        req.put("workflowName", "chendai-flow-4");
        req.put("workflowDesc", "chendai-desc-4");

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/createWorkflowOfWizardMode", req.toJSONString()));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
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

        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
    }

    @Test
    public void getWorkflowOfWizardModeCase4Step3() throws Exception {
        System.out.println("result = " + getWorkflowOfWizardMode(4L, 1L, 3));
    }

    @Test
    public void setWorkflowOfWizardModeCase4Step3() throws Exception {
        String json = "{\n" +
                "\t\"workflowId\": 4,\n" +
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
        System.out.println("result = " + commonPostWithToken("/workflow/wizard/settingWorkflowOfWizardMode", json));
    }


    // ----------------------专家模式创建的单节点训练工作流----------------------------------------------
    @Test
    public void createWorkflowOfExpertModeCase1() throws Exception {
        // 创建
        String workflowName = "chendai-flow-5";
        System.out.println("result = "  + createWorkflowOfExpertMode(workflowName));
//        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1}}

        // 查询
        Long workflowId = 5L;
        Long workflowVersion = 1L;
//        System.out.println("result = "  + getWorkflowSettingOfExpertMode(workflowId, workflowVersion));
//        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1,"workflowNodeList":[]}}

        // 设置
        String reqBody = getWorkflowSettingReqBodyOfExpertModeCase1(workflowId, workflowVersion);
        System.out.println("result = "  + settingWorkflowOfExpertMode(reqBody));
        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1}}


        // 查询
//        System.out.println("result = "  + getWorkflowSettingOfExpertMode(workflowId, workflowVersion));
//        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1,"workflowNodeList":[{"nodeStep":1,"nodeName":"隐私线性回归训练","algorithmId":2011,"nodeInput":{"identityId":"identity:e9eef460ea9c473993c6477915106eed","dataInputList":[{"identityId":"identity:3ddb63047d214ddd8187438a82841250","metaDataId":"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6","keyColumn":1,"dependentVariable":0,"dataColumnIds":"2,3,4"},{"identityId":"identity:f614f8ac21b44fe89926ad4f26ef5b07","metaDataId":"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044","keyColumn":1,"dependentVariable":10,"dataColumnIds":"12,13,14"}],"inputModel":false,"model":null,"isPsi":true},"nodeOutput":{"identityId":["identity:07c0119cb39e47f497ff581efd48e342","identity:403931f2e18c4be2915e229b9065a208"],"storePattern":1},"nodeCode":{"variableList":[{"varKey":"batch_size","varValue":"256","varDesc":"111111111111111","varDescEn":null},{"varKey":"epochs","varValue":"10","varDesc":null,"varDescEn":null},{"varKey":"learning_rate","varValue":"0.1","varDesc":null,"varDescEn":null},{"varKey":"predict_threshold","varValue":"0.5","varDesc":null,"varDescEn":null},{"varKey":"use_validation_set","varValue":"true","varDesc":null,"varDescEn":null},{"varKey":"validation_set_rate","varValue":"0.2","varDesc":null,"varDescEn":null}],"code":{"editType":2,"calculateContractCode":"# coding:utf-8\n\nimport os\nimport sys\nimport math\nimport json\nimport time\nimport logging\nimport shutil\nimport numpy as np\nimport pandas as pd\nimport tensorflow as tf\nimport latticex.rosetta as rtt\nimport channel_sdk\n\n\nnp.set_printoptions(suppress=True)\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\nos.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\nlog = logging.getLogger(__name__)\n\nclass PrivacyLinearRegTrain(object):\n    '''\n    Privacy linear regression train base on rosetta.\n    '''\n\n    def __init__(self,\n                 channel_config: str,\n                 cfg_dict: dict,\n                 data_party: list,\n                 result_party: list,\n                 results_dir: str):\n        log.info(f\"channel_config:{channel_config}\")\n        log.info(f\"cfg_dict:{cfg_dict}\")\n        log.info(f\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\")\n        assert isinstance(channel_config, str), \"type of channel_config must be str\"\n        assert isinstance(cfg_dict, dict), \"type of cfg_dict must be dict\"\n        assert isinstance(data_party, (list, tuple)), \"type of data_party must be list or tuple\"\n        assert isinstance(result_party, (list, tuple)), \"type of result_party must be list or tuple\"\n        assert isinstance(results_dir, str), \"type of results_dir must be str\"\n        \n        self.channel_config = channel_config\n        self.data_party = list(data_party)\n        self.result_party = list(result_party)\n        self.party_id = cfg_dict[\"party_id\"]\n        self.input_file = cfg_dict[\"data_party\"].get(\"input_file\")\n        self.key_column = cfg_dict[\"data_party\"].get(\"key_column\")\n        self.selected_columns = cfg_dict[\"data_party\"].get(\"selected_columns\")\n\n        dynamic_parameter = cfg_dict[\"dynamic_parameter\"]\n        self.label_owner = dynamic_parameter.get(\"label_owner\")\n        if self.party_id == self.label_owner:\n            self.label_column = dynamic_parameter.get(\"label_column\")\n            self.data_with_label = True\n        else:\n            self.label_column = \"\"\n            self.data_with_label = False\n                        \n        algorithm_parameter = dynamic_parameter[\"algorithm_parameter\"]\n        self.epochs = algorithm_parameter.get(\"epochs\", 10)\n        self.batch_size = algorithm_parameter.get(\"batch_size\", 256)\n        self.learning_rate = algorithm_parameter.get(\"learning_rate\", 0.001)\n        self.use_validation_set = algorithm_parameter.get(\"use_validation_set\", True)\n        self.validation_set_rate = algorithm_parameter.get(\"validation_set_rate\", 0.2)\n        self.predict_threshold = algorithm_parameter.get(\"predict_threshold\", 0.5)\n\n        self.output_file = os.path.join(results_dir, \"model\")\n        \n        self.check_parameters()\n\n    def check_parameters(self):\n        log.info(f\"check parameter start.\")        \n        assert self.epochs > 0, \"epochs must be greater 0\"\n        assert self.batch_size > 0, \"batch size must be greater 0\"\n        assert self.learning_rate > 0, \"learning rate must be greater 0\"\n        assert 0 < self.validation_set_rate < 1, \"validattion set rate must be between (0,1)\"\n        assert 0 <= self.predict_threshold <= 1, \"predict threshold must be between [0,1]\"\n        \n        if self.input_file:\n            self.input_file = self.input_file.strip()\n        if self.party_id in self.data_party:\n            if os.path.exists(self.input_file):\n                input_columns = pd.read_csv(self.input_file, nrows=0)\n                input_columns = list(input_columns.columns)\n                if self.key_column:\n                    assert self.key_column in input_columns, f\"key_column:{self.key_column} not in input_file\"\n                if self.selected_columns:\n                    error_col = []\n                    for col in self.selected_columns:\n                        if col not in input_columns:\n                            error_col.append(col)   \n                    assert not error_col, f\"selected_columns:{error_col} not in input_file\"\n                if self.label_column:\n                    assert self.label_column in input_columns, f\"label_column:{self.label_column} not in input_file\"\n            else:\n                raise Exception(f\"input_file is not exist. input_file={self.input_file}\")\n        log.info(f\"check parameter finish.\")\n                        \n        \n    def train(self):\n        '''\n        Linear regression training algorithm implementation function\n        '''\n\n        log.info(\"extract feature or label.\")\n        train_x, train_y, val_x, val_y = self.extract_feature_or_label(with_label=self.data_with_label)\n        \n        log.info(\"start create and set channel.\")\n        self.create_set_channel()\n        log.info(\"waiting other party connect...\")\n        rtt.activate(\"SecureNN\")\n        log.info(\"protocol has been activated.\")\n        \n        log.info(f\"start set save model. save to party: {self.result_party}\")\n        rtt.set_saver_model(False, plain_model=self.result_party)\n        # sharing data\n        log.info(f\"start sharing train data. data_owner={self.data_party}, label_owner={self.label_owner}\")\n        shard_x, shard_y = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(train_x, train_y, header=0)\n        log.info(\"finish sharing train data.\")\n        column_total_num = shard_x.shape[1]\n        log.info(f\"column_total_num = {column_total_num}.\")\n        \n        if self.use_validation_set:\n            log.info(\"start sharing validation data.\")\n            shard_x_val, shard_y_val = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(val_x, val_y, header=0)\n            log.info(\"finish sharing validation data.\")\n\n        if self.party_id not in self.data_party:  \n            # mean the compute party and result party\n            log.info(\"compute start.\")\n            X = tf.placeholder(tf.float64, [None, column_total_num])\n            Y = tf.placeholder(tf.float64, [None, 1])\n            W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\n            b = tf.Variable(tf.zeros([1], dtype=tf.float64))\n            pred_Y = tf.matmul(X, W) + b\n            loss = tf.square(Y - pred_Y)\n            loss = tf.reduce_mean(loss)\n            # optimizer\n            optimizer = tf.train.GradientDescentOptimizer(self.learning_rate).minimize(loss)\n            init = tf.global_variables_initializer()\n            saver = tf.train.Saver(var_list=None, max_to_keep=5, name='v2')\n            \n            reveal_Y = rtt.SecureReveal(pred_Y)\n            actual_Y = tf.placeholder(tf.float64, [None, 1])\n            reveal_Y_actual = rtt.SecureReveal(actual_Y)\n\n            with tf.Session() as sess:\n                log.info(\"session init.\")\n                sess.run(init)\n                # train\n                log.info(\"train start.\")\n                train_start_time = time.time()\n                batch_num = math.ceil(len(shard_x) / self.batch_size)\n                for e in range(self.epochs):\n                    for i in range(batch_num):\n                        bX = shard_x[(i * self.batch_size): (i + 1) * self.batch_size]\n                        bY = shard_y[(i * self.batch_size): (i + 1) * self.batch_size]\n                        sess.run(optimizer, feed_dict={X: bX, Y: bY})\n                        if (i % 50 == 0) or (i == batch_num - 1):\n                            log.info(f\"epoch:{e + 1}/{self.epochs}, batch:{i + 1}/{batch_num}\")\n                log.info(f\"model save to: {self.output_file}\")\n                saver.save(sess, self.output_file)\n                train_use_time = round(time.time()-train_start_time, 3)\n                log.info(f\"save model success. train_use_time={train_use_time}s\")\n                \n                if self.use_validation_set:\n                    Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x_val})\n                    log.info(f\"Y_pred:\\n {Y_pred[:10]}\")\n                    Y_actual = sess.run(reveal_Y_actual, feed_dict={actual_Y: shard_y_val})\n                    log.info(f\"Y_actual:\\n {Y_actual[:10]}\")\n        \n            running_stats = str(rtt.get_perf_stats(True)).replace('\\n', '').replace(' ', '')\n            log.info(f\"running stats: {running_stats}\")\n        else:\n            log.info(\"computing, please waiting for compute finish...\")\n        rtt.deactivate()\n     \n        log.info(\"remove temp dir.\")\n        if self.party_id in (self.data_party + self.result_party):\n            # self.remove_temp_dir()\n            pass\n        else:\n            # delete the model in the compute party.\n            self.remove_output_dir()\n        \n        if (self.party_id in self.result_party) and self.use_validation_set:\n            log.info(\"result_party evaluate model.\")\n            from sklearn.metrics import r2_score, mean_squared_error\n            Y_pred = Y_pred.astype(\"float\").reshape([-1, ])\n            Y_true = Y_actual.astype(\"float\").reshape([-1, ])\n            r2 = r2_score(Y_true, Y_pred)\n            rmse = np.sqrt(mean_squared_error(Y_true, Y_pred))\n            log.info(\"********************\")\n            log.info(f\"R Squared: {round(r2, 6)}\")\n            log.info(f\"RMSE: {round(rmse, 6)}\")\n            log.info(\"********************\")\n        log.info(\"train finish.\")\n    \n    def create_set_channel(self):\n        '''\n        create and set channel.\n        '''\n        io_channel = channel_sdk.grpc.APIManager()\n        log.info(\"start create channel\")\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\n        log.info(\"start set channel\")\n        rtt.set_channel(\"\", channel)\n        log.info(\"set channel success.\")\n    \n    def extract_feature_or_label(self, with_label: bool=False):\n        '''\n        Extract feature columns or label column from input file,\n        and then divide them into train set and validation set.\n        '''\n        train_x = \"\"\n        train_y = \"\"\n        val_x = \"\"\n        val_y = \"\"\n        temp_dir = self.get_temp_dir()\n        if self.party_id in self.data_party:\n            if self.input_file:\n                if with_label:\n                    usecols = self.selected_columns + [self.label_column]\n                else:\n                    usecols = self.selected_columns\n                \n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\"str\")\n                input_data = input_data[usecols]\n                # only if self.validation_set_rate==0, split_point==input_data.shape[0]\n                split_point = int(input_data.shape[0] * (1 - self.validation_set_rate))\n                assert split_point > 0, f\"train set is empty, because validation_set_rate:{self.validation_set_rate} is too big\"\n                \n                if with_label:\n                    y_data = input_data[self.label_column]\n                    train_y_data = y_data.iloc[:split_point]\n                    train_y = os.path.join(temp_dir, f\"train_y_{self.party_id}.csv\")\n                    train_y_data.to_csv(train_y, header=True, index=False)\n                    if self.use_validation_set:\n                        assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small\"\n                        val_y_data = y_data.iloc[split_point:]\n                        val_y = os.path.join(temp_dir, f\"val_y_{self.party_id}.csv\")\n                        val_y_data.to_csv(val_y, header=True, index=False)\n                    del input_data[self.label_column]\n                \n                x_data = input_data\n                train_x = os.path.join(temp_dir, f\"train_x_{self.party_id}.csv\")\n                x_data.iloc[:split_point].to_csv(train_x, header=True, index=False)\n                if self.use_validation_set:\n                    assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small.\"\n                    val_x = os.path.join(temp_dir, f\"val_x_{self.party_id}.csv\")\n                    x_data.iloc[split_point:].to_csv(val_x, header=True, index=False)\n            else:\n                raise Exception(f\"data_node {self.party_id} not have data. input_file:{self.input_file}\")\n        return train_x, train_y, val_x, val_y\n    \n    def get_temp_dir(self):\n        '''\n        Get the directory for temporarily saving files\n        '''\n        temp_dir = os.path.join(os.path.dirname(self.output_file), 'temp')\n        if not os.path.exists(temp_dir):\n            os.makedirs(temp_dir, exist_ok=True)\n        return temp_dir\n\n    def remove_temp_dir(self):\n        '''\n        Delete all files in the temporary directory, these files are some temporary data.\n        Only delete temp file.\n        '''\n        temp_dir = self.get_temp_dir()\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n    \n    def remove_output_dir(self):\n        '''\n        Delete all files in the temporary directory, these files are some temporary data.\n        This is used to delete all output files of the non-resulting party\n        '''\n        temp_dir = os.path.dirname(self.output_file)\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n\n\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\n    '''\n    This is the entrance to this module\n    '''\n    privacy_linear_reg = PrivacyLinearRegTrain(channel_config, cfg_dict, data_party, result_party, results_dir)\n    privacy_linear_reg.train()","dataSplitContractCode":""}},"resource":{"costMem":1073741824,"costCpu":1,"costGpu":2,"costBandwidth":3145728,"runTime":180000}}]}}

//        // 查询运行状态
//        System.out.println("result = "  + getWorkflowStatusOfExpertMode(workflowId, workflowVersion));

    }

    // ----------------------专家模式创建的单节点预测工作流----------------------------------------------
    @Test
    public void createWorkflowOfExpertModeCase2() throws Exception {
        // 创建
        String workflowName = "chendai-flow-6";
        System.out.println("result = "  + createWorkflowOfExpertMode(workflowName));
//        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1}}


        // 查询
        Long workflowId = 6L;
        Long workflowVersion = 1L;
//        System.out.println("result = "  + getWorkflowSettingOfExpertMode(workflowId, workflowVersion));
        //{"code":10000,"msg":"成功","data":{"workflowId":2,"workflowVersion":1,"workflowNodeList":[]}}

        // 设置
        String reqBody = getWorkflowSettingReqBodyOfExpertModeCase2(workflowId, workflowVersion);
//        System.out.println("result = "  + settingWorkflowOfExpertMode(reqBody));
        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1}}


        // 查询
//        System.out.println("result = "  + getWorkflowSettingOfExpertMode(workflowId, workflowVersion));
//        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1,"workflowNodeList":[{"nodeStep":1,"nodeName":"隐私线性回归训练","algorithmId":2011,"nodeInput":{"identityId":"identity:e9eef460ea9c473993c6477915106eed","dataInputList":[{"identityId":"identity:3ddb63047d214ddd8187438a82841250","metaDataId":"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6","keyColumn":1,"dependentVariable":0,"dataColumnIds":"2,3,4"},{"identityId":"identity:f614f8ac21b44fe89926ad4f26ef5b07","metaDataId":"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044","keyColumn":1,"dependentVariable":10,"dataColumnIds":"12,13,14"}],"inputModel":false,"model":null,"isPsi":true},"nodeOutput":{"identityId":["identity:07c0119cb39e47f497ff581efd48e342","identity:403931f2e18c4be2915e229b9065a208"],"storePattern":1},"nodeCode":{"variableList":[{"varKey":"batch_size","varValue":"256","varDesc":"111111111111111","varDescEn":null},{"varKey":"epochs","varValue":"10","varDesc":null,"varDescEn":null},{"varKey":"learning_rate","varValue":"0.1","varDesc":null,"varDescEn":null},{"varKey":"predict_threshold","varValue":"0.5","varDesc":null,"varDescEn":null},{"varKey":"use_validation_set","varValue":"true","varDesc":null,"varDescEn":null},{"varKey":"validation_set_rate","varValue":"0.2","varDesc":null,"varDescEn":null}],"code":{"editType":2,"calculateContractCode":"# coding:utf-8\n\nimport os\nimport sys\nimport math\nimport json\nimport time\nimport logging\nimport shutil\nimport numpy as np\nimport pandas as pd\nimport tensorflow as tf\nimport latticex.rosetta as rtt\nimport channel_sdk\n\n\nnp.set_printoptions(suppress=True)\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\nos.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\nlog = logging.getLogger(__name__)\n\nclass PrivacyLinearRegTrain(object):\n    '''\n    Privacy linear regression train base on rosetta.\n    '''\n\n    def __init__(self,\n                 channel_config: str,\n                 cfg_dict: dict,\n                 data_party: list,\n                 result_party: list,\n                 results_dir: str):\n        log.info(f\"channel_config:{channel_config}\")\n        log.info(f\"cfg_dict:{cfg_dict}\")\n        log.info(f\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\")\n        assert isinstance(channel_config, str), \"type of channel_config must be str\"\n        assert isinstance(cfg_dict, dict), \"type of cfg_dict must be dict\"\n        assert isinstance(data_party, (list, tuple)), \"type of data_party must be list or tuple\"\n        assert isinstance(result_party, (list, tuple)), \"type of result_party must be list or tuple\"\n        assert isinstance(results_dir, str), \"type of results_dir must be str\"\n        \n        self.channel_config = channel_config\n        self.data_party = list(data_party)\n        self.result_party = list(result_party)\n        self.party_id = cfg_dict[\"party_id\"]\n        self.input_file = cfg_dict[\"data_party\"].get(\"input_file\")\n        self.key_column = cfg_dict[\"data_party\"].get(\"key_column\")\n        self.selected_columns = cfg_dict[\"data_party\"].get(\"selected_columns\")\n\n        dynamic_parameter = cfg_dict[\"dynamic_parameter\"]\n        self.label_owner = dynamic_parameter.get(\"label_owner\")\n        if self.party_id == self.label_owner:\n            self.label_column = dynamic_parameter.get(\"label_column\")\n            self.data_with_label = True\n        else:\n            self.label_column = \"\"\n            self.data_with_label = False\n                        \n        algorithm_parameter = dynamic_parameter[\"algorithm_parameter\"]\n        self.epochs = algorithm_parameter.get(\"epochs\", 10)\n        self.batch_size = algorithm_parameter.get(\"batch_size\", 256)\n        self.learning_rate = algorithm_parameter.get(\"learning_rate\", 0.001)\n        self.use_validation_set = algorithm_parameter.get(\"use_validation_set\", True)\n        self.validation_set_rate = algorithm_parameter.get(\"validation_set_rate\", 0.2)\n        self.predict_threshold = algorithm_parameter.get(\"predict_threshold\", 0.5)\n\n        self.output_file = os.path.join(results_dir, \"model\")\n        \n        self.check_parameters()\n\n    def check_parameters(self):\n        log.info(f\"check parameter start.\")        \n        assert self.epochs > 0, \"epochs must be greater 0\"\n        assert self.batch_size > 0, \"batch size must be greater 0\"\n        assert self.learning_rate > 0, \"learning rate must be greater 0\"\n        assert 0 < self.validation_set_rate < 1, \"validattion set rate must be between (0,1)\"\n        assert 0 <= self.predict_threshold <= 1, \"predict threshold must be between [0,1]\"\n        \n        if self.input_file:\n            self.input_file = self.input_file.strip()\n        if self.party_id in self.data_party:\n            if os.path.exists(self.input_file):\n                input_columns = pd.read_csv(self.input_file, nrows=0)\n                input_columns = list(input_columns.columns)\n                if self.key_column:\n                    assert self.key_column in input_columns, f\"key_column:{self.key_column} not in input_file\"\n                if self.selected_columns:\n                    error_col = []\n                    for col in self.selected_columns:\n                        if col not in input_columns:\n                            error_col.append(col)   \n                    assert not error_col, f\"selected_columns:{error_col} not in input_file\"\n                if self.label_column:\n                    assert self.label_column in input_columns, f\"label_column:{self.label_column} not in input_file\"\n            else:\n                raise Exception(f\"input_file is not exist. input_file={self.input_file}\")\n        log.info(f\"check parameter finish.\")\n                        \n        \n    def train(self):\n        '''\n        Linear regression training algorithm implementation function\n        '''\n\n        log.info(\"extract feature or label.\")\n        train_x, train_y, val_x, val_y = self.extract_feature_or_label(with_label=self.data_with_label)\n        \n        log.info(\"start create and set channel.\")\n        self.create_set_channel()\n        log.info(\"waiting other party connect...\")\n        rtt.activate(\"SecureNN\")\n        log.info(\"protocol has been activated.\")\n        \n        log.info(f\"start set save model. save to party: {self.result_party}\")\n        rtt.set_saver_model(False, plain_model=self.result_party)\n        # sharing data\n        log.info(f\"start sharing train data. data_owner={self.data_party}, label_owner={self.label_owner}\")\n        shard_x, shard_y = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(train_x, train_y, header=0)\n        log.info(\"finish sharing train data.\")\n        column_total_num = shard_x.shape[1]\n        log.info(f\"column_total_num = {column_total_num}.\")\n        \n        if self.use_validation_set:\n            log.info(\"start sharing validation data.\")\n            shard_x_val, shard_y_val = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(val_x, val_y, header=0)\n            log.info(\"finish sharing validation data.\")\n\n        if self.party_id not in self.data_party:  \n            # mean the compute party and result party\n            log.info(\"compute start.\")\n            X = tf.placeholder(tf.float64, [None, column_total_num])\n            Y = tf.placeholder(tf.float64, [None, 1])\n            W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\n            b = tf.Variable(tf.zeros([1], dtype=tf.float64))\n            pred_Y = tf.matmul(X, W) + b\n            loss = tf.square(Y - pred_Y)\n            loss = tf.reduce_mean(loss)\n            # optimizer\n            optimizer = tf.train.GradientDescentOptimizer(self.learning_rate).minimize(loss)\n            init = tf.global_variables_initializer()\n            saver = tf.train.Saver(var_list=None, max_to_keep=5, name='v2')\n            \n            reveal_Y = rtt.SecureReveal(pred_Y)\n            actual_Y = tf.placeholder(tf.float64, [None, 1])\n            reveal_Y_actual = rtt.SecureReveal(actual_Y)\n\n            with tf.Session() as sess:\n                log.info(\"session init.\")\n                sess.run(init)\n                # train\n                log.info(\"train start.\")\n                train_start_time = time.time()\n                batch_num = math.ceil(len(shard_x) / self.batch_size)\n                for e in range(self.epochs):\n                    for i in range(batch_num):\n                        bX = shard_x[(i * self.batch_size): (i + 1) * self.batch_size]\n                        bY = shard_y[(i * self.batch_size): (i + 1) * self.batch_size]\n                        sess.run(optimizer, feed_dict={X: bX, Y: bY})\n                        if (i % 50 == 0) or (i == batch_num - 1):\n                            log.info(f\"epoch:{e + 1}/{self.epochs}, batch:{i + 1}/{batch_num}\")\n                log.info(f\"model save to: {self.output_file}\")\n                saver.save(sess, self.output_file)\n                train_use_time = round(time.time()-train_start_time, 3)\n                log.info(f\"save model success. train_use_time={train_use_time}s\")\n                \n                if self.use_validation_set:\n                    Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x_val})\n                    log.info(f\"Y_pred:\\n {Y_pred[:10]}\")\n                    Y_actual = sess.run(reveal_Y_actual, feed_dict={actual_Y: shard_y_val})\n                    log.info(f\"Y_actual:\\n {Y_actual[:10]}\")\n        \n            running_stats = str(rtt.get_perf_stats(True)).replace('\\n', '').replace(' ', '')\n            log.info(f\"running stats: {running_stats}\")\n        else:\n            log.info(\"computing, please waiting for compute finish...\")\n        rtt.deactivate()\n     \n        log.info(\"remove temp dir.\")\n        if self.party_id in (self.data_party + self.result_party):\n            # self.remove_temp_dir()\n            pass\n        else:\n            # delete the model in the compute party.\n            self.remove_output_dir()\n        \n        if (self.party_id in self.result_party) and self.use_validation_set:\n            log.info(\"result_party evaluate model.\")\n            from sklearn.metrics import r2_score, mean_squared_error\n            Y_pred = Y_pred.astype(\"float\").reshape([-1, ])\n            Y_true = Y_actual.astype(\"float\").reshape([-1, ])\n            r2 = r2_score(Y_true, Y_pred)\n            rmse = np.sqrt(mean_squared_error(Y_true, Y_pred))\n            log.info(\"********************\")\n            log.info(f\"R Squared: {round(r2, 6)}\")\n            log.info(f\"RMSE: {round(rmse, 6)}\")\n            log.info(\"********************\")\n        log.info(\"train finish.\")\n    \n    def create_set_channel(self):\n        '''\n        create and set channel.\n        '''\n        io_channel = channel_sdk.grpc.APIManager()\n        log.info(\"start create channel\")\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\n        log.info(\"start set channel\")\n        rtt.set_channel(\"\", channel)\n        log.info(\"set channel success.\")\n    \n    def extract_feature_or_label(self, with_label: bool=False):\n        '''\n        Extract feature columns or label column from input file,\n        and then divide them into train set and validation set.\n        '''\n        train_x = \"\"\n        train_y = \"\"\n        val_x = \"\"\n        val_y = \"\"\n        temp_dir = self.get_temp_dir()\n        if self.party_id in self.data_party:\n            if self.input_file:\n                if with_label:\n                    usecols = self.selected_columns + [self.label_column]\n                else:\n                    usecols = self.selected_columns\n                \n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\"str\")\n                input_data = input_data[usecols]\n                # only if self.validation_set_rate==0, split_point==input_data.shape[0]\n                split_point = int(input_data.shape[0] * (1 - self.validation_set_rate))\n                assert split_point > 0, f\"train set is empty, because validation_set_rate:{self.validation_set_rate} is too big\"\n                \n                if with_label:\n                    y_data = input_data[self.label_column]\n                    train_y_data = y_data.iloc[:split_point]\n                    train_y = os.path.join(temp_dir, f\"train_y_{self.party_id}.csv\")\n                    train_y_data.to_csv(train_y, header=True, index=False)\n                    if self.use_validation_set:\n                        assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small\"\n                        val_y_data = y_data.iloc[split_point:]\n                        val_y = os.path.join(temp_dir, f\"val_y_{self.party_id}.csv\")\n                        val_y_data.to_csv(val_y, header=True, index=False)\n                    del input_data[self.label_column]\n                \n                x_data = input_data\n                train_x = os.path.join(temp_dir, f\"train_x_{self.party_id}.csv\")\n                x_data.iloc[:split_point].to_csv(train_x, header=True, index=False)\n                if self.use_validation_set:\n                    assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small.\"\n                    val_x = os.path.join(temp_dir, f\"val_x_{self.party_id}.csv\")\n                    x_data.iloc[split_point:].to_csv(val_x, header=True, index=False)\n            else:\n                raise Exception(f\"data_node {self.party_id} not have data. input_file:{self.input_file}\")\n        return train_x, train_y, val_x, val_y\n    \n    def get_temp_dir(self):\n        '''\n        Get the directory for temporarily saving files\n        '''\n        temp_dir = os.path.join(os.path.dirname(self.output_file), 'temp')\n        if not os.path.exists(temp_dir):\n            os.makedirs(temp_dir, exist_ok=True)\n        return temp_dir\n\n    def remove_temp_dir(self):\n        '''\n        Delete all files in the temporary directory, these files are some temporary data.\n        Only delete temp file.\n        '''\n        temp_dir = self.get_temp_dir()\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n    \n    def remove_output_dir(self):\n        '''\n        Delete all files in the temporary directory, these files are some temporary data.\n        This is used to delete all output files of the non-resulting party\n        '''\n        temp_dir = os.path.dirname(self.output_file)\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n\n\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\n    '''\n    This is the entrance to this module\n    '''\n    privacy_linear_reg = PrivacyLinearRegTrain(channel_config, cfg_dict, data_party, result_party, results_dir)\n    privacy_linear_reg.train()","dataSplitContractCode":""}},"resource":{"costMem":1073741824,"costCpu":1,"costGpu":2,"costBandwidth":3145728,"runTime":180000}}]}}
    }


    // ----------------------专家模式创建的单节点预测工作流----------------------------------------------
    @Test
    public void createWorkflowOfExpertModeCase3() throws Exception {
        // 创建
        String workflowName = "chendai-flow-7";
        System.out.println("result = "  + createWorkflowOfExpertMode(workflowName));
//        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1}}

        // 查询
        Long workflowId = 7L;
        Long workflowVersion = 1L;
//        System.out.println("result = "  + getWorkflowSettingOfExpertMode(workflowId, workflowVersion));
        //{"code":10000,"msg":"成功","data":{"workflowId":2,"workflowVersion":1,"workflowNodeList":[]}}

        // 设置
        String reqBody = getWorkflowSettingReqBodyOfExpertModeCase3(workflowId, workflowVersion);
        System.out.println("result = "  + settingWorkflowOfExpertMode(reqBody));
        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1}}


        // 查询
//        System.out.println("result = "  + getWorkflowSettingOfExpertMode(workflowId, workflowVersion));
//        //{"code":10000,"msg":"成功","data":{"workflowId":1,"workflowVersion":1,"workflowNodeList":[{"nodeStep":1,"nodeName":"隐私线性回归训练","algorithmId":2011,"nodeInput":{"identityId":"identity:e9eef460ea9c473993c6477915106eed","dataInputList":[{"identityId":"identity:3ddb63047d214ddd8187438a82841250","metaDataId":"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6","keyColumn":1,"dependentVariable":0,"dataColumnIds":"2,3,4"},{"identityId":"identity:f614f8ac21b44fe89926ad4f26ef5b07","metaDataId":"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044","keyColumn":1,"dependentVariable":10,"dataColumnIds":"12,13,14"}],"inputModel":false,"model":null,"isPsi":true},"nodeOutput":{"identityId":["identity:07c0119cb39e47f497ff581efd48e342","identity:403931f2e18c4be2915e229b9065a208"],"storePattern":1},"nodeCode":{"variableList":[{"varKey":"batch_size","varValue":"256","varDesc":"111111111111111","varDescEn":null},{"varKey":"epochs","varValue":"10","varDesc":null,"varDescEn":null},{"varKey":"learning_rate","varValue":"0.1","varDesc":null,"varDescEn":null},{"varKey":"predict_threshold","varValue":"0.5","varDesc":null,"varDescEn":null},{"varKey":"use_validation_set","varValue":"true","varDesc":null,"varDescEn":null},{"varKey":"validation_set_rate","varValue":"0.2","varDesc":null,"varDescEn":null}],"code":{"editType":2,"calculateContractCode":"# coding:utf-8\n\nimport os\nimport sys\nimport math\nimport json\nimport time\nimport logging\nimport shutil\nimport numpy as np\nimport pandas as pd\nimport tensorflow as tf\nimport latticex.rosetta as rtt\nimport channel_sdk\n\n\nnp.set_printoptions(suppress=True)\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\nos.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\nlog = logging.getLogger(__name__)\n\nclass PrivacyLinearRegTrain(object):\n    '''\n    Privacy linear regression train base on rosetta.\n    '''\n\n    def __init__(self,\n                 channel_config: str,\n                 cfg_dict: dict,\n                 data_party: list,\n                 result_party: list,\n                 results_dir: str):\n        log.info(f\"channel_config:{channel_config}\")\n        log.info(f\"cfg_dict:{cfg_dict}\")\n        log.info(f\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\")\n        assert isinstance(channel_config, str), \"type of channel_config must be str\"\n        assert isinstance(cfg_dict, dict), \"type of cfg_dict must be dict\"\n        assert isinstance(data_party, (list, tuple)), \"type of data_party must be list or tuple\"\n        assert isinstance(result_party, (list, tuple)), \"type of result_party must be list or tuple\"\n        assert isinstance(results_dir, str), \"type of results_dir must be str\"\n        \n        self.channel_config = channel_config\n        self.data_party = list(data_party)\n        self.result_party = list(result_party)\n        self.party_id = cfg_dict[\"party_id\"]\n        self.input_file = cfg_dict[\"data_party\"].get(\"input_file\")\n        self.key_column = cfg_dict[\"data_party\"].get(\"key_column\")\n        self.selected_columns = cfg_dict[\"data_party\"].get(\"selected_columns\")\n\n        dynamic_parameter = cfg_dict[\"dynamic_parameter\"]\n        self.label_owner = dynamic_parameter.get(\"label_owner\")\n        if self.party_id == self.label_owner:\n            self.label_column = dynamic_parameter.get(\"label_column\")\n            self.data_with_label = True\n        else:\n            self.label_column = \"\"\n            self.data_with_label = False\n                        \n        algorithm_parameter = dynamic_parameter[\"algorithm_parameter\"]\n        self.epochs = algorithm_parameter.get(\"epochs\", 10)\n        self.batch_size = algorithm_parameter.get(\"batch_size\", 256)\n        self.learning_rate = algorithm_parameter.get(\"learning_rate\", 0.001)\n        self.use_validation_set = algorithm_parameter.get(\"use_validation_set\", True)\n        self.validation_set_rate = algorithm_parameter.get(\"validation_set_rate\", 0.2)\n        self.predict_threshold = algorithm_parameter.get(\"predict_threshold\", 0.5)\n\n        self.output_file = os.path.join(results_dir, \"model\")\n        \n        self.check_parameters()\n\n    def check_parameters(self):\n        log.info(f\"check parameter start.\")        \n        assert self.epochs > 0, \"epochs must be greater 0\"\n        assert self.batch_size > 0, \"batch size must be greater 0\"\n        assert self.learning_rate > 0, \"learning rate must be greater 0\"\n        assert 0 < self.validation_set_rate < 1, \"validattion set rate must be between (0,1)\"\n        assert 0 <= self.predict_threshold <= 1, \"predict threshold must be between [0,1]\"\n        \n        if self.input_file:\n            self.input_file = self.input_file.strip()\n        if self.party_id in self.data_party:\n            if os.path.exists(self.input_file):\n                input_columns = pd.read_csv(self.input_file, nrows=0)\n                input_columns = list(input_columns.columns)\n                if self.key_column:\n                    assert self.key_column in input_columns, f\"key_column:{self.key_column} not in input_file\"\n                if self.selected_columns:\n                    error_col = []\n                    for col in self.selected_columns:\n                        if col not in input_columns:\n                            error_col.append(col)   \n                    assert not error_col, f\"selected_columns:{error_col} not in input_file\"\n                if self.label_column:\n                    assert self.label_column in input_columns, f\"label_column:{self.label_column} not in input_file\"\n            else:\n                raise Exception(f\"input_file is not exist. input_file={self.input_file}\")\n        log.info(f\"check parameter finish.\")\n                        \n        \n    def train(self):\n        '''\n        Linear regression training algorithm implementation function\n        '''\n\n        log.info(\"extract feature or label.\")\n        train_x, train_y, val_x, val_y = self.extract_feature_or_label(with_label=self.data_with_label)\n        \n        log.info(\"start create and set channel.\")\n        self.create_set_channel()\n        log.info(\"waiting other party connect...\")\n        rtt.activate(\"SecureNN\")\n        log.info(\"protocol has been activated.\")\n        \n        log.info(f\"start set save model. save to party: {self.result_party}\")\n        rtt.set_saver_model(False, plain_model=self.result_party)\n        # sharing data\n        log.info(f\"start sharing train data. data_owner={self.data_party}, label_owner={self.label_owner}\")\n        shard_x, shard_y = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(train_x, train_y, header=0)\n        log.info(\"finish sharing train data.\")\n        column_total_num = shard_x.shape[1]\n        log.info(f\"column_total_num = {column_total_num}.\")\n        \n        if self.use_validation_set:\n            log.info(\"start sharing validation data.\")\n            shard_x_val, shard_y_val = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(val_x, val_y, header=0)\n            log.info(\"finish sharing validation data.\")\n\n        if self.party_id not in self.data_party:  \n            # mean the compute party and result party\n            log.info(\"compute start.\")\n            X = tf.placeholder(tf.float64, [None, column_total_num])\n            Y = tf.placeholder(tf.float64, [None, 1])\n            W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\n            b = tf.Variable(tf.zeros([1], dtype=tf.float64))\n            pred_Y = tf.matmul(X, W) + b\n            loss = tf.square(Y - pred_Y)\n            loss = tf.reduce_mean(loss)\n            # optimizer\n            optimizer = tf.train.GradientDescentOptimizer(self.learning_rate).minimize(loss)\n            init = tf.global_variables_initializer()\n            saver = tf.train.Saver(var_list=None, max_to_keep=5, name='v2')\n            \n            reveal_Y = rtt.SecureReveal(pred_Y)\n            actual_Y = tf.placeholder(tf.float64, [None, 1])\n            reveal_Y_actual = rtt.SecureReveal(actual_Y)\n\n            with tf.Session() as sess:\n                log.info(\"session init.\")\n                sess.run(init)\n                # train\n                log.info(\"train start.\")\n                train_start_time = time.time()\n                batch_num = math.ceil(len(shard_x) / self.batch_size)\n                for e in range(self.epochs):\n                    for i in range(batch_num):\n                        bX = shard_x[(i * self.batch_size): (i + 1) * self.batch_size]\n                        bY = shard_y[(i * self.batch_size): (i + 1) * self.batch_size]\n                        sess.run(optimizer, feed_dict={X: bX, Y: bY})\n                        if (i % 50 == 0) or (i == batch_num - 1):\n                            log.info(f\"epoch:{e + 1}/{self.epochs}, batch:{i + 1}/{batch_num}\")\n                log.info(f\"model save to: {self.output_file}\")\n                saver.save(sess, self.output_file)\n                train_use_time = round(time.time()-train_start_time, 3)\n                log.info(f\"save model success. train_use_time={train_use_time}s\")\n                \n                if self.use_validation_set:\n                    Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x_val})\n                    log.info(f\"Y_pred:\\n {Y_pred[:10]}\")\n                    Y_actual = sess.run(reveal_Y_actual, feed_dict={actual_Y: shard_y_val})\n                    log.info(f\"Y_actual:\\n {Y_actual[:10]}\")\n        \n            running_stats = str(rtt.get_perf_stats(True)).replace('\\n', '').replace(' ', '')\n            log.info(f\"running stats: {running_stats}\")\n        else:\n            log.info(\"computing, please waiting for compute finish...\")\n        rtt.deactivate()\n     \n        log.info(\"remove temp dir.\")\n        if self.party_id in (self.data_party + self.result_party):\n            # self.remove_temp_dir()\n            pass\n        else:\n            # delete the model in the compute party.\n            self.remove_output_dir()\n        \n        if (self.party_id in self.result_party) and self.use_validation_set:\n            log.info(\"result_party evaluate model.\")\n            from sklearn.metrics import r2_score, mean_squared_error\n            Y_pred = Y_pred.astype(\"float\").reshape([-1, ])\n            Y_true = Y_actual.astype(\"float\").reshape([-1, ])\n            r2 = r2_score(Y_true, Y_pred)\n            rmse = np.sqrt(mean_squared_error(Y_true, Y_pred))\n            log.info(\"********************\")\n            log.info(f\"R Squared: {round(r2, 6)}\")\n            log.info(f\"RMSE: {round(rmse, 6)}\")\n            log.info(\"********************\")\n        log.info(\"train finish.\")\n    \n    def create_set_channel(self):\n        '''\n        create and set channel.\n        '''\n        io_channel = channel_sdk.grpc.APIManager()\n        log.info(\"start create channel\")\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\n        log.info(\"start set channel\")\n        rtt.set_channel(\"\", channel)\n        log.info(\"set channel success.\")\n    \n    def extract_feature_or_label(self, with_label: bool=False):\n        '''\n        Extract feature columns or label column from input file,\n        and then divide them into train set and validation set.\n        '''\n        train_x = \"\"\n        train_y = \"\"\n        val_x = \"\"\n        val_y = \"\"\n        temp_dir = self.get_temp_dir()\n        if self.party_id in self.data_party:\n            if self.input_file:\n                if with_label:\n                    usecols = self.selected_columns + [self.label_column]\n                else:\n                    usecols = self.selected_columns\n                \n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\"str\")\n                input_data = input_data[usecols]\n                # only if self.validation_set_rate==0, split_point==input_data.shape[0]\n                split_point = int(input_data.shape[0] * (1 - self.validation_set_rate))\n                assert split_point > 0, f\"train set is empty, because validation_set_rate:{self.validation_set_rate} is too big\"\n                \n                if with_label:\n                    y_data = input_data[self.label_column]\n                    train_y_data = y_data.iloc[:split_point]\n                    train_y = os.path.join(temp_dir, f\"train_y_{self.party_id}.csv\")\n                    train_y_data.to_csv(train_y, header=True, index=False)\n                    if self.use_validation_set:\n                        assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small\"\n                        val_y_data = y_data.iloc[split_point:]\n                        val_y = os.path.join(temp_dir, f\"val_y_{self.party_id}.csv\")\n                        val_y_data.to_csv(val_y, header=True, index=False)\n                    del input_data[self.label_column]\n                \n                x_data = input_data\n                train_x = os.path.join(temp_dir, f\"train_x_{self.party_id}.csv\")\n                x_data.iloc[:split_point].to_csv(train_x, header=True, index=False)\n                if self.use_validation_set:\n                    assert split_point < input_data.shape[0], \\\n                            f\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small.\"\n                    val_x = os.path.join(temp_dir, f\"val_x_{self.party_id}.csv\")\n                    x_data.iloc[split_point:].to_csv(val_x, header=True, index=False)\n            else:\n                raise Exception(f\"data_node {self.party_id} not have data. input_file:{self.input_file}\")\n        return train_x, train_y, val_x, val_y\n    \n    def get_temp_dir(self):\n        '''\n        Get the directory for temporarily saving files\n        '''\n        temp_dir = os.path.join(os.path.dirname(self.output_file), 'temp')\n        if not os.path.exists(temp_dir):\n            os.makedirs(temp_dir, exist_ok=True)\n        return temp_dir\n\n    def remove_temp_dir(self):\n        '''\n        Delete all files in the temporary directory, these files are some temporary data.\n        Only delete temp file.\n        '''\n        temp_dir = self.get_temp_dir()\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n    \n    def remove_output_dir(self):\n        '''\n        Delete all files in the temporary directory, these files are some temporary data.\n        This is used to delete all output files of the non-resulting party\n        '''\n        temp_dir = os.path.dirname(self.output_file)\n        if os.path.exists(temp_dir):\n            shutil.rmtree(temp_dir)\n\n\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\n    '''\n    This is the entrance to this module\n    '''\n    privacy_linear_reg = PrivacyLinearRegTrain(channel_config, cfg_dict, data_party, result_party, results_dir)\n    privacy_linear_reg.train()","dataSplitContractCode":""}},"resource":{"costMem":1073741824,"costCpu":1,"costGpu":2,"costBandwidth":3145728,"runTime":180000}}]}}

        // 查询运行状态
//        System.out.println("result = "  + getWorkflowStatusOfExpertMode(workflowId, workflowVersion));

        // 查询运行日志
//        System.out.println("result = "  + getWorkflowLogOfExpertMode(workflowId, workflowVersion));

        // 查询文件
//        System.out.println("result = "  + getWorkflowResultOfExpertMode(workflowId, workflowVersion, 1));

        // 复制工作流
//        System.out.println("result = "  + copyWorkflow(workflowId, workflowVersion, "copy-1"));

        // 删除工作流
//        System.out.println("result = "  + deleteWorkflow(workflowId));
    }


    // ----------------------通用功能----------------------------------------------

    @Test
    public void getWorkflowStats()throws Exception{
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap();
        System.out.println("result = "  + commonGetWithToken("/workflow/getWorkflowStats", parameters));
    }

    @Test
    public void getWorkflowList()throws Exception{
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap();
        System.out.println("result = "  + commonGetWithToken("/workflow/getWorkflowList", parameters));
    }

    @Test
    public void getWorkflowVersionList()throws Exception{
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap();
        parameters.add("workflowId", String.valueOf(1));
        System.out.println("result = "  + commonGetWithToken("/workflow/getWorkflowVersionList", parameters));
    }


    private String getWorkflowResultOfExpertMode(Long workflowId, Long workflowVersion, Integer nodeStep)throws Exception{
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap();
        parameters.add("workflowId", String.valueOf(workflowId));
        parameters.add("workflowVersion", String.valueOf(workflowVersion));
        parameters.add("nodeStep", String.valueOf(nodeStep));
        return commonGetWithToken("/workflow/expert/getWorkflowResultOfExpertMode", parameters);
    }

    private String getWorkflowLogOfExpertMode(Long workflowId, Long workflowVersion)throws Exception{
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap();
        parameters.add("workflowId", String.valueOf(workflowId));
        parameters.add("workflowVersion", String.valueOf(workflowVersion));
        return commonGetWithToken("/workflow/expert/getWorkflowLogOfExpertMode", parameters);
    }

    private String getWorkflowSettingOfExpertMode(Long workflowId, Long workflowVersion)throws Exception{
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap();
        parameters.add("workflowId", String.valueOf(workflowId));
        parameters.add("workflowVersion", String.valueOf(workflowVersion));
        return commonGetWithToken("/workflow/expert/getWorkflowSettingOfExpertMode", parameters);
    }

    private String getWorkflowStatusOfExpertMode(Long workflowId, Long workflowVersion) throws Exception{
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap();
        parameters.add("workflowId", String.valueOf(workflowId));
        parameters.add("workflowVersion", String.valueOf(workflowVersion));
        return commonGetWithToken("/workflow/expert/getWorkflowStatusOfExpertMode", parameters);
    }

    private String commonGetWithToken(String url, MultiValueMap<String, String> parameters)throws Exception{
        String result = mvc.perform(get(url)
                        .params(parameters)
                        .header("Accept-Language",lang)
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    private String deleteWorkflow(Long workflowId) throws Exception {
        JSONObject req = new JSONObject();
        req.put("workflowId", workflowId);
        return commonPostWithToken("/workflow/deleteWorkflow", req.toJSONString());
    }

    private String copy(Long workflowId, Long workflowVersion, String workflowVersionName) throws Exception {
        JSONObject req = new JSONObject();
        req.put("workflowId", workflowId);
        req.put("workflowVersion", workflowVersion);
        req.put("workflowVersionName", workflowVersionName);
        return commonPostWithToken("/workflow/copyWorkflow", req.toJSONString());
    }

    private String copyWorkflow(Long workflowId, Long workflowVersion, String workflowVersionName) throws Exception {
        JSONObject req = new JSONObject();
        req.put("workflowId", workflowId);
        req.put("workflowVersion", workflowVersion);
        req.put("workflowVersionName", workflowVersionName);
        return commonPostWithToken("/workflow/copyWorkflow", req.toJSONString());
    }

    private String createWorkflowOfExpertMode(String workflowName) throws Exception {
        JSONObject req = new JSONObject();
        req.put("workflowName", workflowName);
        return commonPostWithToken("/workflow/expert/createWorkflowOfExpertMode", req.toJSONString());
    }

    public String settingWorkflowOfExpertMode(String reqBody) throws Exception {
        return commonPostWithToken("/workflow/expert/settingWorkflowOfExpertMode", reqBody);
    }

    public String commonPostWithToken(String url, String reqBody) throws Exception {
        String result = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(reqBody)
                        .header("Accept-Language",lang)
                        .header("Access-Token",accessToken)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return result;
    }

    private String getWorkflowSettingReqBodyOfExpertModeCase3(Long workflowId, Long workflowVersion){
        String reqStr = "{\n" +
                "\t\"workflowId\": 3,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"workflowNodeList\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"algorithmId\": 2011,\n" +
                "\t\t\t\"nodeStep\": 1,\n" +
                "\t\t\t\"nodeCode\": {\n" +
                "\t\t\t\t\"code\": {\n" +
                "\t\t\t\t\t\"calculateContractCode\": \"# coding:utf-8\\n\\nimport os\\nimport sys\\nimport math\\nimport json\\nimport time\\nimport logging\\nimport shutil\\nimport numpy as np\\nimport pandas as pd\\nimport tensorflow as tf\\nimport latticex.rosetta as rtt\\nimport channel_sdk\\n\\n\\nnp.set_printoptions(suppress=True)\\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\\nos.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\\nlog = logging.getLogger(__name__)\\n\\nclass PrivacyLinearRegTrain(object):\\n    '''\\n    Privacy linear regression train base on rosetta.\\n    '''\\n\\n    def __init__(self,\\n                 channel_config: str,\\n                 cfg_dict: dict,\\n                 data_party: list,\\n                 result_party: list,\\n                 results_dir: str):\\n        log.info(f\\\"channel_config:{channel_config}\\\")\\n        log.info(f\\\"cfg_dict:{cfg_dict}\\\")\\n        log.info(f\\\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\\\")\\n        assert isinstance(channel_config, str), \\\"type of channel_config must be str\\\"\\n        assert isinstance(cfg_dict, dict), \\\"type of cfg_dict must be dict\\\"\\n        assert isinstance(data_party, (list, tuple)), \\\"type of data_party must be list or tuple\\\"\\n        assert isinstance(result_party, (list, tuple)), \\\"type of result_party must be list or tuple\\\"\\n        assert isinstance(results_dir, str), \\\"type of results_dir must be str\\\"\\n        \\n        self.channel_config = channel_config\\n        self.data_party = list(data_party)\\n        self.result_party = list(result_party)\\n        self.party_id = cfg_dict[\\\"party_id\\\"]\\n        self.input_file = cfg_dict[\\\"data_party\\\"].get(\\\"input_file\\\")\\n        self.key_column = cfg_dict[\\\"data_party\\\"].get(\\\"key_column\\\")\\n        self.selected_columns = cfg_dict[\\\"data_party\\\"].get(\\\"selected_columns\\\")\\n\\n        dynamic_parameter = cfg_dict[\\\"dynamic_parameter\\\"]\\n        self.label_owner = dynamic_parameter.get(\\\"label_owner\\\")\\n        if self.party_id == self.label_owner:\\n            self.label_column = dynamic_parameter.get(\\\"label_column\\\")\\n            self.data_with_label = True\\n        else:\\n            self.label_column = \\\"\\\"\\n            self.data_with_label = False\\n                        \\n        algorithm_parameter = dynamic_parameter[\\\"algorithm_parameter\\\"]\\n        self.epochs = algorithm_parameter.get(\\\"epochs\\\", 10)\\n        self.batch_size = algorithm_parameter.get(\\\"batch_size\\\", 256)\\n        self.learning_rate = algorithm_parameter.get(\\\"learning_rate\\\", 0.001)\\n        self.use_validation_set = algorithm_parameter.get(\\\"use_validation_set\\\", True)\\n        self.validation_set_rate = algorithm_parameter.get(\\\"validation_set_rate\\\", 0.2)\\n        self.predict_threshold = algorithm_parameter.get(\\\"predict_threshold\\\", 0.5)\\n\\n        self.output_file = os.path.join(results_dir, \\\"model\\\")\\n        \\n        self.check_parameters()\\n\\n    def check_parameters(self):\\n        log.info(f\\\"check parameter start.\\\")        \\n        assert self.epochs > 0, \\\"epochs must be greater 0\\\"\\n        assert self.batch_size > 0, \\\"batch size must be greater 0\\\"\\n        assert self.learning_rate > 0, \\\"learning rate must be greater 0\\\"\\n        assert 0 < self.validation_set_rate < 1, \\\"validattion set rate must be between (0,1)\\\"\\n        assert 0 <= self.predict_threshold <= 1, \\\"predict threshold must be between [0,1]\\\"\\n        \\n        if self.input_file:\\n            self.input_file = self.input_file.strip()\\n        if self.party_id in self.data_party:\\n            if os.path.exists(self.input_file):\\n                input_columns = pd.read_csv(self.input_file, nrows=0)\\n                input_columns = list(input_columns.columns)\\n                if self.key_column:\\n                    assert self.key_column in input_columns, f\\\"key_column:{self.key_column} not in input_file\\\"\\n                if self.selected_columns:\\n                    error_col = []\\n                    for col in self.selected_columns:\\n                        if col not in input_columns:\\n                            error_col.append(col)   \\n                    assert not error_col, f\\\"selected_columns:{error_col} not in input_file\\\"\\n                if self.label_column:\\n                    assert self.label_column in input_columns, f\\\"label_column:{self.label_column} not in input_file\\\"\\n            else:\\n                raise Exception(f\\\"input_file is not exist. input_file={self.input_file}\\\")\\n        log.info(f\\\"check parameter finish.\\\")\\n                        \\n        \\n    def train(self):\\n        '''\\n        Linear regression training algorithm implementation function\\n        '''\\n\\n        log.info(\\\"extract feature or label.\\\")\\n        train_x, train_y, val_x, val_y = self.extract_feature_or_label(with_label=self.data_with_label)\\n        \\n        log.info(\\\"start create and set channel.\\\")\\n        self.create_set_channel()\\n        log.info(\\\"waiting other party connect...\\\")\\n        rtt.activate(\\\"SecureNN\\\")\\n        log.info(\\\"protocol has been activated.\\\")\\n        \\n        log.info(f\\\"start set save model. save to party: {self.result_party}\\\")\\n        rtt.set_saver_model(False, plain_model=self.result_party)\\n        # sharing data\\n        log.info(f\\\"start sharing train data. data_owner={self.data_party}, label_owner={self.label_owner}\\\")\\n        shard_x, shard_y = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(train_x, train_y, header=0)\\n        log.info(\\\"finish sharing train data.\\\")\\n        column_total_num = shard_x.shape[1]\\n        log.info(f\\\"column_total_num = {column_total_num}.\\\")\\n        \\n        if self.use_validation_set:\\n            log.info(\\\"start sharing validation data.\\\")\\n            shard_x_val, shard_y_val = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(val_x, val_y, header=0)\\n            log.info(\\\"finish sharing validation data.\\\")\\n\\n        if self.party_id not in self.data_party:  \\n            # mean the compute party and result party\\n            log.info(\\\"compute start.\\\")\\n            X = tf.placeholder(tf.float64, [None, column_total_num])\\n            Y = tf.placeholder(tf.float64, [None, 1])\\n            W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\\n            b = tf.Variable(tf.zeros([1], dtype=tf.float64))\\n            pred_Y = tf.matmul(X, W) + b\\n            loss = tf.square(Y - pred_Y)\\n            loss = tf.reduce_mean(loss)\\n            # optimizer\\n            optimizer = tf.train.GradientDescentOptimizer(self.learning_rate).minimize(loss)\\n            init = tf.global_variables_initializer()\\n            saver = tf.train.Saver(var_list=None, max_to_keep=5, name='v2')\\n            \\n            reveal_Y = rtt.SecureReveal(pred_Y)\\n            actual_Y = tf.placeholder(tf.float64, [None, 1])\\n            reveal_Y_actual = rtt.SecureReveal(actual_Y)\\n\\n            with tf.Session() as sess:\\n                log.info(\\\"session init.\\\")\\n                sess.run(init)\\n                # train\\n                log.info(\\\"train start.\\\")\\n                train_start_time = time.time()\\n                batch_num = math.ceil(len(shard_x) / self.batch_size)\\n                for e in range(self.epochs):\\n                    for i in range(batch_num):\\n                        bX = shard_x[(i * self.batch_size): (i + 1) * self.batch_size]\\n                        bY = shard_y[(i * self.batch_size): (i + 1) * self.batch_size]\\n                        sess.run(optimizer, feed_dict={X: bX, Y: bY})\\n                        if (i % 50 == 0) or (i == batch_num - 1):\\n                            log.info(f\\\"epoch:{e + 1}/{self.epochs}, batch:{i + 1}/{batch_num}\\\")\\n                log.info(f\\\"model save to: {self.output_file}\\\")\\n                saver.save(sess, self.output_file)\\n                train_use_time = round(time.time()-train_start_time, 3)\\n                log.info(f\\\"save model success. train_use_time={train_use_time}s\\\")\\n                \\n                if self.use_validation_set:\\n                    Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x_val})\\n                    log.info(f\\\"Y_pred:\\\\n {Y_pred[:10]}\\\")\\n                    Y_actual = sess.run(reveal_Y_actual, feed_dict={actual_Y: shard_y_val})\\n                    log.info(f\\\"Y_actual:\\\\n {Y_actual[:10]}\\\")\\n        \\n            running_stats = str(rtt.get_perf_stats(True)).replace('\\\\n', '').replace(' ', '')\\n            log.info(f\\\"running stats: {running_stats}\\\")\\n        else:\\n            log.info(\\\"computing, please waiting for compute finish...\\\")\\n        rtt.deactivate()\\n     \\n        log.info(\\\"remove temp dir.\\\")\\n        if self.party_id in (self.data_party + self.result_party):\\n            # self.remove_temp_dir()\\n            pass\\n        else:\\n            # delete the model in the compute party.\\n            self.remove_output_dir()\\n        \\n        if (self.party_id in self.result_party) and self.use_validation_set:\\n            log.info(\\\"result_party evaluate model.\\\")\\n            from sklearn.metrics import r2_score, mean_squared_error\\n            Y_pred = Y_pred.astype(\\\"float\\\").reshape([-1, ])\\n            Y_true = Y_actual.astype(\\\"float\\\").reshape([-1, ])\\n            r2 = r2_score(Y_true, Y_pred)\\n            rmse = np.sqrt(mean_squared_error(Y_true, Y_pred))\\n            log.info(\\\"********************\\\")\\n            log.info(f\\\"R Squared: {round(r2, 6)}\\\")\\n            log.info(f\\\"RMSE: {round(rmse, 6)}\\\")\\n            log.info(\\\"********************\\\")\\n        log.info(\\\"train finish.\\\")\\n    \\n    def create_set_channel(self):\\n        '''\\n        create and set channel.\\n        '''\\n        io_channel = channel_sdk.grpc.APIManager()\\n        log.info(\\\"start create channel\\\")\\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\\n        log.info(\\\"start set channel\\\")\\n        rtt.set_channel(\\\"\\\", channel)\\n        log.info(\\\"set channel success.\\\")\\n    \\n    def extract_feature_or_label(self, with_label: bool=False):\\n        '''\\n        Extract feature columns or label column from input file,\\n        and then divide them into train set and validation set.\\n        '''\\n        train_x = \\\"\\\"\\n        train_y = \\\"\\\"\\n        val_x = \\\"\\\"\\n        val_y = \\\"\\\"\\n        temp_dir = self.get_temp_dir()\\n        if self.party_id in self.data_party:\\n            if self.input_file:\\n                if with_label:\\n                    usecols = self.selected_columns + [self.label_column]\\n                else:\\n                    usecols = self.selected_columns\\n                \\n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\\\"str\\\")\\n                input_data = input_data[usecols]\\n                # only if self.validation_set_rate==0, split_point==input_data.shape[0]\\n                split_point = int(input_data.shape[0] * (1 - self.validation_set_rate))\\n                assert split_point > 0, f\\\"train set is empty, because validation_set_rate:{self.validation_set_rate} is too big\\\"\\n                \\n                if with_label:\\n                    y_data = input_data[self.label_column]\\n                    train_y_data = y_data.iloc[:split_point]\\n                    train_y = os.path.join(temp_dir, f\\\"train_y_{self.party_id}.csv\\\")\\n                    train_y_data.to_csv(train_y, header=True, index=False)\\n                    if self.use_validation_set:\\n                        assert split_point < input_data.shape[0], \\\\\\n                            f\\\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small\\\"\\n                        val_y_data = y_data.iloc[split_point:]\\n                        val_y = os.path.join(temp_dir, f\\\"val_y_{self.party_id}.csv\\\")\\n                        val_y_data.to_csv(val_y, header=True, index=False)\\n                    del input_data[self.label_column]\\n                \\n                x_data = input_data\\n                train_x = os.path.join(temp_dir, f\\\"train_x_{self.party_id}.csv\\\")\\n                x_data.iloc[:split_point].to_csv(train_x, header=True, index=False)\\n                if self.use_validation_set:\\n                    assert split_point < input_data.shape[0], \\\\\\n                            f\\\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small.\\\"\\n                    val_x = os.path.join(temp_dir, f\\\"val_x_{self.party_id}.csv\\\")\\n                    x_data.iloc[split_point:].to_csv(val_x, header=True, index=False)\\n            else:\\n                raise Exception(f\\\"data_node {self.party_id} not have data. input_file:{self.input_file}\\\")\\n        return train_x, train_y, val_x, val_y\\n    \\n    def get_temp_dir(self):\\n        '''\\n        Get the directory for temporarily saving files\\n        '''\\n        temp_dir = os.path.join(os.path.dirname(self.output_file), 'temp')\\n        if not os.path.exists(temp_dir):\\n            os.makedirs(temp_dir, exist_ok=True)\\n        return temp_dir\\n\\n    def remove_temp_dir(self):\\n        '''\\n        Delete all files in the temporary directory, these files are some temporary data.\\n        Only delete temp file.\\n        '''\\n        temp_dir = self.get_temp_dir()\\n        if os.path.exists(temp_dir):\\n            shutil.rmtree(temp_dir)\\n    \\n    def remove_output_dir(self):\\n        '''\\n        Delete all files in the temporary directory, these files are some temporary data.\\n        This is used to delete all output files of the non-resulting party\\n        '''\\n        temp_dir = os.path.dirname(self.output_file)\\n        if os.path.exists(temp_dir):\\n            shutil.rmtree(temp_dir)\\n\\n\\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\\n    '''\\n    This is the entrance to this module\\n    '''\\n    privacy_linear_reg = PrivacyLinearRegTrain(channel_config, cfg_dict, data_party, result_party, results_dir)\\n    privacy_linear_reg.train()\",\n" +
                "\t\t\t\t\t\"dataSplitContractCode\": \"\",\n" +
                "\t\t\t\t\t\"editType\": 2\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"variableList\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"batch_size\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"256\",\n" +
                "\t\t\t\t\t\t\"varDesc\": \"111111111111111\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"epochs\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"10\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"learning_rate\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"0.1\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"predict_threshold\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"0.5\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"use_validation_set\",\n" +
                "\t\t\t\t\t\t\"varType\": 1,\n" +
                "\t\t\t\t\t\t\"varValue\": \"true\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"validation_set_rate\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"0.2\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeInput\": {\n" +
                "\t\t\t\t\"dataInputList\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 10,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\t\t\"inputModel\": false,\n" +
                "\t\t\t\t\"isPsi\": true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeName\": \"隐私线性回归训练\",\n" +
                "\t\t\t\"nodeOutput\": {\n" +
                "\t\t\t\t\"identityId\": [\n" +
                "\t\t\t\t\t\"identity:403931f2e18c4be2915e229b9065a208\",\n" +
                "\t\t\t\t\t\"identity:07c0119cb39e47f497ff581efd48e342\"\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"storePattern\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"resource\": {\n" +
                "\t\t\t\t\"costBandwidth\": 3,\n" +
                "\t\t\t\t\"costCpu\": 1,\n" +
                "\t\t\t\t\"costGpu\": 2,\n" +
                "\t\t\t\t\"costMem\": 1024,\n" +
                "\t\t\t\t\"runTime\": 3\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"algorithmId\": 2012,\n" +
                "\t\t\t\"nodeStep\": 2,\n" +
                "\t\t\t\"nodeCode\": {\n" +
                "\t\t\t\t\"code\": {\n" +
                "\t\t\t\t\t\"calculateContractCode\": \" as rtt\\nimport channel_sdk\\n\\n\\nnp.set_printoptions(suppress=True)\\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\\nos.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\\nlog = logging.getLogger(__name__)\\n\\nclass PrivacyLinearRegPredict(object):\\n    '''\\n    Privacy linear regression predict base on rosetta.\\n    '''\\n\\n    def __init__(self,\\n                 channel_config: str,\\n                 cfg_dict: dict,\\n                 data_party: list,\\n                 result_party: list,\\n                 results_dir: str):\\n        log.info(f\\\"channel_config:{channel_config}\\\")\\n        log.info(f\\\"cfg_dict:{cfg_dict}\\\")\\n        log.info(f\\\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\\\")\\n        assert isinstance(channel_config, str), \\\"type of channel_config must be str\\\"\\n        assert isinstance(cfg_dict, dict), \\\"type of cfg_dict must be dict\\\"\\n        assert isinstance(data_party, (list, tuple)), \\\"type of data_party must be list or tuple\\\"\\n        assert isinstance(result_party, (list, tuple)), \\\"type of result_party must be list or tuple\\\"\\n        assert isinstance(results_dir, str), \\\"type of results_dir must be str\\\"\\n        \\n        self.channel_config = channel_config\\n        self.data_party = list(data_party)\\n        self.result_party = list(result_party)\\n        self.party_id = cfg_dict[\\\"party_id\\\"]\\n        self.input_file = cfg_dict[\\\"data_party\\\"].get(\\\"input_file\\\")\\n        self.key_column = cfg_dict[\\\"data_party\\\"].get(\\\"key_column\\\")\\n        self.selected_columns = cfg_dict[\\\"data_party\\\"].get(\\\"selected_columns\\\")\\n        dynamic_parameter = cfg_dict[\\\"dynamic_parameter\\\"]\\n        self.model_restore_party = dynamic_parameter.get(\\\"model_restore_party\\\")\\n        self.model_path = dynamic_parameter.get(\\\"model_path\\\")\\n        self.model_file = os.path.join(self.model_path, \\\"model\\\")\\n        self.predict_threshold = dynamic_parameter.get(\\\"predict_threshold\\\", 0.5)        \\n        self.output_file = os.path.join(results_dir, \\\"result\\\")\\n        self.data_party.remove(self.model_restore_party)  # except restore party\\n        self.check_parameters()\\n\\n    def check_parameters(self):\\n        log.info(f\\\"check parameter start.\\\")        \\n        assert 0 <= self.predict_threshold <= 1, \\\"predict threshold must be between [0,1]\\\"\\n        \\n        if self.input_file:\\n            self.input_file = self.input_file.strip()\\n        if self.party_id in self.data_party:\\n            if os.path.exists(self.input_file):\\n                input_columns = pd.read_csv(self.input_file, nrows=0)\\n                input_columns = list(input_columns.columns)\\n                if self.key_column:\\n                    assert self.key_column in input_columns, f\\\"key_column:{self.key_column} not in input_file\\\"\\n                if self.selected_columns:\\n                    error_col = []\\n                    for col in self.selected_columns:\\n                        if col not in input_columns:\\n                            error_col.append(col)   \\n                    assert not error_col, f\\\"selected_columns:{error_col} not in input_file\\\"\\n            else:\\n                raise Exception(f\\\"input_file is not exist. input_file={self.input_file}\\\")\\n        if self.party_id == self.model_restore_party:\\n            assert os.path.exists(self.model_path), f\\\"model path not found. model_path={self.model_path}\\\"\\n        log.info(f\\\"check parameter finish.\\\")\\n       \\n\\n    def predict(self):\\n        '''\\n        Linear regression predict algorithm implementation function\\n        '''\\n\\n        log.info(\\\"extract feature or id.\\\")\\n        file_x, id_col = self.extract_feature_or_index()\\n        \\n        log.info(\\\"start create and set channel.\\\")\\n        self.create_set_channel()\\n        log.info(\\\"waiting other party connect...\\\")\\n        rtt.activate(\\\"SecureNN\\\")\\n        log.info(\\\"protocol has been activated.\\\")\\n        \\n        log.info(f\\\"start set restore model. restore party={self.model_restore_party}\\\")\\n        rtt.set_restore_model(False, plain_model=self.model_restore_party)\\n        # sharing data\\n        log.info(f\\\"start sharing data. data_owner={self.data_party}\\\")\\n        shard_x = rtt.PrivateDataset(data_owner=self.data_party).load_X(file_x, header=0)\\n        log.info(\\\"finish sharing data .\\\")\\n        column_total_num = shard_x.shape[1]\\n        log.info(f\\\"column_total_num = {column_total_num}.\\\")\\n\\n        X = tf.placeholder(tf.float64, [None, column_total_num])\\n        W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\\n        b = tf.Variable(tf.zeros([1], dtype=tf.float64))\\n        saver = tf.train.Saver(var_list=None, max_to_keep=5, name='v2')\\n        init = tf.global_variables_initializer()\\n        # predict\\n        pred_Y = tf.matmul(X, W) + b\\n        reveal_Y = rtt.SecureReveal(pred_Y)  # only reveal to result party\\n\\n        with tf.Session() as sess:\\n            log.info(\\\"session init.\\\")\\n            sess.run(init)\\n            log.info(\\\"start restore model.\\\")\\n            if self.party_id == self.model_restore_party:\\n                if os.path.exists(os.path.join(self.model_path, \\\"checkpoint\\\")):\\n                    log.info(f\\\"model restore from: {self.model_file}.\\\")\\n                    saver.restore(sess, self.model_file)\\n                else:\\n                    raise Exception(\\\"model not found or model damaged\\\")\\n            else:\\n                log.info(\\\"restore model...\\\")\\n                temp_file = os.path.join(self.get_temp_dir(), 'ckpt_temp_file')\\n                with open(temp_file, \\\"w\\\") as f:\\n                    pass\\n                saver.restore(sess, temp_file)\\n            log.info(\\\"finish restore model.\\\")\\n            \\n            # predict\\n            log.info(\\\"predict start.\\\")\\n            predict_start_time = time.time()\\n            Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x})\\n            log.debug(f\\\"Y_pred:\\\\n {Y_pred[:10]}\\\")\\n            predict_use_time = round(time.time() - predict_start_time, 3)\\n            log.info(f\\\"predict success. predict_use_time={predict_use_time}s\\\")\\n        rtt.deactivate()\\n        log.info(\\\"rtt deactivate finish.\\\")\\n        \\n        if self.party_id in self.result_party:\\n            log.info(\\\"predict result write to file.\\\")\\n            output_file_predict_prob = os.path.splitext(self.output_file)[0] + \\\"_predict.csv\\\"\\n            Y_pred = Y_pred.astype(\\\"float\\\")\\n            Y_result = pd.DataFrame(Y_pred, columns=[\\\"result\\\"])\\n            Y_result.to_csv(output_file_predict_prob, header=True, index=False)\\n        log.info(\\\"start remove temp dir.\\\")\\n        self.remove_temp_dir()\\n        log.info(\\\"predict finish.\\\")\\n\\n    def create_set_channel(self):\\n        '''\\n        create and set channel.\\n        '''\\n        io_channel = channel_sdk.grpc.APIManager()\\n        log.info(\\\"start create channel\\\")\\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\\n        log.info(\\\"start set channel\\\")\\n        rtt.set_channel(\\\"\\\", channel)\\n        log.info(\\\"set channel success.\\\")\\n        \\n    def extract_feature_or_index(self):\\n        '''\\n        Extract feature columns or index column from input file.\\n        '''\\n        file_x = \\\"\\\"\\n        id_col = None\\n        temp_dir = self.get_temp_dir()\\n        if self.party_id in self.data_party:\\n            if self.input_file:\\n                usecols = [self.key_column] + self.selected_columns\\n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\\\"str\\\")\\n                input_data = input_data[usecols]\\n                id_col = input_data[self.key_column]\\n                file_x = os.path.join(temp_dir, f\\\"file_x_{self.party_id}.csv\\\")\\n                x_data = input_data.drop(labels=self.key_column, axis=1)\\n                x_data.to_csv(file_x, header=True, index=False)\\n            else:\\n                raise Exception(f\\\"data_party:{self.party_id} not have data. input_file:{self.input_file}\\\")\\n        return file_x, id_col\\n    \\n    def get_temp_dir(self):\\n        '''\\n        Get the directory for temporarily saving files\\n        '''\\n        temp_dir = os.path.join(os.path.dirname(self.output_file), 'temp')\\n        if not os.path.exists(temp_dir):\\n            os.makedirs(temp_dir, exist_ok=True)\\n        return temp_dir\\n\\n    def remove_temp_dir(self):\\n        '''\\n        Delete all files in the temporary directory, these files are some temporary data.\\n        Only delete temp file.\\n        '''\\n        temp_dir = self.get_temp_dir()\\n        if os.path.exists(temp_dir):\\n            shutil.rmtree(temp_dir)\\n\\n\\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\\n    '''\\n    This is the entrance to this module\\n    '''\\n    privacy_linear_reg = PrivacyLinearRegPredict(channel_config, cfg_dict, data_party, result_party, results_dir)\\n    privacy_linear_reg.predict()\\n\",\n" +
                "\t\t\t\t\t\"dataSplitContractCode\": \"\",\n" +
                "\t\t\t\t\t\"editType\": 2\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"variableList\": []\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeInput\": {\n" +
                "\t\t\t\t\"dataInputList\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\t\t\"inputModel\": true,\n" +
                "\t\t\t\t\"isPsi\": true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeName\": \"隐私线性训练训练\",\n" +
                "\t\t\t\"nodeOutput\": {\n" +
                "\t\t\t\t\"identityId\": [\n" +
                "\t\t\t\t\t\"identity:403931f2e18c4be2915e229b9065a208\",\n" +
                "\t\t\t\t\t\"identity:07c0119cb39e47f497ff581efd48e342\"\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"storePattern\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"resource\": {\n" +
                "\t\t\t\t\"costBandwidth\": 3,\n" +
                "\t\t\t\t\"costCpu\": 1,\n" +
                "\t\t\t\t\"costGpu\": 2,\n" +
                "\t\t\t\t\"costMem\": 1024,\n" +
                "\t\t\t\t\"runTime\": 3\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        JSONObject reqJson = JSONObject.parseObject(reqStr);
        reqJson.put("workflowId", workflowId);
        reqJson.put("workflowVersion", workflowVersion);
        return reqJson.toJSONString();
    }

    private String getWorkflowSettingReqBodyOfExpertModeCase2(Long workflowId, Long workflowVersion){
        String reqStr = "{\n" +
                "\t\"workflowId\": 2,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"workflowNodeList\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"algorithmId\": 2012,\n" +
                "\t\t\t\"nodeStep\": 1,\n" +
                "\t\t\t\"nodeCode\": {\n" +
                "\t\t\t\t\"code\": {\n" +
                "\t\t\t\t\t\"calculateContractCode\": \" as rtt\\nimport channel_sdk\\n\\n\\nnp.set_printoptions(suppress=True)\\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\\nos.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\\nlog = logging.getLogger(__name__)\\n\\nclass PrivacyLinearRegPredict(object):\\n    '''\\n    Privacy linear regression predict base on rosetta.\\n    '''\\n\\n    def __init__(self,\\n                 channel_config: str,\\n                 cfg_dict: dict,\\n                 data_party: list,\\n                 result_party: list,\\n                 results_dir: str):\\n        log.info(f\\\"channel_config:{channel_config}\\\")\\n        log.info(f\\\"cfg_dict:{cfg_dict}\\\")\\n        log.info(f\\\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\\\")\\n        assert isinstance(channel_config, str), \\\"type of channel_config must be str\\\"\\n        assert isinstance(cfg_dict, dict), \\\"type of cfg_dict must be dict\\\"\\n        assert isinstance(data_party, (list, tuple)), \\\"type of data_party must be list or tuple\\\"\\n        assert isinstance(result_party, (list, tuple)), \\\"type of result_party must be list or tuple\\\"\\n        assert isinstance(results_dir, str), \\\"type of results_dir must be str\\\"\\n        \\n        self.channel_config = channel_config\\n        self.data_party = list(data_party)\\n        self.result_party = list(result_party)\\n        self.party_id = cfg_dict[\\\"party_id\\\"]\\n        self.input_file = cfg_dict[\\\"data_party\\\"].get(\\\"input_file\\\")\\n        self.key_column = cfg_dict[\\\"data_party\\\"].get(\\\"key_column\\\")\\n        self.selected_columns = cfg_dict[\\\"data_party\\\"].get(\\\"selected_columns\\\")\\n        dynamic_parameter = cfg_dict[\\\"dynamic_parameter\\\"]\\n        self.model_restore_party = dynamic_parameter.get(\\\"model_restore_party\\\")\\n        self.model_path = dynamic_parameter.get(\\\"model_path\\\")\\n        self.model_file = os.path.join(self.model_path, \\\"model\\\")\\n        self.predict_threshold = dynamic_parameter.get(\\\"predict_threshold\\\", 0.5)        \\n        self.output_file = os.path.join(results_dir, \\\"result\\\")\\n        self.data_party.remove(self.model_restore_party)  # except restore party\\n        self.check_parameters()\\n\\n    def check_parameters(self):\\n        log.info(f\\\"check parameter start.\\\")        \\n        assert 0 <= self.predict_threshold <= 1, \\\"predict threshold must be between [0,1]\\\"\\n        \\n        if self.input_file:\\n            self.input_file = self.input_file.strip()\\n        if self.party_id in self.data_party:\\n            if os.path.exists(self.input_file):\\n                input_columns = pd.read_csv(self.input_file, nrows=0)\\n                input_columns = list(input_columns.columns)\\n                if self.key_column:\\n                    assert self.key_column in input_columns, f\\\"key_column:{self.key_column} not in input_file\\\"\\n                if self.selected_columns:\\n                    error_col = []\\n                    for col in self.selected_columns:\\n                        if col not in input_columns:\\n                            error_col.append(col)   \\n                    assert not error_col, f\\\"selected_columns:{error_col} not in input_file\\\"\\n            else:\\n                raise Exception(f\\\"input_file is not exist. input_file={self.input_file}\\\")\\n        if self.party_id == self.model_restore_party:\\n            assert os.path.exists(self.model_path), f\\\"model path not found. model_path={self.model_path}\\\"\\n        log.info(f\\\"check parameter finish.\\\")\\n       \\n\\n    def predict(self):\\n        '''\\n        Linear regression predict algorithm implementation function\\n        '''\\n\\n        log.info(\\\"extract feature or id.\\\")\\n        file_x, id_col = self.extract_feature_or_index()\\n        \\n        log.info(\\\"start create and set channel.\\\")\\n        self.create_set_channel()\\n        log.info(\\\"waiting other party connect...\\\")\\n        rtt.activate(\\\"SecureNN\\\")\\n        log.info(\\\"protocol has been activated.\\\")\\n        \\n        log.info(f\\\"start set restore model. restore party={self.model_restore_party}\\\")\\n        rtt.set_restore_model(False, plain_model=self.model_restore_party)\\n        # sharing data\\n        log.info(f\\\"start sharing data. data_owner={self.data_party}\\\")\\n        shard_x = rtt.PrivateDataset(data_owner=self.data_party).load_X(file_x, header=0)\\n        log.info(\\\"finish sharing data .\\\")\\n        column_total_num = shard_x.shape[1]\\n        log.info(f\\\"column_total_num = {column_total_num}.\\\")\\n\\n        X = tf.placeholder(tf.float64, [None, column_total_num])\\n        W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\\n        b = tf.Variable(tf.zeros([1], dtype=tf.float64))\\n        saver = tf.train.Saver(var_list=None, max_to_keep=5, name='v2')\\n        init = tf.global_variables_initializer()\\n        # predict\\n        pred_Y = tf.matmul(X, W) + b\\n        reveal_Y = rtt.SecureReveal(pred_Y)  # only reveal to result party\\n\\n        with tf.Session() as sess:\\n            log.info(\\\"session init.\\\")\\n            sess.run(init)\\n            log.info(\\\"start restore model.\\\")\\n            if self.party_id == self.model_restore_party:\\n                if os.path.exists(os.path.join(self.model_path, \\\"checkpoint\\\")):\\n                    log.info(f\\\"model restore from: {self.model_file}.\\\")\\n                    saver.restore(sess, self.model_file)\\n                else:\\n                    raise Exception(\\\"model not found or model damaged\\\")\\n            else:\\n                log.info(\\\"restore model...\\\")\\n                temp_file = os.path.join(self.get_temp_dir(), 'ckpt_temp_file')\\n                with open(temp_file, \\\"w\\\") as f:\\n                    pass\\n                saver.restore(sess, temp_file)\\n            log.info(\\\"finish restore model.\\\")\\n            \\n            # predict\\n            log.info(\\\"predict start.\\\")\\n            predict_start_time = time.time()\\n            Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x})\\n            log.debug(f\\\"Y_pred:\\\\n {Y_pred[:10]}\\\")\\n            predict_use_time = round(time.time() - predict_start_time, 3)\\n            log.info(f\\\"predict success. predict_use_time={predict_use_time}s\\\")\\n        rtt.deactivate()\\n        log.info(\\\"rtt deactivate finish.\\\")\\n        \\n        if self.party_id in self.result_party:\\n            log.info(\\\"predict result write to file.\\\")\\n            output_file_predict_prob = os.path.splitext(self.output_file)[0] + \\\"_predict.csv\\\"\\n            Y_pred = Y_pred.astype(\\\"float\\\")\\n            Y_result = pd.DataFrame(Y_pred, columns=[\\\"result\\\"])\\n            Y_result.to_csv(output_file_predict_prob, header=True, index=False)\\n        log.info(\\\"start remove temp dir.\\\")\\n        self.remove_temp_dir()\\n        log.info(\\\"predict finish.\\\")\\n\\n    def create_set_channel(self):\\n        '''\\n        create and set channel.\\n        '''\\n        io_channel = channel_sdk.grpc.APIManager()\\n        log.info(\\\"start create channel\\\")\\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\\n        log.info(\\\"start set channel\\\")\\n        rtt.set_channel(\\\"\\\", channel)\\n        log.info(\\\"set channel success.\\\")\\n        \\n    def extract_feature_or_index(self):\\n        '''\\n        Extract feature columns or index column from input file.\\n        '''\\n        file_x = \\\"\\\"\\n        id_col = None\\n        temp_dir = self.get_temp_dir()\\n        if self.party_id in self.data_party:\\n            if self.input_file:\\n                usecols = [self.key_column] + self.selected_columns\\n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\\\"str\\\")\\n                input_data = input_data[usecols]\\n                id_col = input_data[self.key_column]\\n                file_x = os.path.join(temp_dir, f\\\"file_x_{self.party_id}.csv\\\")\\n                x_data = input_data.drop(labels=self.key_column, axis=1)\\n                x_data.to_csv(file_x, header=True, index=False)\\n            else:\\n                raise Exception(f\\\"data_party:{self.party_id} not have data. input_file:{self.input_file}\\\")\\n        return file_x, id_col\\n    \\n    def get_temp_dir(self):\\n        '''\\n        Get the directory for temporarily saving files\\n        '''\\n        temp_dir = os.path.join(os.path.dirname(self.output_file), 'temp')\\n        if not os.path.exists(temp_dir):\\n            os.makedirs(temp_dir, exist_ok=True)\\n        return temp_dir\\n\\n    def remove_temp_dir(self):\\n        '''\\n        Delete all files in the temporary directory, these files are some temporary data.\\n        Only delete temp file.\\n        '''\\n        temp_dir = self.get_temp_dir()\\n        if os.path.exists(temp_dir):\\n            shutil.rmtree(temp_dir)\\n\\n\\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\\n    '''\\n    This is the entrance to this module\\n    '''\\n    privacy_linear_reg = PrivacyLinearRegPredict(channel_config, cfg_dict, data_party, result_party, results_dir)\\n    privacy_linear_reg.predict()\\n\",\n" +
                "\t\t\t\t\t\"dataSplitContractCode\": \"\",\n" +
                "\t\t\t\t\t\"editType\": 2\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"variableList\": [\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeInput\": {\n" +
                "\t\t\t\t\"dataInputList\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\t\t\"inputModel\": true,\n" +
                "\t\t\t\t\"isPsi\": true,\n" +
                "\t\t\t\t\"model\": {\n" +
                "\t\t\t\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\t\t\t\"metaDataId\": \"metadata:0x0e4693a97c213d057cbbb69ae18a217628d943776592996a87e59c594bd095a8\",\n" +
                "\t\t\t\t\t\"name\": \"逻辑回归训练(task:0xec69f3029b125d975631134fcd996d8d5ef0f4a24f93e96db1271167de28f42a)\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeName\": \"隐私线性训练训练\",\n" +
                "\t\t\t\"nodeOutput\": {\n" +
                "\t\t\t\t\"identityId\": [\n" +
                "\t\t\t\t\t\"identity:403931f2e18c4be2915e229b9065a208\",\n" +
                "\t\t\t\t\t\"identity:07c0119cb39e47f497ff581efd48e342\"\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"storePattern\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"resource\": {\n" +
                "\t\t\t\t\"costBandwidth\": 3,\n" +
                "\t\t\t\t\"costCpu\": 1,\n" +
                "\t\t\t\t\"costGpu\": 2,\n" +
                "\t\t\t\t\"costMem\": 1024,\n" +
                "\t\t\t\t\"runTime\": 3\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        JSONObject reqJson = JSONObject.parseObject(reqStr);
        reqJson.put("workflowId", workflowId);
        reqJson.put("workflowVersion", workflowVersion);
        return reqJson.toJSONString();
    }

    private String getWorkflowSettingReqBodyOfExpertModeCase1(Long workflowId, Long workflowVersion){
        String reqStr = "{\n" +
                "\t\"workflowId\": 1,\n" +
                "\t\"workflowVersion\": 1,\n" +
                "\t\"workflowNodeList\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"algorithmId\": 2011,\n" +
                "\t\t\t\"nodeStep\": 1,\n" +
                "\t\t\t\"nodeCode\": {\n" +
                "\t\t\t\t\"code\": {\n" +
                "\t\t\t\t\t\"calculateContractCode\": \"# coding:utf-8\\n\\nimport os\\nimport sys\\nimport math\\nimport json\\nimport time\\nimport logging\\nimport shutil\\nimport numpy as np\\nimport pandas as pd\\nimport tensorflow as tf\\nimport latticex.rosetta as rtt\\nimport channel_sdk\\n\\n\\nnp.set_printoptions(suppress=True)\\ntf.compat.v1.logging.set_verbosity(tf.compat.v1.logging.ERROR)\\nos.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'\\nrtt.set_backend_loglevel(5)  # All(0), Trace(1), Debug(2), Info(3), Warn(4), Error(5), Fatal(6)\\nlog = logging.getLogger(__name__)\\n\\nclass PrivacyLinearRegTrain(object):\\n    '''\\n    Privacy linear regression train base on rosetta.\\n    '''\\n\\n    def __init__(self,\\n                 channel_config: str,\\n                 cfg_dict: dict,\\n                 data_party: list,\\n                 result_party: list,\\n                 results_dir: str):\\n        log.info(f\\\"channel_config:{channel_config}\\\")\\n        log.info(f\\\"cfg_dict:{cfg_dict}\\\")\\n        log.info(f\\\"data_party:{data_party}, result_party:{result_party}, results_dir:{results_dir}\\\")\\n        assert isinstance(channel_config, str), \\\"type of channel_config must be str\\\"\\n        assert isinstance(cfg_dict, dict), \\\"type of cfg_dict must be dict\\\"\\n        assert isinstance(data_party, (list, tuple)), \\\"type of data_party must be list or tuple\\\"\\n        assert isinstance(result_party, (list, tuple)), \\\"type of result_party must be list or tuple\\\"\\n        assert isinstance(results_dir, str), \\\"type of results_dir must be str\\\"\\n        \\n        self.channel_config = channel_config\\n        self.data_party = list(data_party)\\n        self.result_party = list(result_party)\\n        self.party_id = cfg_dict[\\\"party_id\\\"]\\n        self.input_file = cfg_dict[\\\"data_party\\\"].get(\\\"input_file\\\")\\n        self.key_column = cfg_dict[\\\"data_party\\\"].get(\\\"key_column\\\")\\n        self.selected_columns = cfg_dict[\\\"data_party\\\"].get(\\\"selected_columns\\\")\\n\\n        dynamic_parameter = cfg_dict[\\\"dynamic_parameter\\\"]\\n        self.label_owner = dynamic_parameter.get(\\\"label_owner\\\")\\n        if self.party_id == self.label_owner:\\n            self.label_column = dynamic_parameter.get(\\\"label_column\\\")\\n            self.data_with_label = True\\n        else:\\n            self.label_column = \\\"\\\"\\n            self.data_with_label = False\\n                        \\n        algorithm_parameter = dynamic_parameter[\\\"algorithm_parameter\\\"]\\n        self.epochs = algorithm_parameter.get(\\\"epochs\\\", 10)\\n        self.batch_size = algorithm_parameter.get(\\\"batch_size\\\", 256)\\n        self.learning_rate = algorithm_parameter.get(\\\"learning_rate\\\", 0.001)\\n        self.use_validation_set = algorithm_parameter.get(\\\"use_validation_set\\\", True)\\n        self.validation_set_rate = algorithm_parameter.get(\\\"validation_set_rate\\\", 0.2)\\n        self.predict_threshold = algorithm_parameter.get(\\\"predict_threshold\\\", 0.5)\\n\\n        self.output_file = os.path.join(results_dir, \\\"model\\\")\\n        \\n        self.check_parameters()\\n\\n    def check_parameters(self):\\n        log.info(f\\\"check parameter start.\\\")        \\n        assert self.epochs > 0, \\\"epochs must be greater 0\\\"\\n        assert self.batch_size > 0, \\\"batch size must be greater 0\\\"\\n        assert self.learning_rate > 0, \\\"learning rate must be greater 0\\\"\\n        assert 0 < self.validation_set_rate < 1, \\\"validattion set rate must be between (0,1)\\\"\\n        assert 0 <= self.predict_threshold <= 1, \\\"predict threshold must be between [0,1]\\\"\\n        \\n        if self.input_file:\\n            self.input_file = self.input_file.strip()\\n        if self.party_id in self.data_party:\\n            if os.path.exists(self.input_file):\\n                input_columns = pd.read_csv(self.input_file, nrows=0)\\n                input_columns = list(input_columns.columns)\\n                if self.key_column:\\n                    assert self.key_column in input_columns, f\\\"key_column:{self.key_column} not in input_file\\\"\\n                if self.selected_columns:\\n                    error_col = []\\n                    for col in self.selected_columns:\\n                        if col not in input_columns:\\n                            error_col.append(col)   \\n                    assert not error_col, f\\\"selected_columns:{error_col} not in input_file\\\"\\n                if self.label_column:\\n                    assert self.label_column in input_columns, f\\\"label_column:{self.label_column} not in input_file\\\"\\n            else:\\n                raise Exception(f\\\"input_file is not exist. input_file={self.input_file}\\\")\\n        log.info(f\\\"check parameter finish.\\\")\\n                        \\n        \\n    def train(self):\\n        '''\\n        Linear regression training algorithm implementation function\\n        '''\\n\\n        log.info(\\\"extract feature or label.\\\")\\n        train_x, train_y, val_x, val_y = self.extract_feature_or_label(with_label=self.data_with_label)\\n        \\n        log.info(\\\"start create and set channel.\\\")\\n        self.create_set_channel()\\n        log.info(\\\"waiting other party connect...\\\")\\n        rtt.activate(\\\"SecureNN\\\")\\n        log.info(\\\"protocol has been activated.\\\")\\n        \\n        log.info(f\\\"start set save model. save to party: {self.result_party}\\\")\\n        rtt.set_saver_model(False, plain_model=self.result_party)\\n        # sharing data\\n        log.info(f\\\"start sharing train data. data_owner={self.data_party}, label_owner={self.label_owner}\\\")\\n        shard_x, shard_y = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(train_x, train_y, header=0)\\n        log.info(\\\"finish sharing train data.\\\")\\n        column_total_num = shard_x.shape[1]\\n        log.info(f\\\"column_total_num = {column_total_num}.\\\")\\n        \\n        if self.use_validation_set:\\n            log.info(\\\"start sharing validation data.\\\")\\n            shard_x_val, shard_y_val = rtt.PrivateDataset(data_owner=self.data_party, label_owner=self.label_owner).load_data(val_x, val_y, header=0)\\n            log.info(\\\"finish sharing validation data.\\\")\\n\\n        if self.party_id not in self.data_party:  \\n            # mean the compute party and result party\\n            log.info(\\\"compute start.\\\")\\n            X = tf.placeholder(tf.float64, [None, column_total_num])\\n            Y = tf.placeholder(tf.float64, [None, 1])\\n            W = tf.Variable(tf.zeros([column_total_num, 1], dtype=tf.float64))\\n            b = tf.Variable(tf.zeros([1], dtype=tf.float64))\\n            pred_Y = tf.matmul(X, W) + b\\n            loss = tf.square(Y - pred_Y)\\n            loss = tf.reduce_mean(loss)\\n            # optimizer\\n            optimizer = tf.train.GradientDescentOptimizer(self.learning_rate).minimize(loss)\\n            init = tf.global_variables_initializer()\\n            saver = tf.train.Saver(var_list=None, max_to_keep=5, name='v2')\\n            \\n            reveal_Y = rtt.SecureReveal(pred_Y)\\n            actual_Y = tf.placeholder(tf.float64, [None, 1])\\n            reveal_Y_actual = rtt.SecureReveal(actual_Y)\\n\\n            with tf.Session() as sess:\\n                log.info(\\\"session init.\\\")\\n                sess.run(init)\\n                # train\\n                log.info(\\\"train start.\\\")\\n                train_start_time = time.time()\\n                batch_num = math.ceil(len(shard_x) / self.batch_size)\\n                for e in range(self.epochs):\\n                    for i in range(batch_num):\\n                        bX = shard_x[(i * self.batch_size): (i + 1) * self.batch_size]\\n                        bY = shard_y[(i * self.batch_size): (i + 1) * self.batch_size]\\n                        sess.run(optimizer, feed_dict={X: bX, Y: bY})\\n                        if (i % 50 == 0) or (i == batch_num - 1):\\n                            log.info(f\\\"epoch:{e + 1}/{self.epochs}, batch:{i + 1}/{batch_num}\\\")\\n                log.info(f\\\"model save to: {self.output_file}\\\")\\n                saver.save(sess, self.output_file)\\n                train_use_time = round(time.time()-train_start_time, 3)\\n                log.info(f\\\"save model success. train_use_time={train_use_time}s\\\")\\n                \\n                if self.use_validation_set:\\n                    Y_pred = sess.run(reveal_Y, feed_dict={X: shard_x_val})\\n                    log.info(f\\\"Y_pred:\\\\n {Y_pred[:10]}\\\")\\n                    Y_actual = sess.run(reveal_Y_actual, feed_dict={actual_Y: shard_y_val})\\n                    log.info(f\\\"Y_actual:\\\\n {Y_actual[:10]}\\\")\\n        \\n            running_stats = str(rtt.get_perf_stats(True)).replace('\\\\n', '').replace(' ', '')\\n            log.info(f\\\"running stats: {running_stats}\\\")\\n        else:\\n            log.info(\\\"computing, please waiting for compute finish...\\\")\\n        rtt.deactivate()\\n     \\n        log.info(\\\"remove temp dir.\\\")\\n        if self.party_id in (self.data_party + self.result_party):\\n            # self.remove_temp_dir()\\n            pass\\n        else:\\n            # delete the model in the compute party.\\n            self.remove_output_dir()\\n        \\n        if (self.party_id in self.result_party) and self.use_validation_set:\\n            log.info(\\\"result_party evaluate model.\\\")\\n            from sklearn.metrics import r2_score, mean_squared_error\\n            Y_pred = Y_pred.astype(\\\"float\\\").reshape([-1, ])\\n            Y_true = Y_actual.astype(\\\"float\\\").reshape([-1, ])\\n            r2 = r2_score(Y_true, Y_pred)\\n            rmse = np.sqrt(mean_squared_error(Y_true, Y_pred))\\n            log.info(\\\"********************\\\")\\n            log.info(f\\\"R Squared: {round(r2, 6)}\\\")\\n            log.info(f\\\"RMSE: {round(rmse, 6)}\\\")\\n            log.info(\\\"********************\\\")\\n        log.info(\\\"train finish.\\\")\\n    \\n    def create_set_channel(self):\\n        '''\\n        create and set channel.\\n        '''\\n        io_channel = channel_sdk.grpc.APIManager()\\n        log.info(\\\"start create channel\\\")\\n        channel = io_channel.create_channel(self.party_id, self.channel_config)\\n        log.info(\\\"start set channel\\\")\\n        rtt.set_channel(\\\"\\\", channel)\\n        log.info(\\\"set channel success.\\\")\\n    \\n    def extract_feature_or_label(self, with_label: bool=False):\\n        '''\\n        Extract feature columns or label column from input file,\\n        and then divide them into train set and validation set.\\n        '''\\n        train_x = \\\"\\\"\\n        train_y = \\\"\\\"\\n        val_x = \\\"\\\"\\n        val_y = \\\"\\\"\\n        temp_dir = self.get_temp_dir()\\n        if self.party_id in self.data_party:\\n            if self.input_file:\\n                if with_label:\\n                    usecols = self.selected_columns + [self.label_column]\\n                else:\\n                    usecols = self.selected_columns\\n                \\n                input_data = pd.read_csv(self.input_file, usecols=usecols, dtype=\\\"str\\\")\\n                input_data = input_data[usecols]\\n                # only if self.validation_set_rate==0, split_point==input_data.shape[0]\\n                split_point = int(input_data.shape[0] * (1 - self.validation_set_rate))\\n                assert split_point > 0, f\\\"train set is empty, because validation_set_rate:{self.validation_set_rate} is too big\\\"\\n                \\n                if with_label:\\n                    y_data = input_data[self.label_column]\\n                    train_y_data = y_data.iloc[:split_point]\\n                    train_y = os.path.join(temp_dir, f\\\"train_y_{self.party_id}.csv\\\")\\n                    train_y_data.to_csv(train_y, header=True, index=False)\\n                    if self.use_validation_set:\\n                        assert split_point < input_data.shape[0], \\\\\\n                            f\\\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small\\\"\\n                        val_y_data = y_data.iloc[split_point:]\\n                        val_y = os.path.join(temp_dir, f\\\"val_y_{self.party_id}.csv\\\")\\n                        val_y_data.to_csv(val_y, header=True, index=False)\\n                    del input_data[self.label_column]\\n                \\n                x_data = input_data\\n                train_x = os.path.join(temp_dir, f\\\"train_x_{self.party_id}.csv\\\")\\n                x_data.iloc[:split_point].to_csv(train_x, header=True, index=False)\\n                if self.use_validation_set:\\n                    assert split_point < input_data.shape[0], \\\\\\n                            f\\\"validation set is empty, because validation_set_rate:{self.validation_set_rate} is too small.\\\"\\n                    val_x = os.path.join(temp_dir, f\\\"val_x_{self.party_id}.csv\\\")\\n                    x_data.iloc[split_point:].to_csv(val_x, header=True, index=False)\\n            else:\\n                raise Exception(f\\\"data_node {self.party_id} not have data. input_file:{self.input_file}\\\")\\n        return train_x, train_y, val_x, val_y\\n    \\n    def get_temp_dir(self):\\n        '''\\n        Get the directory for temporarily saving files\\n        '''\\n        temp_dir = os.path.join(os.path.dirname(self.output_file), 'temp')\\n        if not os.path.exists(temp_dir):\\n            os.makedirs(temp_dir, exist_ok=True)\\n        return temp_dir\\n\\n    def remove_temp_dir(self):\\n        '''\\n        Delete all files in the temporary directory, these files are some temporary data.\\n        Only delete temp file.\\n        '''\\n        temp_dir = self.get_temp_dir()\\n        if os.path.exists(temp_dir):\\n            shutil.rmtree(temp_dir)\\n    \\n    def remove_output_dir(self):\\n        '''\\n        Delete all files in the temporary directory, these files are some temporary data.\\n        This is used to delete all output files of the non-resulting party\\n        '''\\n        temp_dir = os.path.dirname(self.output_file)\\n        if os.path.exists(temp_dir):\\n            shutil.rmtree(temp_dir)\\n\\n\\ndef main(channel_config: str, cfg_dict: dict, data_party: list, result_party: list, results_dir: str):\\n    '''\\n    This is the entrance to this module\\n    '''\\n    privacy_linear_reg = PrivacyLinearRegTrain(channel_config, cfg_dict, data_party, result_party, results_dir)\\n    privacy_linear_reg.train()\",\n" +
                "\t\t\t\t\t\"dataSplitContractCode\": \"\",\n" +
                "\t\t\t\t\t\"editType\": 2\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"variableList\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"batch_size\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"256\",\n" +
                "\t\t\t\t\t\t\"varDesc\": \"111111111111111\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"epochs\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"10\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"learning_rate\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"0.1\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"predict_threshold\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"0.5\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"use_validation_set\",\n" +
                "\t\t\t\t\t\t\"varType\": 1,\n" +
                "\t\t\t\t\t\t\"varValue\": \"true\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"varKey\": \"validation_set_rate\",\n" +
                "\t\t\t\t\t\t\"varType\": 2,\n" +
                "\t\t\t\t\t\t\"varValue\": \"0.2\",\n" +
                "\t\t\t\t\t\t\"varDesc\": null\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeInput\": {\n" +
                "\t\t\t\t\"dataInputList\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"12,13,14\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 10,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:f614f8ac21b44fe89926ad4f26ef5b07\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x65b1ae7e819b443413f46dd22c80b3f7bf24f36cf18512c033eee3096a847044\"\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"dataColumnIds\": \"2,3,4\",\n" +
                "\t\t\t\t\t\t\"dependentVariable\": 0,\n" +
                "\t\t\t\t\t\t\"identityId\": \"identity:3ddb63047d214ddd8187438a82841250\",\n" +
                "\t\t\t\t\t\t\"keyColumn\": 1,\n" +
                "\t\t\t\t\t\t\"metaDataId\": \"metadata:0x4c20858f152b13d36773c588ec9424e2001a4886732005e6edbd301825397bb6\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"identityId\": \"identity:e9eef460ea9c473993c6477915106eed\",\n" +
                "\t\t\t\t\"inputModel\": false,\n" +
                "\t\t\t\t\"isPsi\": true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"nodeName\": \"隐私线性回归训练\",\n" +
                "\t\t\t\"nodeOutput\": {\n" +
                "\t\t\t\t\"identityId\": [\n" +
                "\t\t\t\t\t\"identity:403931f2e18c4be2915e229b9065a208\",\n" +
                "\t\t\t\t\t\"identity:07c0119cb39e47f497ff581efd48e342\"\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\"storePattern\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\"resource\": {\n" +
                "\t\t\t\t\"costBandwidth\": 3,\n" +
                "\t\t\t\t\"costCpu\": 1,\n" +
                "\t\t\t\t\"costGpu\": 2,\n" +
                "\t\t\t\t\"costMem\": 1024,\n" +
                "\t\t\t\t\"runTime\": 3\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        JSONObject reqJson = JSONObject.parseObject(reqStr);
        reqJson.put("workflowId", workflowId);
        reqJson.put("workflowVersion", workflowVersion);
        return reqJson.toJSONString();
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
