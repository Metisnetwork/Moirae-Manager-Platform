package com.moirae.rosettaflow.controller;

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
public class HomeControllerTest extends BaseControllerTest {

    @Test
    public void queryNavigation() throws Exception {
        emptyParameters.add("keyword", "identity:6a9df99adf8e48ed94bed3b53f9ea4f7");
        System.out.println("result = "  + commonGet("/home/queryNavigation", emptyParameters));
    }

    @Test
    public void getLatestModelList() throws Exception {
        emptyParameters.add("size", "10");
        System.out.println("result = "  + commonGet("/home/getLatestModelList", emptyParameters));
    }

    @Test
    public void getGlobalStats() throws Exception {
        System.out.println("result = "  + commonGet("/home/getGlobalStats", emptyParameters));
    }

    @Test
    public void getTaskTrend() throws Exception {
        emptyParameters.add("size", "15");
        System.out.println("result = "  + commonGet("/home/getTaskTrend", emptyParameters));
    }

    @Test
    public void getOrgPowerTop() throws Exception {
        emptyParameters.add("size", "15");
        System.out.println("result = "  + commonGet("/home/getOrgPowerTop", emptyParameters));
    }

    @Test
    public void getDataTokenUsedTop() throws Exception {
        emptyParameters.add("size", "15");
        System.out.println("result = "  + commonGet("/home/getDataTokenUsedTop", emptyParameters));
    }
}
