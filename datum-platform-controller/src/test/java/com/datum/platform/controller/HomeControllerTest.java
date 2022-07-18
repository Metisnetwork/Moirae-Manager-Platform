package com.datum.platform.controller;

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
    public void queryNavigation1() throws Exception {
        emptyParameters.add("keyword", "did:pid:lat1eqpf8vxz7m64j25mkmwk39t3xf8zeltxrr2nql");
        System.out.println("result = "  + commonGet("/home/queryNavigation", emptyParameters));
    }

    @Test
    public void queryNavigation2() throws Exception {
        emptyParameters.add("keyword", "task:0x2efd7745846a3df8eebbf31fa836c5c62e4246df05074bdac95a6aa6d0c5f4bd");
        System.out.println("result = "  + commonGet("/home/queryNavigation", emptyParameters));
    }

    @Test
    public void getLatestTaskList() throws Exception {
        emptyParameters.add("size", "10");
        System.out.println("result = "  + commonGet("/home/getLatestTaskList", emptyParameters));
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
        System.out.println("result = "  + commonGet("/home/getDataUsedTop", emptyParameters));
    }
}
